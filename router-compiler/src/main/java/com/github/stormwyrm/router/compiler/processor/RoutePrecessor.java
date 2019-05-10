package com.github.stormwyrm.router.compiler.processor;

import com.github.stormwyrm.router.annotation.Route;
import com.github.stormwyrm.router.compiler.util.Consts;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class RoutePrecessor extends BaseProcessor {
    private ClassName routerInitializer = ClassName.get("com.github.stormwyrm.router.initializer",
            "IRouterInitializer");
    private ClassName router = ClassName.get("com.github.stormwyrm.router", "Router");


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Route.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations == null || annotations.size() == 0)
            return false;

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Route.class);
        parseRoute(elements);
        return true;
    }

    private void parseRoute(Set<? extends Element> elements) {
        String packageName = "com.github.stormwyrm.router";
        String className = "AptRouterInitializer";
        TypeSpec.Builder aptRouterInitClassBuilder = TypeSpec.classBuilder(className)
                .addJavadoc(Consts.WARNING_TIPS)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(routerInitializer)
                .addStaticBlock(CodeBlock.builder().add("$T.register(new $L());\n",router,className).build());

        //覆盖父类方法init方法
        MethodSpec.Builder aptRouterInitMethodBuilder = null;
        TypeElement routerInitTypeElement = elementUtils.getTypeElement(routerInitializer.toString());
        List<? extends Element> allMembers = elementUtils.getAllMembers(routerInitTypeElement);
        for (Element element : allMembers) {
            if ("init".equals(element.getSimpleName().toString())) {
                aptRouterInitMethodBuilder = MethodSpec.overriding((ExecutableElement) element);
                break;
            }
        }

        if (aptRouterInitMethodBuilder == null) {
            return;
        }

        for (Element element : elements) {
            Route routerActivity = element.getAnnotation(Route.class);
            String value = routerActivity.path();
            aptRouterInitMethodBuilder.addStatement("arg0.put($S,$T.class)", value, element.asType());
        }


        JavaFile javaFile = JavaFile.builder(packageName,
                aptRouterInitClassBuilder.addMethod(aptRouterInitMethodBuilder.build()).build())
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
