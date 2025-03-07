package com.piotrpabich.talktactics.course.participant;

import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.exception.ConflictException;
import com.piotrpabich.talktactics.exception.ForbiddenException;
import com.piotrpabich.talktactics.exception.NotFoundException;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantDto;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantQueryCriteria;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantRequest;
import com.piotrpabich.talktactics.course.participant.word.CourseParticipantWordRepository;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import com.piotrpabich.talktactics.common.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.piotrpabich.talktactics.auth.AuthUtil.isUserAdmin;
import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseParticipantService {

    private final CourseParticipantWordRepository courseParticipantWordRepository;
    private final CourseParticipantRepository courseParticipantRepository;
    private final CourseParticipantMapper courseParticipantMapper;

    public Page<CourseParticipantDto> queryAll(
            final CourseParticipantQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        validateQueryAll(criteria.getUserUuids(), requester);
        return courseParticipantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable)
                .map(CourseParticipantDto::of);
    }

    public CourseParticipant getCourseParticipantByUuid(final UUID uuid, final User requester) {
        final var courseParticipant = courseParticipantRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException(String.format("Course participant with uuid %s not found", uuid)));
        validateIfUserHimselfOrAdmin(requester, courseParticipant.getUser());
        return courseParticipant;
    }

    public void assignCourseToUser(final User user, final Course course) {
        validateIfCourseParticipantExists(course.getUuid(), user.getUuid());
        final var courseParticipant = new CourseParticipant(user, course);
        final var courseParticipantWords = courseParticipantMapper.convert(courseParticipant);

        courseParticipantRepository.save(courseParticipant);
        courseParticipantWordRepository.saveAll(courseParticipantWords);
    }

    public void removeCourseFromUser(final CourseParticipantRequest request) {
        final var courseParticipant = courseParticipantRepository.findByCourseUuidAndUserUuid(request.courseUuid(), request.userUuid())
                .orElseThrow(() -> new NotFoundException(String.format("Course participant for course: %s and user: %s not found", request.courseUuid(), request.userUuid())));
        courseParticipantRepository.delete(courseParticipant);
    }

    private void validateQueryAll(final Set<UUID> userUuids, final User requester) {
        if (!isUserAdmin(requester)) {
            userUuids.stream().findAny().ifPresent(userUuid -> {
                if(!userUuid.equals(requester.getUuid())) {
                    throw new ForbiddenException("You can only query multiple users if you are an admin or the user himself");
                }
            });
        }
    }

    private void validateIfCourseParticipantExists(final UUID courseUuid, final UUID userUuid) {
        if (courseParticipantRepository.existsByCourseUuidAndUserUuid(courseUuid, userUuid)) {
            throw new ConflictException(String.format("Course participant for course: %s and user: %s already exists", courseUuid, userUuid));
        }
    }
}
