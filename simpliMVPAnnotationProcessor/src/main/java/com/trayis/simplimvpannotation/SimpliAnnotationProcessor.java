package com.trayis.simplimvpannotation;

import com.trayis.simpliannotations.SimpliViewComponent;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
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
        StringBuilder packageBuilder = new StringBuilder();
        StringBuilder classBuilder = new StringBuilder();

        packageBuilder.append("package " + packageName + ";");

        packageBuilder.append("\n\nimport com.trayis.simplimvp.utils.SimpliProvider;");
        packageBuilder.append("\nimport com.trayis.simplimvp.presenter.SimpliPresenter;");
        packageBuilder.append("\nimport com.trayis.simplimvp.view.SimpliView;");
        packageBuilder.append("\n\nimport java.util.InvalidPropertiesFormatException;\n");

        classBuilder.append("\n\n/**\n * This is a generated class\n * Generated on " + new Date() + "\n * Do not modify.\n */");
        classBuilder.append("\npublic class SimpliMVPProvider<P extends SimpliPresenter, V extends SimpliView> implements SimpliProvider<P, V> {");

        classBuilder.append("\n\n\tprivate static final SimpliMVPProvider instance = new SimpliMVPProvider();");

        classBuilder.append("\n\n\tpublic static SimpliMVPProvider getInstance() {\n\t\treturn instance;\n\t}");

        classBuilder.append("\n\n\tprivate SimpliMVPProvider() {}");

        classBuilder.append("\n\n\t@Override\n\tpublic P getPresenter(V view) throws InvalidPropertiesFormatException {");

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

            packageBuilder.append("\nimport " + presenterName + ";");
            packageBuilder.append("\nimport " + viewName + ";");

            classBuilder.append("\n\n\t\tif (view instanceof " + viewName.substring(viewName.lastIndexOf(".") + 1) + ") {");
            classBuilder.append("\n\t\t\treturn (P)new " + presenterName.substring(presenterName.lastIndexOf(".") + 1) + "();\n\t\t}");
        }

        classBuilder.append("\n\n\t\tthrow new java.util.InvalidPropertiesFormatException(\"view of type\" + view.getClass() + \" is not supported.\");\n\n\t}\n\n}\n"); // close class

        try {
            JavaFileObject source = processingEnv.getFiler().createSourceFile(packageName + ".SimpliMVPProvider");
            Writer writer = source.openWriter();
            writer.write(packageBuilder.toString());
            writer.write(classBuilder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }

        return true;
    }

}
