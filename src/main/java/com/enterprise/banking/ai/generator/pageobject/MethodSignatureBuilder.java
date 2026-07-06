package com.enterprise.banking.ai.generator.pageobject;

import com.enterprise.banking.ai.mapper.MappedAction;
import com.squareup.javapoet.MethodSpec;
import javax.lang.model.element.Modifier;

/**
 * Constructs robust Java method signatures applying the resolved parameters.
 */
public class MethodSignatureBuilder {

    /**
     * Builds a MethodSpec dynamically injecting parameters if the action requires input.
     *
     * @param mappedAction The mapped action configuration.
     * @param fieldName    The generated field name representing the locator.
     * @return A constructed MethodSpec ready for JavaPoet rendering.
     */
    public MethodSpec buildMethod(MappedAction mappedAction, String fieldName) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(mappedAction.methodName())
            .addModifiers(Modifier.PUBLIC);

        if ("INPUT".equals(mappedAction.actionType()) && !mappedAction.parameters().isEmpty()) {
            String paramName = mappedAction.parameters().get(0);
            builder.addParameter(String.class, paramName);
            builder.addStatement("this.driver.findElement($N).clear()", fieldName);
            builder.addStatement("this.driver.findElement($N).sendKeys($N)", fieldName, paramName);
        } else {
            builder.addStatement("this.driver.findElement($N).click()", fieldName);
        }

        return builder.build();
    }
}