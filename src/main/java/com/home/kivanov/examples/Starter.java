package com.home.kivanov.examples;

import com.home.kivanov.examples.config.MainConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Starter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.refresh();
        context.scan("com.home");
        context.register(MainConfiguration.class);
        Application bean = context.getBean(Application.class);
        bean.run();
        context.close();
    }
}
