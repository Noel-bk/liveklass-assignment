package com.liveklass.assignment.common.config;

import com.liveklass.assignment.entity.Classmate;
import com.liveklass.assignment.entity.Course;
import com.liveklass.assignment.entity.Creator;
import com.liveklass.assignment.repository.ClassmateRepository;
import com.liveklass.assignment.repository.CourseRepository;
import com.liveklass.assignment.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
        CreatorRepository creatorRepository,
        ClassmateRepository classmateRepository,
        CourseRepository courseRepository
    ) {
        return args -> {
            if (creatorRepository.count() > 0) {
                return;
            }

            Creator creator = Creator.create("John Creator");

            creatorRepository.save(creator);

            Classmate classmate = Classmate.create("Alice");

            classmateRepository.save(classmate);

            Course course = Course.create(
                creator,
                "YOLO",
                "Description goes here..",
                BigDecimal.valueOf(100_000),
                20,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(15)
            );

            course.open();

            courseRepository.save(course);
        };
    }
}
