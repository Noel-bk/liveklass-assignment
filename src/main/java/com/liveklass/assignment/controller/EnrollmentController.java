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

@RestController
@RequestMapping("/courses/{courseId}/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Operation(summary = "수강 신청")
    @PostMapping
    public ResponseEntity<EnrollmentResponse> enroll(
        @PathVariable Long courseId,
        @Valid @RequestBody CreateEnrollmentRequest request
    ) {
        EnrollmentResponse response = enrollmentService.enroll(courseId, request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
}
