package com.seminfo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Documentação", version = "1", description = "API de Blog"))
public class SeminfoBackendApplication
{

    public static void main(String[] args) {
        SpringApplication.run(SeminfoBackendApplication.class, args);
    }

}
