package com.liveklass.assignment.repository;

import com.liveklass.assignment.entity.Course;
import com.liveklass.assignment.entity.CourseStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(CourseStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT c
        FROM Course c
        WHERE c.id = :id
        """)
    Optional<Course> findByIdWithLock(Long id);

    @EntityGraph(attributePaths = "creator")
    List<Course> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "creator")
    List<Course> findAllByStatusOrderByCreatedAtDesc(CourseStatus status);

    @EntityGraph(attributePaths = "creator")
    Optional<Course> findById(Long id);

    @EntityGraph(attributePaths = "creator")
    Optional<Course> findByIdAndCreatorId(Long courseId, Long creatorId);
}
