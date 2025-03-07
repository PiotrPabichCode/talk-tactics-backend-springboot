package com.piotrpabich.talktactics.course.participant.word;

import com.piotrpabich.talktactics.exception.ConflictException;
import com.piotrpabich.talktactics.exception.NotFoundException;
import com.piotrpabich.talktactics.course.participant.word.dto.CourseParticipantWordQueryCriteria;
import com.piotrpabich.talktactics.course.participant.word.dto.CourseParticipantWordDto;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.course.participant.word.entity.CourseParticipantWord;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;
import static com.piotrpabich.talktactics.common.QueryHelp.getPredicate;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseParticipantWordService {

    private final CourseParticipantWordRepository courseParticipantWordRepository;

    Page<CourseParticipantWordDto> queryAll(
            final CourseParticipantWordQueryCriteria criteria,
            final Pageable pageable
    ) {
        return courseParticipantWordRepository.findAll(getQueryAllSpecification(criteria), pageable)
                .map(CourseParticipantWordDto::of);
    }

    void learnCourseParticipantWord(final UUID uuid, final User requester) {
        final var courseParticipantWord = getCourseParticipantWordByUuid(uuid);
        final var user = courseParticipantWord.getCourseParticipant().getUser();
        validateIfUserHimselfOrAdmin(requester, user);

        if (courseParticipantWord.isLearned()) {
            throw new ConflictException(String.format("Course participant word with uuid: %s already learned", uuid));
        }

        courseParticipantWord.setLearned(true);
        courseParticipantWordRepository.save(courseParticipantWord);
    }

    private Specification<CourseParticipantWord> getQueryAllSpecification(
            final CourseParticipantWordQueryCriteria criteria
    ) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final var userCourseRoot = root.get("userCourse");
            return criteriaBuilder.and(
                    getPredicate(root, criteria, criteriaBuilder),
                    criteriaBuilder.equal(userCourseRoot.get("course").get("uuid"), criteria.getCourseUuid()),
                    criteriaBuilder.equal(userCourseRoot.get("user").get("uuid"), criteria.getUserUuid())
            );
        };
    }

    private CourseParticipantWord getCourseParticipantWordByUuid(final UUID uuid) {
        return courseParticipantWordRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException(String.format("Course participant word with uuid %s not found", uuid)));
    }
}
