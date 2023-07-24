package dev.example.jpademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableTransactionManagement
public class App {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder(App.class).build();
        app.run(args);
    }
}

