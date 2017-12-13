package com.zinc.libprocessor.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zinc.libannotation.Param;
import com.zinc.libprocessor.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

import static com.zinc.libprocessor.utils.Consts.ACTIVITY_FULL_NAME;
import static com.zinc.libprocessor.utils.Consts.CLASS_JAVA_DOC;
import static com.zinc.libprocessor.utils.Consts.FRAGMENT_FULL_NAME;
import static com.zinc.libprocessor.utils.Consts.FRAGMENT_V4_FULL_NAME;
import static com.zinc.libprocessor.utils.Consts.INNER_CLASS_NAME;
import static com.zinc.libprocessor.utils.Consts.METHOD_INJECT;
import static com.zinc.libprocessor.utils.Consts.OPTION_MODULE_NAME;
import static com.zinc.libprocessor.utils.Consts.PACKAGE_NAME;
import static com.zinc.libprocessor.utils.Consts.PARAM_ANNOTATION_TYPE;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/13
 * @description
 */

@SupportedOptions(OPTION_MODULE_NAME)
@SupportedAnnotationTypes(PARAM_ANNOTATION_TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ParamProcessor extends AbstractProcessor {

    private String mModuleName;
    private Logger mLogger;

    private Map<TypeElement, List<Element>> mClzAndParams = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mModuleName = processingEnvironment.getOptions().get(OPTION_MODULE_NAME);
        mLogger = new Logger(processingEnvironment.getMessager());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Param.class);

        if (elements == null || elements.isEmpty()) {
            return true;
        }

        mLogger.info(String.format(">>> %s: ParamProcessor begin... <<<", mModuleName));
        parseParams(elements);

        try {
            generate();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mLogger.info(String.format(">>> %s: ParamProcessor end...", mModuleName));

        return true;
    }

    private void generate() throws IllegalAccessException, IOException {

        final String TARGET = "target";
        final String EXTARS = "extras";
        final String OBJ = "obj";

        //建立参数： Object obj
        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, OBJ).build();

        for (Map.Entry<TypeElement, List<Element>> entry : mClzAndParams.entrySet()) {

            //获取类
            TypeElement parent = entry.getKey();
            //获取参数
            List<Element> params = entry.getValue();

            //类的全路径（含包名）
            String qualifiedName = parent.getQualifiedName().toString();
            //类名
            String simpleName = parent.getSimpleName().toString();
            //包名
            String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
            //注入参数的文件名
            String fileName = simpleName + INNER_CLASS_NAME;

            //判断是否为视图
            boolean isActivity;
            if (isSubtype(parent, ACTIVITY_FULL_NAME)) {
                isActivity = true;
            } else if (isSubtype(parent, FRAGMENT_V4_FULL_NAME) || isSubtype(parent, FRAGMENT_FULL_NAME)) {
                isActivity = false;
            } else {
                throw new IllegalAccessException(String.format("The target %s must be Activity or Fragment.", simpleName));
            }

            mLogger.info(String.format(">>>Start to process injected params in %s...", simpleName));

            //@Override
            //public void inject(Object obj){}
            MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder(METHOD_INJECT)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(parameterSpec);

            //XXXActivity target = (XXXActivity) obj;
            injectMethodBuilder.addStatement("$T $L = ($T) $L", ClassName.get(parent), TARGET, ClassName.get(parent), OBJ);

            if (isActivity) {
                //Bundle extras = target.getIntent().getExtras();
                injectMethodBuilder.addStatement("$T $L = $L.getIntent().getExtras()", ClassName.get("android.os", "Bundle"), EXTARS, TARGET);
            } else {
                //Bundle extras = target.getArguments();
                injectMethodBuilder.addStatement("$T $L = $L.getArguments()", ClassName.get("android.os", "Bundle"), EXTARS, TARGET);
            }

            //遍历属性
            for (Element paramEle : params) {
                Param param = paramEle.getAnnotation(Param.class);

                //标记了@Param的属性
                String fieldName = paramEle.getSimpleName().toString();
                //若设置@Param(key="xxx")的key，则使用
                String key = isEmpty(param.key()) ? fieldName : param.key();

                StringBuilder statement = new StringBuilder();

                if (paramEle.getModifiers().contains(Modifier.PRIVATE)) {     //属性标为private
                    mLogger.warn(paramEle, String.format("Found private field %s, please remove 'private' modifier for a better performance.", fieldName));

                    String reflectName = "field_" + fieldName;

                    //try{
                    // Field field_XXX = Field.class.getDeclared(field_XXX)
                    injectMethodBuilder.beginControlFlow("try")
                            .addStatement("$T $L = $T.class.getDeclaredField($s)", ClassName.get(Field.class), reflectName, ClassName.get(Field.class), fieldName);

                    Object[] args;

                    //field_xxx.set(target,
                    statement.append("$L.set($L, $L.get")
                            .append(getAccessorType(paramEle.asType()))
                            .append("(")
                            .append("$S");

                    if (supportDefaultValue(paramEle.asType())) {
                        statement.append(", ($T) $L.get($L)");
                        args = new Object[]{reflectName, TARGET, EXTARS, key, ClassName.get(paramEle.asType()), reflectName, TARGET};
                    } else {
                        args = new Object[]{reflectName, TARGET, EXTARS, key};
                    }

                    statement.append("))");

                    injectMethodBuilder.addStatement(statement.toString(), args)
                            .nextControlFlow("catch($T e)", Exception.class)
                            .addStatement("e.printStackTrace()")
                            .endControlFlow();

                } else {

                    Object[] args;

                    //target.
                    statement.append("$L.$L = ($T) $L.get")
                            .append(getAccessorType(paramEle.asType()))
                            .append("(")
                            .append("$S");

                    if (supportDefaultValue(paramEle.asType())) {   //需默认值
                        statement.append(", $L.$L");
                        args = new Object[]{TARGET, fieldName, ClassName.get(paramEle.asType()), EXTARS, key, TARGET, fieldName};
                    } else {
                        args = new Object[]{TARGET, fieldName, ClassName.get(paramEle.asType()), EXTARS, key};
                    }

                    statement.append(")");

                    injectMethodBuilder.addStatement(statement.toString(), args);

                }

                TypeSpec typeSpec = TypeSpec.classBuilder(fileName)
                        .addJavadoc(CLASS_JAVA_DOC)
                        .addSuperinterface(ClassName.get(PACKAGE_NAME, "ParamInjector"))
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(injectMethodBuilder.build())
                        .build();

                JavaFile.builder(packageName, typeSpec).build().writeTo(processingEnv.getFiler());

                mLogger.info(String.format("Params in class %s have been processed: %s.", simpleName, fieldName));
            }

        }

    }

    private boolean supportDefaultValue(TypeMirror typeMirror) {
        if (typeMirror instanceof PrimitiveType) {
            return true;
        }
        if (isSubtype(typeMirror, "java.lang.String") || isSubtype(typeMirror, "java.lang.CharSequence")) {
            return true;
        }
        return false;
    }

    private String getAccessorType(TypeMirror typeMirror) {

        //primitiveType 含 boolean, byte, short, int, long, char, float, double.
        if (typeMirror instanceof PrimitiveType) {
            return typeMirror.toString().toUpperCase().charAt(0) + typeMirror.toString().substring(1);
        } else if (typeMirror instanceof DeclaredType) {
            Element element = ((DeclaredType) typeMirror).asElement();
            if (element instanceof TypeElement) {

                if (isSubtype(element, "java.util.List")) {       //ArrayList

                    List<? extends TypeMirror> typeArgs = ((DeclaredType) typeMirror).getTypeArguments();

                    if (typeArgs != null && !typeArgs.isEmpty()) {

                        TypeMirror argType = typeArgs.get(0);

                        if (isSubtype(argType, "java.lang.Integer")) {
                            return "IntergerArrayList";
                        } else if (isSubtype(argType, "java.lang.String")) {
                            return "StringArrayList";
                        } else if (isSubtype(argType, "java.lang.CahrSequence")) {
                            return "CharSequenceArrayList";
                        } else if (isSubtype(argType, "android.os.Parcelable")) {
                            return "ParcelableArrayList";
                        }

                    }
                } else if (isSubtype(element, "android.os.Bundle")) {
                    return "Bundle";
                } else if (isSubtype(element, "java.lang.String")) {
                    return "String";
                } else if (isSubtype(element, "java.lang.CharSequence")) {
                    return "CharSequence";
                } else if (isSubtype(element, "android.utils.SparseArray")) {
                    return "SparseParcelableArray";
                } else if (isSubtype(element, "android.os.Parcelable")) {
                    return "Parcelable";
                } else if (isSubtype(element, "java.io.Serializable")) {
                    return "Serializable";
                } else if (isSubtype(element, "android.os.IBinder")) {
                    return "Binder";
                }

            }
        } else if (typeMirror instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) typeMirror;
            TypeMirror compType = arrayType.getComponentType();

            if (compType instanceof PrimitiveType) {
                return compType.toString().toUpperCase().charAt(0) + compType.toString().substring(1) + "Array";
            } else if (compType instanceof DeclaredType) {
                Element compElement = ((DeclaredType) compType).asElement();
                if (isSubtype(compElement, "java.lang.String")) {
                    return "StringArray";
                } else if (isSubtype(compElement, "java.lang.CharSequence")) {
                    return "CharSequence";
                } else if (isSubtype(compElement, "android.os.Parcelable")) {
                    return "ParcelableArray";
                }
                return null;
            }

        }

        return null;

    }

    private void parseParams(Set<? extends Element> elements) {
        for (Element element : elements) {
            //获取标记有@Param注解的类
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            //将类和@Param注解的字段归类
            if (mClzAndParams.containsKey(enclosingElement)) {
                mClzAndParams.get(enclosingElement).add(element);
            } else {
                List<Element> params = new ArrayList<>();
                params.add(element);
                mClzAndParams.put(enclosingElement, params);
            }
        }
    }

    private boolean isSubtype(Element typeElement, String type) {
        return processingEnv.getTypeUtils().isSubtype(typeElement.asType(),
                processingEnv.getElementUtils().getTypeElement(type).asType());
    }

    private boolean isSubtype(TypeMirror typeElement, String type) {
        return processingEnv.getTypeUtils().isSubtype(typeElement,
                processingEnv.getElementUtils().getTypeElement(type).asType());
    }

    private boolean isEmpty(CharSequence content) {
        return content == null || content.length() == 0;
    }


}



















