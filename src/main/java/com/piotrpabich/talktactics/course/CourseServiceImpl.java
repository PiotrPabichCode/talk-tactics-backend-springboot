package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.dto.CourseNavbarDto;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.auth.AuthUtil;
import com.piotrpabich.talktactics.common.util.PageUtil;
import com.piotrpabich.talktactics.common.QueryHelp;
import com.piotrpabich.talktactics.common.util.ValidationUtil;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

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
    @Transactional(rollbackFor = Exception.class)
    public void create(Course course, User requester) {
        AuthUtil.validateIfUserAdmin(requester);
        courseRepository.save(course);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Course resources, User requester) {
        AuthUtil.validateIfUserAdmin(requester);
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
    public void delete(Set<Long> ids, User requester) {
        AuthUtil.validateIfUserAdmin(requester);
        for(Long id: ids) {
            courseRepository.deleteById(id);
        }
    }

//  PRIVATE
}
