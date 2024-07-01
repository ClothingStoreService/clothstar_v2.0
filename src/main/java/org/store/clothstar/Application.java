package org.store.clothstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.store.clothstar")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
