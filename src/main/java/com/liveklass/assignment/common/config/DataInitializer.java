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
import java.util.ArrayList;
import java.util.List;

// Development purpose only.
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

            List<Creator> creators = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Creator creator = creatorRepository.save(
                    Creator.create("creator" + i)
                );

                creators.add(creator);

                classmateRepository.save(
                    Classmate.create("classmate" + i)
                );
            }

            Course course = Course.create(
                creators.get(0),
                "Sample Course #1",
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
