package com.liveklass.assignment.dto;

import com.liveklass.assignment.entity.Course;
import com.liveklass.assignment.entity.CourseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CourseResponse(
    Long id,
    Long creatorId,
    String title,
    String description,
    BigDecimal price,
    Integer capacity,
    Integer enrolledCount,
    CourseStatus status,
    LocalDate startDate,
    LocalDate endDate
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getCreator().getId(),
            course.getTitle(),
            course.getDescription(),
            course.getPrice(),
            course.getCapacity(),
            course.getEnrolledCount(),
            course.getStatus(),
            course.getStartDate(),
            course.getEndDate()
        );
    }
}
