package com.enterprise.banking.ai.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Modifier;
import java.nio.file.Paths;

/**
 * Generates TestNG DataProvider skeletons to support data-driven execution
 * in subsequent phases.
 */
public class DataProviderSkeletonGenerator {

    public void generateSkeleton() throws Exception {
        AnnotationSpec dataProviderAnnotation = AnnotationSpec.builder(
            com.squareup.javapoet.ClassName.get("org.testng.annotations", "DataProvider")
        ).addMember("name", "$S", "aiGeneratedData").build();

        MethodSpec dataProviderMethod = MethodSpec.methodBuilder("provideData")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(dataProviderAnnotation)
            .returns(Object[][].class)
            .addComment("Placeholder for Excel Test Data reading logic (Phase 9)")
            .addStatement("return new Object[][] { { \"sampleData1\", \"sampleData2\" } }")
            .build();

        TypeSpec dataProviderClass = TypeSpec.classBuilder("AiDataProvider")
            .addModifiers(Modifier.PUBLIC)
            .addMethod(dataProviderMethod)
            .build();

        JavaFile javaFile = JavaFile.builder("com.enterprise.generated.utils", dataProviderClass).build();
        javaFile.writeTo(Paths.get("generated-code"));
    }
}