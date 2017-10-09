package com.trayis.simplimvpannotation;

import com.trayis.simpliannotations.SimpiViewComponent;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.trayis.simpliannotations.SimpiViewComponent")
public class SimpliAnnotationProcessor extends AbstractProcessor {

    private String packageName = "com.trayis.simplimvpannotation.generated";

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        StringBuilder builder = new StringBuilder()
                .append("package " + packageName + ";\n\n")
                .append("public class SimpliMVPProvider {")
                .append("\n\n\tprivate static final SimpliMVPProvider instance = new SimpliMVPProvider();")
                .append("\n\n\tpublic static SimpliMVPProvider getInstance() {\n\t\treturn instance;\n\t}")
                .append("\n\n\tpublic String getMessage() {\n")
                .append("\t\treturn \"");

        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element : roundEnv.getElementsAnnotatedWith(SimpiViewComponent.class)) {

            if (!(element instanceof TypeElement)) {
                System.err.println("This annotation is for Classes only");
                return true;
            }

            TypeElement typeElement = (TypeElement) element;
            DeclaredType declaredType = (DeclaredType) typeElement.getSuperclass();
            builder.append("Type: " + declaredType.getTypeArguments().get(0));

            // this is appending to the return statement
            String objectType = element.getSimpleName().toString();
            builder.append(objectType).append(" says hello!\"");
        }

        builder.append(";\n\t}\n\n}\n"); // close class

        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile(packageName + ".SimpliMVPProvider");

            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }

        return true;
    }

}
