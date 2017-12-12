//package com.zinc.libtest_poet;
//
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.MethodSpec;
//import com.squareup.javapoet.TypeSpec;
//
//import java.io.IOException;
//
//import javax.lang.model.element.Modifier;
//
///**
// * @author Jiang zinc
// * @date 创建时间：2017/11/29
// * @description
// */
//
//public class MyTestPoet {
//
//    public static void main(String []args) throws IOException {
//        generateHelloworld();
//    }
//
//    public static void generateHelloworld() throws IOException {
//        MethodSpec main = MethodSpec.methodBuilder("main") //main代表方法名
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)//Modifier 修饰的关键字
//                .addParameter(String[].class, "args") //添加string[]类型的名为args的参数
//                .addStatement("$T.out.println($S)", System.class,"Hello World")//添加代码，这里$T和$S后面会讲，这里其实就是添加了System,out.println("Hello World");
//                .build();
//        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")//HelloWorld是类名
//                .addModifiers(Modifier.FINAL,Modifier.PUBLIC)
//                .addMethod(main)  //在类中添加方法
//                .build();
//        JavaFile javaFile = JavaFile.builder("com.zinc.libprocessor.helloworld", typeSpec)
//                .build();
//        javaFile.writeTo(System.out);
//    }
//
//}