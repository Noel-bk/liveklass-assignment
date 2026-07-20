package com.liveklass.assignment.controller;

import com.liveklass.assignment.dto.CourseResponse;
import com.liveklass.assignment.dto.CreateCourseRequest;
import com.liveklass.assignment.entity.CourseStatus;
import com.liveklass.assignment.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @Operation(summary = "강의 등록", description = "강의를 등록합니다")
    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> createCourse(
        @RequestHeader("X-Creator-Id") Long creatorId,
        @Valid @RequestBody CreateCourseRequest request
    ) {
        CourseResponse response = courseService.createCourse(creatorId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @Operation(summary = "강의 개설", description = "해당 강의를 개설합니다")
    @PatchMapping("/courses/{courseId}/open")
    public CourseResponse open(
        @RequestHeader("X-Creator-Id") Long creatorId,
        @PathVariable Long courseId
    ) {
        return courseService.open(courseId, creatorId);
    }

    @Operation(summary = "강의 목록 조회", description = "강의 목록을 조회합니다")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getCourses(
        @RequestParam(required = false) CourseStatus status
    ) {
        return ResponseEntity.ok(
            courseService.getCourses(status)
        );
    }

    @Operation(summary = "강의 상세 조회", description = "해당 강의를 조회합니다")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseResponse> getCourse(
        @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
            courseService.getCourse(courseId)
        );
    }

}
