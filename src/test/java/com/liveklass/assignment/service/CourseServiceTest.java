package com.liveklass.assignment.service;

import com.liveklass.assignment.dto.CourseResponse;
import com.liveklass.assignment.dto.CreateCourseRequest;
import com.liveklass.assignment.entity.CourseStatus;
import com.liveklass.assignment.entity.Creator;
import com.liveklass.assignment.repository.CreatorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CreatorRepository creatorRepository;

    @Test
    void createCourse_success() {
        Creator creator = creatorRepository.save(
            Creator.create("creator")
        );

        CreateCourseRequest request = new CreateCourseRequest(
            "Spring Course",
            "description",
            BigDecimal.valueOf(100_000),
            20,
            LocalDate.now(),
            LocalDate.now().plusDays(15)
        );

        CourseResponse response =
            courseService.createCourse(
                creator.getId(),
                request
            );

        assertNotNull(response.id());
        assertEquals("Spring Course", response.title());
        assertEquals(CourseStatus.DRAFT, response.status());
    }

}