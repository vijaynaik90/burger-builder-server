package com.burgerapp.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

@WithSecurityContext(factory = WithMockBasicAuthSecurityContextFactory.class)
public @interface WithMockBasicAuth {
    String username() default "spring";

    String password() default "password";

    String[] roles() default {};
}
