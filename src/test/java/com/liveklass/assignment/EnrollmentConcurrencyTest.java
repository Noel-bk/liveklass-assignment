package com.liveklass.assignment;

import com.liveklass.assignment.common.exception.CourseFullException;
import com.liveklass.assignment.entity.Classmate;
import com.liveklass.assignment.entity.Course;
import com.liveklass.assignment.entity.Creator;
import com.liveklass.assignment.repository.ClassmateRepository;
import com.liveklass.assignment.repository.CourseRepository;
import com.liveklass.assignment.repository.CreatorRepository;
import com.liveklass.assignment.repository.EnrollmentRepository;
import com.liveklass.assignment.service.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EnrollmentConcurrencyTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private ClassmateRepository classmateRepository;

    @Test
    void shouldNotExceedCapacityWhenConcurrentEnroll() throws Exception {
        // 동시 실행 중 발생한 예외 타입을 저장
        ConcurrentLinkedQueue<Class<?>> exceptions = new ConcurrentLinkedQueue<>();

        // 테스트용 Creator 생성
        Creator creator = creatorRepository.save(
            Creator.create("creator")
        );

        // 정원이 3명인 테스트용 Course 생성
        Course course = courseRepository.save(
            Course.create(
                creator,
                "Concurrency Test",
                "Test",
                BigDecimal.valueOf(100_000),
                3,
                LocalDate.now(),
                LocalDate.now().plusDays(15)
            )
        );

        // 수강 신청 가능 상태로 변경
        course.open();
        courseRepository.save(course);

        // 테스트용 Classmate 10명 생성
        List<Classmate> classmates = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            classmates.add(
                classmateRepository.save(
                    Classmate.create("classmate" + i)
                )
            );
        }

        int threadCount = classmates.size();

        // 동시에 실행할 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 모든 스레드가 준비될 때까지 대기
        CountDownLatch readyLatch = new CountDownLatch(threadCount);

        // 모든 스레드를 동시에 시작시키기 위한 신호
        CountDownLatch startLatch = new CountDownLatch(1);

        // 모든 스레드 종료 대기
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();

        AtomicInteger fail = new AtomicInteger();

        for (Classmate classmate : classmates) {
            executorService.submit(() -> {
                // 현재 스레드 준비 완료
                readyLatch.countDown();

                try {
                    // 모든 스레드가 동시에 시작될 때까지 대기
                    startLatch.await();

                    enrollmentService.enroll(
                        course.getId(),
                        classmate.getId()
                    );

                    success.incrementAndGet();
                } catch (Exception e) {
                    exceptions.add(e.getClass());
                    fail.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // 모든 스레드 준비 완료 대기
        readyLatch.await();

        // 모든 스레드를 동시에 시작
        startLatch.countDown();

        // 모든 작업 종료 대기
        doneLatch.await();

        // 모든 작업이 완료된 후 ExecutorService 종료
        executorService.shutdown();

        Course updatedCourse = courseRepository.findById(course.getId()).orElseThrow();

        // 실제 수강 인원이 정원을 초과하지 않아야 함
        assertEquals(3, updatedCourse.getEnrolledCount());

        // Enrollment도 3건만 생성되어야 함
        assertEquals(3, enrollmentRepository.countByCourseId(course.getId()));

        // 성공한 신청은 3건이어야 함
        assertEquals(3, success.get());

        // 실패한 신청은 7건이어야 함
        assertEquals(7, fail.get());

        // 실패 원인은 모두 CourseFullException 이어야 함
        assertTrue(exceptions.stream().allMatch(ex -> ex == CourseFullException.class));
    }
}
