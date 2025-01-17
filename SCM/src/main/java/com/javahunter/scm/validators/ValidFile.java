package com.javahunter.scm.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({
        ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface ValidFile {
    String message() default "Invalid File";

    Class<?> [] groups() default {};
    boolean checkEmpty() default true;
    Class<? extends Payload>[] payload() default {};
}
