package com.liveklass.assignment.service;

import com.liveklass.assignment.common.exception.*;
import com.liveklass.assignment.dto.EnrollmentResponse;
import com.liveklass.assignment.entity.Classmate;
import com.liveklass.assignment.entity.Course;
import com.liveklass.assignment.entity.Enrollment;
import com.liveklass.assignment.repository.ClassmateRepository;
import com.liveklass.assignment.repository.CourseRepository;
import com.liveklass.assignment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassmateRepository classmateRepository;

    @Transactional
    public EnrollmentResponse enroll(Long courseId, Long classmateId) {
        Course course = getCourse(courseId);
        Classmate classmate = getClassmate(classmateId);

        validateCourse(course);
        validateDuplicationEnrollment(course, classmate);

        Enrollment enrollment = Enrollment.create(course, classmate);

        course.increaseEnrollment();

        enrollmentRepository.save(enrollment);

        return EnrollmentResponse.from(enrollment);
    }

    @Transactional
    public void confirm(Long enrollmentId, Long classmateId) {
        Enrollment enrollment = getEnrollment(enrollmentId, classmateId);

        enrollment.confirm();
    }

    @Transactional
    public void cancel(Long enrollmentId, Long classmateId) {
        Enrollment enrollment = getEnrollment(enrollmentId, classmateId);

        enrollment.cancel();

        enrollment.getCourse().decreaseEnrollment();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findMyEnrollments(Long classmateId) {
        return enrollmentRepository.findAllByClassmateIdOrderByCreatedAtDesc(classmateId)
            .stream()
            .map(EnrollmentResponse::from)
            .toList();
    }

    private Course getCourse(Long courseId) {
        return courseRepository.findByIdWithLock(courseId)
            .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    private Classmate getClassmate(Long classmateId) {
        return classmateRepository.findById(classmateId)
            .orElseThrow(() -> new ClassmateNotFoundException(classmateId));
    }

    private void validateCourse(Course course) {
        if (!course.isOpen()) {
            throw new CourseNotOpenException(course.getId());
        }
        if (course.isFull()) {
            throw new CourseFullException(course.getId());
        }
    }

    private void validateDuplicationEnrollment(Course course, Classmate classmate) {
        enrollmentRepository.findByCourseAndClassmate(course, classmate)
            .ifPresent(enrollment -> {
                throw new DuplicateEnrollmentException(course.getId(), classmate.getId());
            });
    }

    private Enrollment getEnrollment(Long enrollmentId, Long classmateId) {
        return enrollmentRepository
            .findByIdAndClassmateId(enrollmentId, classmateId)
            .orElseThrow(() -> new EnrollmentNotFoundException(enrollmentId));
    }
}

