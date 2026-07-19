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

    @Operation(summary = "수강 신청")
    @PostMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<EnrollmentResponse> enroll(
        @PathVariable Long courseId,
        @Valid @RequestBody CreateEnrollmentRequest request
    ) {
        EnrollmentResponse response = enrollmentService.enroll(courseId, request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @Operation(summary = "수강 확정")
    @PatchMapping("/enrollments/{enrollmentId}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Long enrollmentId) {
        enrollmentService.confirm(enrollmentId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "수강 취소")
    @PatchMapping("/enrollments/{enrollmentId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long enrollmentId) {
        enrollmentService.cancel(enrollmentId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내 수강 신청 목록 조회")
    @GetMapping("/enrollments")
    public ResponseEntity<List<EnrollmentResponse>> findMyEnrollments(
        @RequestHeader("X-Classmate-Id") Long classmateId
    ) {
        return ResponseEntity.ok(
            enrollmentService.findMyEnrollments(classmateId)
        );
    }

}
