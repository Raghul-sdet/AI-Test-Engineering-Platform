package com.enterprise.banking.ai.generator.pageobject;

import com.enterprise.banking.ai.mapper.MappedAction;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Modifier;
import java.nio.file.Paths;
import java.util.List;

/**
 * Uses JavaPoet to translate mapped actions into executable Selenium Page Objects.
 * Updated to support correct Selenium By selector mapping.
 */
public class PageObjectMethodGenerator {

    private static final ClassName WEB_DRIVER = ClassName.get("org.openqa.selenium", "WebDriver");
    private static final ClassName BY = ClassName.get("org.openqa.selenium", "By");

    public void generateExecutablePageObject(String className, List<MappedAction> actions) throws Exception {
        FieldSpec driverField = FieldSpec.builder(WEB_DRIVER, "driver", Modifier.PRIVATE, Modifier.FINAL).build();

        MethodSpec constructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(WEB_DRIVER, "driver")
            .addStatement("this.driver = driver")
            .build();

        TypeSpec.Builder pageClassBuilder = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .addField(driverField)
            .addMethod(constructor);

        MethodSignatureBuilder signatureBuilder = new MethodSignatureBuilder();

        for (MappedAction action : actions) {
            if (action.targetElement().priorityLocator() == null) continue;

            String fieldName = action.methodName() + "Locator";
            String locatorStrategy = action.targetElement().priorityLocator().strategy();
            String locatorValue = action.targetElement().priorityLocator().value();

            // FIX: Map "css" strategy to "cssSelector"
            String seleniumStrategy = "css".equals(locatorStrategy) ? "cssSelector" : locatorStrategy;

            FieldSpec locatorField = FieldSpec.builder(BY, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$T.$L($S)", BY, seleniumStrategy, locatorValue)
                .build();
            
            pageClassBuilder.addField(locatorField);
            pageClassBuilder.addMethod(signatureBuilder.buildMethod(action, fieldName));
        }

        JavaFile javaFile = JavaFile.builder("com.enterprise.generated.pages", pageClassBuilder.build()).build();
        javaFile.writeTo(Paths.get("generated-code"));
    }
}