package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.common.UuidResponse;
import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.dto.CourseNavbarDto;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.course.dto.CourseRequest;
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

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;

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
                .map(CourseDto::of);
    }

    @Override
    public List<CourseNavbarDto> getNavbarList() {
        return courseRepository.findNavbarList()
                .stream()
                .map(CourseNavbarDto::of)
                .toList();
    }

    @Override
    public Course getCourseByUuid(final UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(notFoundExceptionSupplier(uuid));
    }

    @Override
    public UuidResponse create(final CourseRequest request, final User requester) {
        validateIfUserAdmin(requester);
        final var course = courseMapper.convert(request);
        final var savedCourse = courseRepository.save(course);
        return UuidResponse.of(savedCourse.getUuid());
    }

    @Override
    public void update(final UUID courseUuid, final CourseRequest request, final User requester) {
        validateIfUserAdmin(requester);
        final var course = getCourseByUuid(courseUuid);
        final var updatedCourse = courseMapper.convert(course, request);
        courseRepository.save(updatedCourse);
    }

    @Override
    public void delete(final UUID courseUuid, final User requester) {
        if (!courseRepository.existsByUuid(courseUuid)) {
            throw new EntityNotFoundException(Course.class, "uuid", String.valueOf(courseUuid));
        }
        courseRepository.deleteByUuid(courseUuid);
    }

    private Specification<Course> queryAllSpecification(final CourseQueryCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder);
    }

    private Supplier<EntityNotFoundException> notFoundExceptionSupplier(final UUID uuid) {
        return () -> new EntityNotFoundException(Course.class, "uuid", String.valueOf(uuid));
    }
}
