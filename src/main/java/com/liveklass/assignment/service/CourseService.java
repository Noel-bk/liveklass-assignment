package com.liveklass.assignment.service;

import com.liveklass.assignment.common.exception.CourseNotFoundException;
import com.liveklass.assignment.common.exception.CreatorNotFoundException;
import com.liveklass.assignment.dto.CourseResponse;
import com.liveklass.assignment.dto.CreateCourseRequest;
import com.liveklass.assignment.entity.Course;
import com.liveklass.assignment.entity.CourseStatus;
import com.liveklass.assignment.entity.Creator;
import com.liveklass.assignment.repository.CourseRepository;
import com.liveklass.assignment.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CreatorRepository creatorRepository;

    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        Creator creator = getCreator(request.creatorId());

        Course course = Course.create(
            creator,
            request.title(),
            request.description(),
            request.price(),
            request.capacity(),
            request.startDate(),
            request.endDate()
        );

        Course savedCourse = courseRepository.save(course);

        return CourseResponse.from(savedCourse);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCourses(CourseStatus status) {
        List<Course> courses;

        if (status == null) {
            courses = courseRepository.findAllByOrderByCreatedAtDesc();
        } else {
            courses = courseRepository.findAllByStatusOrderByCreatedAtDesc(status);
        }

        return courses.stream()
            .map(CourseResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourse(Long courseId) {
        Course course = getCourseEntity(courseId);
        return CourseResponse.from(course);
    }

    private Creator getCreator(Long creatorId) {
        return creatorRepository.findById(creatorId)
            .orElseThrow(() -> new CreatorNotFoundException(creatorId));
    }

    private Course getCourseEntity(Long courseId) {
        return courseRepository.findById(courseId)
            .orElseThrow(() -> new CourseNotFoundException(courseId));
    }
}
