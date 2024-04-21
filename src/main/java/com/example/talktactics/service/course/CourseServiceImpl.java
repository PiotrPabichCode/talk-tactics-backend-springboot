package com.example.talktactics.service.course;

import com.example.talktactics.dto.course.CourseFilterDto;
import com.example.talktactics.dto.course.CourseNavbarDto;
import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.exception.CourseRuntimeException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.*;
import com.example.talktactics.service.user.UserService;
import com.example.talktactics.util.Constants;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.talktactics.specification.course.CourseSpecification.*;

@Service
@Transactional
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    public CourseServiceImpl(
            CourseRepository courseRepository,
            @Lazy UserService userService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
    }

//  PUBLIC
    @Override
    public Course create(Course course) throws CourseRuntimeException {
        userService.validateAdmin();
        return courseRepository.save(course);
    }

    @Override
    public List<CourseNavbarDto> getNavbarList() {
        List<Tuple> items = courseRepository.findNavbarList();
        return items.stream().map(CourseNavbarDto::fromTuple).toList();
    }
    @Override
    public Course getById(long id) throws CourseRuntimeException {
        return courseRepository.findById(id).orElseThrow(() -> new CourseRuntimeException(Constants.COURSE_NOT_FOUND_EXCEPTION));
    }
    @Override
    public List<CoursePreviewProjection> getPreviewList() throws CourseRuntimeException {
        return courseRepository.findCoursePreviews();
    }
    @Override
    public Course update(long id, Course newCourse) throws CourseRuntimeException {
        userService.validateAdmin();
        Course course = getById(id);

        course.setDescription(newCourse.getDescription());
        course.setLevel(newCourse.getLevel());
        course.setTitle(newCourse.getTitle());

        return course;
    }
    @Override
    public Page<Course> getCourseList(int page, int size, CourseFilterDto filters) {
        Specification<Course> courseSpecification = Specification.where(courseTitleContains(filters.getTitle()))
                .and(courseDescriptionContains(filters.getDescription()))
                .and(courseLevelIn(filters.getLevels()))
                .and(courseQuantityBetween(filters.getMinQuantity(), filters.getMaxQuantity()));
        Pageable pageRequest = PageRequest.of(page, size);

        return courseRepository.findAll(courseSpecification, pageRequest);
    }
    @Override
    public void delete(long id) throws CourseRuntimeException {
        userService.validateAdmin();
        if (!courseRepository.existsById(id)) {
            throw new CourseRuntimeException(Constants.COURSE_NOT_FOUND_EXCEPTION);
        }
        courseRepository.deleteById(id);
    }

//  PRIVATE
}
