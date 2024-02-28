package com.florin.SpringDataMongoDB;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongodbTemplate) {
        return args -> {
            Address address = new Address("Romania",
                    "Bucharest",
                    "10010");
            String email = "flori@gmail.com";
            Student student = new Student(
                    "Florin",
                    "Bogdan",
                    email,
                    Gender.MALE,
                    address,
                    List.of("Computer Science", "Java"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );
            repository.findStudentByEmail(email)
                    .ifPresentOrElse(s -> {
                        System.out.println(s + " already exists");
                    }, () -> {
                        System.out.println("Inserting student " + student);
                        repository.insert(student);
                    });
        };
    }
}
