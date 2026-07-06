package com.enterprise.banking.ai.generator.test;

import com.enterprise.banking.ai.mapper.MappedAction;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.nio.file.Paths;
import java.util.List;

/**
 * Generates the executable TestNG class that consumes the generated Page Objects.
 */
public class TestClassGenerator {

    public void generateTestClass(String testClassName, String pageClassName, List<MappedAction> actions) throws Exception {
        
        ClassName pageClassType = ClassName.get("com.enterprise.generated.pages", pageClassName);

        MethodSpec.Builder testMethod = MethodSpec.methodBuilder("executeAiGeneratedTest")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(org.testng.annotations.Test.class)
                // FIX: Added URL navigation before interacting with the page
                .addStatement("this.driver.get($S)", "https://parabank.parasoft.com/parabank/index.htm")
                .addStatement("$T page = new $T(this.driver)", pageClassType, pageClassType);

        for (MappedAction action : actions) {
            String methodName = action.methodName();
            if (action.parameters().isEmpty()) {
                testMethod.addStatement("page.$L()", methodName);
            } else {
                testMethod.addStatement("page.$L($S)", methodName, "testuser"); // Mock data
            }
        }

        TypeSpec testClass = TypeSpec.classBuilder(testClassName)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ClassName.get("com.enterprise.generated.tests", "BaseTest"))
                .addMethod(testMethod.build())
                .build();

        JavaFile javaFile = JavaFile.builder("com.enterprise.generated.tests", testClass).build();
        javaFile.writeTo(Paths.get("generated-code"));
    }
}