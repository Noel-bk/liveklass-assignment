package com.liveklass.assignment.repository;

import com.liveklass.assignment.entity.Classmate;
import com.liveklass.assignment.entity.Course;
import com.liveklass.assignment.entity.Enrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByClassmate(Classmate classmate);

    @EntityGraph(attributePaths = "course")
    List<Enrollment> findAllByClassmateIdOrderByCreatedAtDesc(Long classmateId);

    @EntityGraph(attributePaths = "course")
    Optional<Enrollment> findByIdAndClassmateId(Long enrollmentId, Long classmateId);

    Optional<Enrollment> findByCourseAndClassmate(Course course, Classmate classmate);

    long countByCourseId(Long courseId);
}
