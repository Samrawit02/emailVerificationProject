package com.samrit.emailverificationprojject.appUser.registration;

import org.springframework.context.annotation.Configuration;

import java.util.function.Predicate;
@Configuration
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
        //TODO: Regex to validate email
        return true;
    }
}
