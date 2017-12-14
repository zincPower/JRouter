package com.zinc.libprocessor.processor;

import com.google.common.primitives.Chars;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.zinc.libannotation.Interceptor;
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

import static com.zinc.libprocessor.utils.Consts.CLASS_JAVA_DOC;
import static com.zinc.libprocessor.utils.Consts.HANDLE;
import static com.zinc.libprocessor.utils.Consts.INTERCEPTOR_ANNOTATION_TYPE;
import static com.zinc.libprocessor.utils.Consts.INTERCEPTOR_INTERFACE;
import static com.zinc.libprocessor.utils.Consts.INTERCEPTOR_TABLE;
import static com.zinc.libprocessor.utils.Consts.OPTION_MODULE_NAME;
import static com.zinc.libprocessor.utils.Consts.PACKAGE_NAME;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/13
 * @description
 */

@SupportedAnnotationTypes(INTERCEPTOR_ANNOTATION_TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions(OPTION_MODULE_NAME)
public class InterceptorProcessor extends AbstractProcessor {
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
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Interceptor.class);
        if (elements == null || elements.isEmpty()) {
            return true;
        }

        mLogger.info(String.format(">>> %s: InterceptorProcessor begin...", mModuleName));

        Set<TypeElement> typeElements = new HashSet<>();

        for (Element element : elements) {
            if (validateElement(element)) {
                typeElements.add((TypeElement) element);
            } else {
                mLogger.error(element, String.format("The annotated element is not a implementation class of %s...", INTERCEPTOR_INTERFACE));
            }
        }

        if (mModuleName != null) {
            //格式化module名
            String validModuleName = mModuleName.replace(".", "_").replace("-", "_");
            generateInterceptors(validModuleName, typeElements);
        }else {
            throw new RuntimeException(String.format("No option '%S' passed to Interceptor annotation processor.", OPTION_MODULE_NAME));
        }

        mLogger.info(String.format(">>> %S: InterceptorProcessor end.", mModuleName));

        return true;
    }

    private void generateInterceptors(String validModuleName, Set<TypeElement> typeElements) {

        //params
        TypeElement interceptorType = processingEnv.getElementUtils().getTypeElement(INTERCEPTOR_INTERFACE);

        //Map<String, Class<? extends RouteInterceptor>> map
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(ClassName.get(interceptorType))));
        ParameterSpec mapParameterSpec = ParameterSpec.builder(mapTypeName, "map").build();

        //@Override
        //public void handle(Map<String, Class<? extends RouteInterceptor>> map){}
        MethodSpec.Builder handleInterceptors = MethodSpec.methodBuilder(HANDLE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapParameterSpec);


        for (TypeElement typeElement : typeElements) {
            mLogger.info(String.format("Found interceptor: %s", typeElement.getQualifiedName()));
            Interceptor interceptor = typeElement.getAnnotation(Interceptor.class);
            String name = interceptor.value();
            //map.put("XXXIntercpetor", xxxInterceptor.class);
            handleInterceptors.addStatement("map.put($S, $T.class)", name, ClassName.get(typeElement));
        }

        TypeSpec type = TypeSpec.classBuilder(capitalize(validModuleName) + INTERCEPTOR_TABLE)
                .addSuperinterface(ClassName.get(PACKAGE_NAME, INTERCEPTOR_TABLE))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(handleInterceptors.build())
                .addJavadoc(CLASS_JAVA_DOC)
                .build();

        try {
            JavaFile.builder(PACKAGE_NAME, type).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean validateElement(Element element) {
        return element.getKind().isClass() && processingEnv.getTypeUtils().isAssignable(element.asType(),
                processingEnv.getElementUtils().getTypeElement(INTERCEPTOR_INTERFACE).asType());
    }

    /**
     * @date 创建时间 2017/12/14
     * @author Jiang zinc
     * @Description 格式化类名
     * @version
     */
    private String capitalize(CharSequence content) {
        return content.length() == 0 ? "" : "" + Character.toUpperCase(content.charAt(0)) + content.subSequence(1, content.length());
    }

}
