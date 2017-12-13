package com.zinc.libprocessor.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.zinc.libannotation.Route;
import com.zinc.libprocessor.utils.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.zinc.libprocessor.utils.Consts.ACTIVITY_FULL_NAME;
import static com.zinc.libprocessor.utils.Consts.CLASS_JAVA_DOC;
import static com.zinc.libprocessor.utils.Consts.FRAGMENT_FULL_NAME;
import static com.zinc.libprocessor.utils.Consts.FRAGMENT_V4_FULL_NAME;
import static com.zinc.libprocessor.utils.Consts.HANDLE;
import static com.zinc.libprocessor.utils.Consts.OPTION_MODULE_NAME;
import static com.zinc.libprocessor.utils.Consts.PACKAGE_NAME;
import static com.zinc.libprocessor.utils.Consts.ROUTER_TABLE;
import static com.zinc.libprocessor.utils.Consts.ROUTE_ANNOTATION_TYPE;
import static com.zinc.libprocessor.utils.Consts.ROUTE_FULL_NAME;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/11/29
 * @description 路由注解的解析器
 */

//该解析器支持的注解[这里指@Router]
@SupportedAnnotationTypes(ROUTE_ANNOTATION_TYPE)
//这个是要和gradle的匹对
@SupportedOptions(OPTION_MODULE_NAME)
//支持的编译jdk版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouteProcessor extends AbstractProcessor {

    private String mModuleName;
    private Logger mLogger;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mModuleName = processingEnvironment.getOptions().get(OPTION_MODULE_NAME);
        mLogger = new Logger(processingEnvironment.getMessager());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Route.class);

        if (elements == null || elements.size() == 0) {
            return true;
        }

        mLogger.info(String.format(">>> %s:RouterProcessor begin...", mModuleName));

        //将不为抽象类的视图保存
        Set<TypeElement> typeElements = new HashSet<>();
        for (Element element : elements) {
            //需要检测是否为视图
            if (validateElement(element)) {
                typeElements.add((TypeElement) element);
            }
        }

        if (mModuleName != null) {
            String validModuleName = mModuleName.replace(".", "_").replace("-", "_");
            generateRouteTable(validModuleName, typeElements);
            generateTargetInterceptors(validModuleName, typeElements);
        } else {
            throw new RuntimeException(String.format("No option '%s' passed to Route annotation processor.", OPTION_MODULE_NAME));
        }

        mLogger.info(String.format(">>> %s: RouteProcessor end.", mModuleName));

        return true;
    }

    private void generateTargetInterceptors(String validModuleName, Set<TypeElement> typeElements) {
    }

    //编写路由映射表
    private void generateRouteTable(String validModuleName, Set<TypeElement> typeElements) {

        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(Object.class)));
        ParameterSpec mapParameterSpec = ParameterSpec.builder(mapTypeName, "map").build();

        /**
         * 方法编写：
         * 方法名：handle
         * 注解：override
         * 作用域：public
         * 参数：TODO 这个还不太懂
         */
        MethodSpec.Builder methodHandle = MethodSpec.methodBuilder(HANDLE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapParameterSpec);

        //搜索有所属性
        for (TypeElement element : typeElements) {
            mLogger.info(String.format("Found routed target: %s", element.getQualifiedName()));
            Route route = element.getAnnotation(Route.class);
            String[] paths = route.value();
            //将route的映射加入方法handle
            for (String path : paths) {
                methodHandle.addStatement("map.put($S, $T.class)",path, ClassName.get(element));
            }
        }

        TypeElement interfaceType = processingEnv.getElementUtils().getTypeElement(ROUTE_FULL_NAME);
        TypeSpec type = TypeSpec.classBuilder(capitalize(validModuleName)+ROUTER_TABLE)
                .addSuperinterface(ClassName.get(interfaceType))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodHandle.build())
                .addJavadoc(CLASS_JAVA_DOC)
                .build();

        try {
            JavaFile.builder(PACKAGE_NAME, type).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检测是否为视图（activity，fragment）且不为抽象类
     *
     * @param typeElement
     * @return
     */
    private boolean validateElement(Element typeElement) {

        //检查是否为activity，fragment的子类
        if (!isSubtype(typeElement, ACTIVITY_FULL_NAME) && !isSubtype(typeElement, FRAGMENT_FULL_NAME)
                && !isSubtype(typeElement, FRAGMENT_V4_FULL_NAME)) {
            mLogger.error(typeElement, String.format("%s is not a subclass of Activity or Fragment",
                    typeElement.getSimpleName().toString()));
            return false;
        }

        //检查是否为抽象类
        Set<Modifier> modifiers = typeElement.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            mLogger.error(typeElement, String.format("The class %s is  abstract,You can't annotate abstract classes with @%s",
                    ((TypeElement) typeElement).getSimpleName(), Route.class.getSimpleName()));
            return false;
        }

        return true;

    }

    /**
     * 检测element是不是type的子类
     *
     * @param element
     * @param type
     * @return
     */
    private boolean isSubtype(Element element, String type) {

        return processingEnv.getTypeUtils().isSubtype(element.asType(),
                processingEnv.getElementUtils().getTypeElement(type).asType());

    }

    private String capitalize(CharSequence self){
        return self.length() == 0?
                "":""+Character.toUpperCase(self.charAt(0))+self.subSequence(1,self.length());
    }

}
