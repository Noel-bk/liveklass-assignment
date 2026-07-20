package com.liveklass.assignment.controller;

import com.liveklass.assignment.dto.CreateEnrollmentRequest;
import com.liveklass.assignment.dto.EnrollmentResponse;
import com.liveklass.assignment.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Operation(summary = "수강 신청", description = "수강생이 강의를 신청 합니다")
    @PostMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<EnrollmentResponse> enroll(
        @PathVariable Long courseId,
        @RequestHeader("X-Classmate-Id") Long classmateId
    ) {
        EnrollmentResponse response = enrollmentService.enroll(courseId, classmateId);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @Operation(summary = "수강 확정", description = "해당 수강신청 강의에 대해 확정 처리합니다")
    @PatchMapping("/enrollments/{enrollmentId}/confirm")
    public ResponseEntity<Void> confirm(
        @PathVariable Long enrollmentId,
        @RequestHeader("X-Classmate-Id") Long classmateId
    ) {
        enrollmentService.confirm(enrollmentId, classmateId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "수강 취소", description = "해당 수강 신청을 취소합니다")
    @PatchMapping("/enrollments/{enrollmentId}/cancel")
    public ResponseEntity<Void> cancel(
        @PathVariable Long enrollmentId,
        @RequestHeader("X-Classmate-Id") Long classmateId
    ) {
        enrollmentService.cancel(enrollmentId, classmateId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내 수강 신청 목록 조회", description = "내 수강 신청 목록을 조회합니다")
    @GetMapping("/enrollments")
    public ResponseEntity<List<EnrollmentResponse>> findMyEnrollments(
        @RequestHeader("X-Classmate-Id") Long classmateId
    ) {
        return ResponseEntity.ok(
            enrollmentService.findMyEnrollments(classmateId)
        );
    }

}
