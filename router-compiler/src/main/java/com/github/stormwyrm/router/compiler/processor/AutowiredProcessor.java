package com.github.stormwyrm.router.compiler.processor;

import com.github.stormwyrm.router.annotation.Autowired;
import com.github.stormwyrm.router.compiler.util.Consts;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.*;

import static com.github.stormwyrm.router.compiler.util.Consts.*;

@AutoService(Processor.class)
public class AutowiredProcessor extends BaseProcessor {
    private Map<TypeElement, List<Element>> map = new HashMap<>();//类和autowired的关系

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Autowired.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations != null && annotations.size() > 0) {
            parseAutowired(roundEnv.getElementsAnnotatedWith(Autowired.class));
            return true;
        }
        return false;
    }

    private void parseAutowired(Set<? extends Element> elements) {
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            if (map.containsKey(typeElement)) {
                map.get(typeElement).add(element);
            } else {
                List<Element> child = new ArrayList<>();
                map.put(typeElement, child);
            }
        }

        TypeElement iRouterInitializerTe = elementUtils.getTypeElement(Consts.I_AUTOWIRED_INITIALIZER);
        TypeMirror activityTm = elementUtils.getTypeElement(Consts.ACTIVITY).asType();

        for (Map.Entry<TypeElement, List<Element>> me : map.entrySet()) {
            TypeElement parent = me.getKey();
            List<Element> childs = me.getValue();

            String qualifiedName = parent.getQualifiedName().toString();
            String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
            String fileName = parent.getSimpleName() + "$$" + "AutowiredInitializer";

            TypeSpec.Builder typeSpec = TypeSpec
                    .classBuilder(fileName)
                    .addJavadoc(Consts.WARNING_TIPS)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(iRouterInitializerTe));

            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(TypeName.OBJECT, "thiz")
                    .addAnnotation(Override.class)
                    .addStatement("$T target = ($T) thiz", ClassName.get(parent.asType()), ClassName.get(parent.asType()));

            for (Element child : childs) {
                Autowired annotation = child.getAnnotation(Autowired.class);
                String fieldName = child.getSimpleName().toString();
                if (!annotation.name().equals("")) {
                    fieldName = annotation.name();
                }
                boolean isActivity = false;
                String statement = "target." + fieldName + " = target.";
                if (typeUtils.isSubtype(parent.asType(), activityTm)) {
                    isActivity = true;
                    statement += "getIntent().";
                } else {
                    throw new RuntimeException("Only support Activity");
                }
                methodSpec.addStatement(buildStatement(statement,child,fieldName, isActivity), fieldName);
            }

            try {
                JavaFile.builder(packageName, typeSpec.addMethod(methodSpec.build()).build()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String buildStatement(String statement, Element child,String fieldName, boolean isActivity) {
        TypeMirror typeMirror = child.asType();
        switch (typeMirror.toString()) {
            case BYTE:
                statement += "getByteExtra($S,target."+fieldName+")";
                break;
            case SHORT:
                statement += "getShortExtra($S,target."+fieldName+")";
                break;
            case INTEGER:
                statement += "getIntExtra($S,target."+fieldName+")";
                break;
            case LONG:
                statement += "getLongExtra($S,target."+fieldName+")";
                break;
            case FLOAT:
                statement += "getFloatExtra($S,target."+fieldName+")";
                break;
            case DOUBEL:
                statement += "getDoubleExtra($S,target."+fieldName+")";
                break;
            case CHAR:
                statement += "getCharExtra($S,target."+fieldName+")";
                break;
            case STRING:
                statement += "getStringExtra($S)";
                break;
            case PARCELABLE:
                statement += "getParcelableExtra($S)";
                break;
            case SERIALIZABLE:
                statement += "getSerializableExtra($S)";
                break;
        }
        return statement;
    }
}
