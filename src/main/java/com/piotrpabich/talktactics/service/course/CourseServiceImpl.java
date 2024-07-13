package com.piotrpabich.talktactics.service.course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.course.*;
import com.piotrpabich.talktactics.entity.*;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.repository.*;
import com.piotrpabich.talktactics.service.user.UserService;
import com.piotrpabich.talktactics.util.PageUtil;
import com.piotrpabich.talktactics.util.QueryHelp;
import com.piotrpabich.talktactics.util.ValidationUtil;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;


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
        return items.stream().map(CourseNavbarDto::fromTuple).sorted(Comparator.comparing(t -> CourseLevel.fromString(t.level()))).toList();
    }
    @Override
    public Course getById(long id) {
        return courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Course.class, "id", String.valueOf(id)));
    }
    @Override
    public long countAll(CourseQueryCriteria criteria) {
        return courseRepository.count((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Course course) {
        userService.validateAdmin();
        courseRepository.save(course);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Course resources) {
        userService.validateAdmin();
        Course course = courseRepository.findById(resources.getId()).orElseGet(() -> {
            log.warn("Course not found with id: {}", resources.getId());
            return new Course();
        });
        ValidationUtil.isNull(course.getId(), "Course", "id", resources.getId());
        course.copy(resources);
        courseRepository.save(course);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        userService.validateAdmin();
        for(Long id: ids) {
            courseRepository.deleteById(id);
        }
    }

//  PRIVATE
}
