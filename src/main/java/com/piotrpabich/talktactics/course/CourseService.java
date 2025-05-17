package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.common.QueryHelp;
import com.piotrpabich.talktactics.common.UuidResponse;
import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.dto.CourseNavbarDto;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.course.dto.CourseRequest;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.word.CourseWordService;
import com.piotrpabich.talktactics.exception.ConflictException;
import com.piotrpabich.talktactics.exception.NotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseWordService courseWordService;

    public Page<CourseDto> queryAll(
            final CourseQueryCriteria criteria,
            final Pageable pageable
    ) {
        return courseRepository.findAll(queryAllSpecification(criteria), pageable)
                .map(CourseDto::of);
    }

    public List<CourseNavbarDto> getNavbarList() {
        return courseRepository.findNavbarList()
                .stream()
                .map(course -> {
                    final var wordUuid = courseWordService.getRandomUuidByCourseUuid(course.getUuid());
                    return CourseNavbarDto.of(course, wordUuid);
                })
                .toList();
    }

    public Course getCourseByUuid(final UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(notFoundExceptionSupplier(uuid));
    }

    public UuidResponse create(final CourseRequest request, final User requester) {
        validateIfUserAdmin(requester);
        final var course = courseMapper.convert(request);
        final var savedCourse = courseRepository.save(course);
        return UuidResponse.of(savedCourse.getUuid());
    }

    public void update(final UUID courseUuid, final CourseRequest request, final User requester) {
        validateIfUserAdmin(requester);
        final var course = getCourseByUuid(courseUuid);
        final var updatedCourse = courseMapper.convert(course, request);
        courseRepository.save(updatedCourse);
    }

    public void delete(final UUID courseUuid, final User requester) {
        validateIfUserAdmin(requester);
        if (!courseRepository.existsByUuid(courseUuid)) {
            throw new ConflictException(String.format("Course with uuid: %s already exists", courseUuid));
        }
        courseRepository.deleteByUuid(courseUuid);
    }

    private Specification<Course> queryAllSpecification(final CourseQueryCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder);
    }

    private Supplier<NotFoundException> notFoundExceptionSupplier(final UUID uuid) {
        return () -> new NotFoundException(String.format("Course with uuid: %s was not found", uuid));
    }
}
