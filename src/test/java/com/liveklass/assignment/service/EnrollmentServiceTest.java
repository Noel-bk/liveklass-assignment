package com.liveklass.assignment.service;

import com.liveklass.assignment.dto.EnrollmentResponse;
import com.liveklass.assignment.entity.*;
import com.liveklass.assignment.repository.ClassmateRepository;
import com.liveklass.assignment.repository.CourseRepository;
import com.liveklass.assignment.repository.CreatorRepository;
import com.liveklass.assignment.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
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
class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private ClassmateRepository classmateRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private Course course;
    private Classmate classmate;

    @BeforeEach
    void setUp() {
        Creator creator = creatorRepository.save(
            Creator.create("creator")
        );

        course = Course.create(
            creator,
            "Course",
            "description",
            BigDecimal.valueOf(100000),
            10,
            LocalDate.now(),
            LocalDate.now().plusDays(30)
        );

        course.open();

        courseRepository.save(course);

        classmate = classmateRepository.save(
            Classmate.create("classmate")
        );
    }

    @Test
    void enroll_success() {
        EnrollmentResponse response =
            enrollmentService.enroll(
                course.getId(),
                classmate.getId()
            );

        assertNotNull(response.enrollmentId());

        assertEquals(
            EnrollmentStatus.PENDING,
            response.status()
        );
    }

    @Test
    void confirm_success() {
        EnrollmentResponse enrollment =
            enrollmentService.enroll(
                course.getId(),
                classmate.getId()
            );

        enrollmentService.confirm(
            enrollment.enrollmentId(),
            classmate.getId()
        );

        Enrollment updatedEnrollment =
            enrollmentRepository.findById(enrollment.enrollmentId())
                .orElseThrow();

        assertEquals(
            EnrollmentStatus.CONFIRMED,
            updatedEnrollment.getStatus()
        );

        assertNotNull(
            updatedEnrollment.getConfirmedAt()
        );
    }

    @Test
    void cancel_success() {
        EnrollmentResponse enrollment =
            enrollmentService.enroll(
                course.getId(),
                classmate.getId()
            );

        enrollmentService.confirm(
            enrollment.enrollmentId(),
            classmate.getId()
        );

        enrollmentService.cancel(
            enrollment.enrollmentId(),
            classmate.getId()
        );

        Enrollment updated =
            enrollmentRepository.findById(enrollment.enrollmentId())
                .orElseThrow();

        assertEquals(
            EnrollmentStatus.CANCELLED,
            updated.getStatus()
        );

        assertNotNull(
            updated.getCancelledAt()
        );
    }
}