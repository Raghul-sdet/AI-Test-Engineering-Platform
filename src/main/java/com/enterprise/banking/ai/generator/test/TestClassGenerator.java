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

    /**
     * @param testClassName the generated TestNG class name
     * @param pageClassName the generated Page Object class name this test drives
     * @param actions       the mapped actions to execute, in order
     * @param targetUrl     the site URL the generated test should navigate to first.
     *                      Previously hardcoded to the ParaBank demo site - now the caller
     *                      decides, which is what lets this framework target any website.
     */
    public void generateTestClass(String testClassName, String pageClassName, List<MappedAction> actions, String targetUrl) throws Exception {
        
        ClassName pageClassType = ClassName.get("com.enterprise.generated.pages", pageClassName);

        MethodSpec.Builder testMethod = MethodSpec.methodBuilder("executeAiGeneratedTest")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(org.testng.annotations.Test.class)
                .addStatement("this.driver.get($S)", targetUrl)
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