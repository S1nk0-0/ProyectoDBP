package com.example.proyectodbp;

import org.springframework.boot.SpringApplication;

public class TestProyectoDbpApplication {

    public static void main(String[] args) {
        SpringApplication.from(ProyectoDbpApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
