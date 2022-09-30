package com.darku.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author catalin
 */
@SpringBootApplication
public class AppStarter implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppStarter.class);

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.addViewController("/").setViewName("forward:/index.html");
    }

    public static void main(String[] args) {
        LOGGER.info("********* STARTING {} ************", AppStarter.class.getSimpleName());
        SpringApplication.run(AppStarter.class, args);
    }

}