package com.liveklass.assignment.entity;

import com.liveklass.assignment.common.exception.InvalidCoursePeriodException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
    name = "course",
    indexes = {
        @Index(name = "idx_course_status", columnList = "status"),
        @Index(name = "idx_course_creator", columnList = "creator_id")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Creator creator;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "enrolled_count", nullable = false)
    private Integer enrolledCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public boolean isOpen() {
        return status == CourseStatus.OPEN;
    }

    public boolean isFull() {
        return enrolledCount >= capacity;
    }

    public void increaseEnrollment() {
        enrolledCount++;
    }

    public void decreaseEnrollment() {
        enrolledCount--;
    }

    public void open() {
        if (status != CourseStatus.DRAFT) {
            throw new IllegalStateException("Only draft course can be opened.");
        }

        this.status = CourseStatus.OPEN;
    }

    public void close() {
        if (status != CourseStatus.OPEN) {
            throw new IllegalStateException("Only open course can be closed.");
        }

        this.status = CourseStatus.CLOSED;
    }

    public static Course create(
        Creator creator,
        String title,
        String description,
        BigDecimal price,
        Integer capacity,
        LocalDate startDate,
        LocalDate endDate
    ) {
        validateCoursePeriod(startDate, endDate);

        Course course = new Course();

        course.creator = creator;
        course.title = title;
        course.description = description;
        course.price = price;
        course.capacity = capacity;
        course.enrolledCount = 0;
        course.status = CourseStatus.DRAFT;
        course.startDate = startDate;
        course.endDate = endDate;

        return course;
    }

    private static void validateCoursePeriod(
        LocalDate startDate,
        LocalDate endDate
    ) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidCoursePeriodException(startDate, endDate);
        }
    }
}
