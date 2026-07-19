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

    @Operation(summary = "강의 등록")
    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> createCourse(
        @Valid @RequestBody CreateCourseRequest request
    ) {
        CourseResponse response = courseService.createCourse(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @Operation(summary = "강의 목록 조회")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getCourses(
        @RequestParam(required = false) CourseStatus status
    ) {
        return ResponseEntity.ok(
            courseService.getCourses(status)
        );
    }

    @Operation(summary = "강의 상세 조회")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseResponse> getCourse(
        @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
            courseService.getCourse(courseId)
        );
    }

}
