package com.liveklass.assignment.repository;

import com.liveklass.assignment.entity.Course;
import com.liveklass.assignment.entity.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(CourseStatus status);
}
