package com.trayis.simplimvpannotation;

import com.trayis.simpliannotations.SimpliViewComponent;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
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

@SupportedAnnotationTypes("com.trayis.simpliannotations.SimpliViewComponent")
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
        StringBuilder builder = new StringBuilder();

        builder.append("package " + packageName + ";");

        builder.append("\n\nimport com.trayis.simplimvp.utils.SimpliProvider;");
        builder.append("\nimport com.trayis.simplimvp.presenter.SimpliPresenter;");
        builder.append("\nimport com.trayis.simplimvp.view.SimpliView;");
        builder.append("\n\nimport java.util.InvalidPropertiesFormatException;");

        builder.append("\n\npublic class SimpliMVPProvider<P extends SimpliPresenter, V extends SimpliView> implements SimpliProvider<P, V> {");

        builder.append("\n\n\tprivate static final SimpliMVPProvider instance = new SimpliMVPProvider();");

        builder.append("\n\n\tpublic static SimpliMVPProvider getInstance() {\n\t\treturn instance;\n\t}");

        builder.append("\n\n\tprivate SimpliMVPProvider() {}");

        builder.append("\n\n\t@Override\n\tpublic P getPresenter(V view) throws InvalidPropertiesFormatException {");

        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element : roundEnv.getElementsAnnotatedWith(SimpliViewComponent.class)) {

            if (!(element instanceof TypeElement)) {
                System.err.println("This annotation is for Classes only");
                return true;
            }

            TypeElement typeElement = (TypeElement) element;
            DeclaredType declaredType = (DeclaredType) typeElement.getSuperclass();
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            String presenterName = String.valueOf(typeArguments.get(0));
            String viewName = String.valueOf(typeArguments.get(1));
            builder.append("\n\t\tif (view instanceof " + viewName + ") {");
            builder.append("\n\t\t\treturn (P)new " + presenterName + "();\n\t\t}");
        }

        builder.append("\n\t\tthrow new java.util.InvalidPropertiesFormatException(\"view of type\" + view.getClass() + \" is not supported.\");");
        builder.append("\n\t}\n\n}\n"); // close class

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
