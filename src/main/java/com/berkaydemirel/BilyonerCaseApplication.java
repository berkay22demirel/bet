package com.berkaydemirel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BilyonerCaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(BilyonerCaseApplication.class, args);
  }
}
