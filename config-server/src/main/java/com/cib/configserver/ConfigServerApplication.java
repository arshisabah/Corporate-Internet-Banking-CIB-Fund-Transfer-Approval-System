package com.cib.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Entry point for the centralized Spring Cloud Config Server.
 * Serves externalized application.yml configuration to every microservice
 * in the CIB Fund Transfer & Approval System from the local `config-repo`
 * (native filesystem backend). Runs standalone on port 8888 and is the
 * first service every other component contacts on startup.
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
