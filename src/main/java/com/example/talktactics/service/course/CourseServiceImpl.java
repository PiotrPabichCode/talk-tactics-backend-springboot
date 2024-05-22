package com.example.talktactics.service.course;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.course.*;
import com.example.talktactics.exception.CourseRuntimeException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.*;
import com.example.talktactics.service.user.UserService;
import com.example.talktactics.util.Constants;
import com.example.talktactics.util.PageUtil;
import com.example.talktactics.util.QueryHelp;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;


@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final CourseMapper courseMapper;

    public CourseServiceImpl(
            CourseRepository courseRepository,
            @Lazy UserService userService,
            CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.courseMapper = courseMapper;
    }

//  PUBLIC

    @Override
    public PageResult<CourseDto> queryAll(CourseQueryCriteria criteria, Pageable pageable) {
        Page<Course> page = courseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(courseMapper::toDto));
    }

    @Override
    public List<CourseNavbarDto> getNavbarList() {
        List<Tuple> items = courseRepository.findNavbarList();
        return items.stream().map(CourseNavbarDto::fromTuple).sorted(Comparator.comparing(t -> CourseLevel.fromString(t.getLevel()))).toList();
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
    @Transactional(rollbackFor = Exception.class)
    public void create(Course course) throws CourseRuntimeException {
        userService.validateAdmin();
        courseRepository.save(course);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(long id, Course newCourse) throws CourseRuntimeException {
        userService.validateAdmin();
        Course course = getById(id);

        course.setDescription(newCourse.getDescription());
        course.setLevel(newCourse.getLevel());
        course.setTitle(newCourse.getTitle());

        courseRepository.save(course);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) throws CourseRuntimeException {
        userService.validateAdmin();
        if (!courseRepository.existsById(id)) {
            throw new CourseRuntimeException(Constants.COURSE_NOT_FOUND_EXCEPTION);
        }
        courseRepository.deleteById(id);
    }

//  PRIVATE
}
