package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.dto.CourseNavbarDto;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.common.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;
import static com.piotrpabich.talktactics.course.entity.CourseLevel.fromString;
import static java.util.Comparator.comparing;
import static org.springframework.beans.BeanUtils.copyProperties;


@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public Page<CourseDto> queryAll(
            final CourseQueryCriteria criteria,
            final Pageable pageable
    ) {
        return courseRepository.findAll(queryAllSpecification(criteria), pageable)
                .map(courseMapper::toDto);
    }

    @Override
    public List<CourseNavbarDto> getNavbarList() {
        return courseRepository.findNavbarList()
                .stream()
                .map(CourseNavbarDto::fromTuple)
                .sorted(comparing(t -> fromString(t.level())))
                .toList();
    }

    @Override
    public Course getById(final Long id) {
        return courseRepository.findById(id)
                .orElseThrow(notFoundExceptionSupplier(id));
    }

    @Override
    @Transactional
    public void create(final Course course, final User requester) {
        validateIfUserAdmin(requester);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void update(final Course resources, final User requester) {
        validateIfUserAdmin(requester);
        final var course = getById(resources.getId());
        copyProperties(resources, course);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void delete(final Set<Long> ids, final User requester) {
        validateIfUserAdmin(requester);
        courseRepository.deleteAllById(ids);
    }

    private Specification<Course> queryAllSpecification(final CourseQueryCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder);
    }

    private Supplier<EntityNotFoundException> notFoundExceptionSupplier(final Long id) {
        return () -> new EntityNotFoundException(Course.class, "id", String.valueOf(id));
    }
}
