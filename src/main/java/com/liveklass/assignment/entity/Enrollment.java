package com.liveklass.assignment.entity;

import com.liveklass.assignment.common.exception.CancellationPeriodExpiredException;
import com.liveklass.assignment.common.exception.InvalidEnrollmentStatusException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
    name = "enrollment",
    indexes = {
        @Index(name = "idx_enrollment_course", columnList = "course_id"),
        @Index(name = "idx_enrollment_classmate", columnList = "classmate_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_course_classmate",
            columnNames = {"course_id", "classmate_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Enrollment {

    private static final long CANCELLATION_PERIOD_DAYS = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classmate_id", nullable = false)
    private Classmate classmate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void confirm() {
        if (status != EnrollmentStatus.PENDING) {
            throw new InvalidEnrollmentStatusException(id, status);
        }

        status = EnrollmentStatus.CONFIRMED;
        confirmedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status != EnrollmentStatus.CONFIRMED) {
            throw new InvalidEnrollmentStatusException(id, status);
        }

        if (confirmedAt.plusDays(CANCELLATION_PERIOD_DAYS).isBefore(LocalDateTime.now())) {
            throw new CancellationPeriodExpiredException(id);
        }

        status = EnrollmentStatus.CANCELLED;
        cancelledAt = LocalDateTime.now();
    }

    public static Enrollment create(Course course, Classmate classmate) {
        Enrollment enrollment = new Enrollment();

        enrollment.course = course;
        enrollment.classmate = classmate;
        enrollment.status = EnrollmentStatus.PENDING;

        return enrollment;
    }
}
