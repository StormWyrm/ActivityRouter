package com.github.stormwyrm.router_compiler;

import com.github.stormwyrm.router_annotation.RouterActivity;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class RouterPrecessor extends AbstractProcessor {
    private ClassName routerInitializer = ClassName.get("com.github.stormwyrm.router",
            "RouterInitializer");
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(RouterActivity.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() == 0)
            return false;

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterActivity.class);
        TypeSpec.Builder aptRouterInitClassBuilder = TypeSpec.classBuilder("Apt" + routerInitializer.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(routerInitializer)
                .addStaticBlock(CodeBlock.builder().add("Router.register(new $L());\n", "Apt" + routerInitializer.simpleName()).build());

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
            return false;
        }

        for (Element element : elements) {
            RouterActivity routerActivity = element.getAnnotation(RouterActivity.class);
            String value = routerActivity.value();
            aptRouterInitMethodBuilder.addStatement("arg0.put($S,$T.class)", value, element.asType());
        }


        JavaFile javaFile = JavaFile.builder("com.github.stormwyrm.router",
                aptRouterInitClassBuilder.addMethod(aptRouterInitMethodBuilder.build()).build())
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;

    }
}
