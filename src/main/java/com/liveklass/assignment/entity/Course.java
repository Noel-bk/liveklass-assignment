package com.liveklass.assignment.entity;

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
        if (isFull()) {
            throw new IllegalStateException("Course is full.");
        }
        enrolledCount++;
    }

    public void decreaseEnrollment() {
        if (enrolledCount > 0) {
            enrolledCount--;
        }
    }
}
