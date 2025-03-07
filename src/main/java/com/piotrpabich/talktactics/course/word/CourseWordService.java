package com.piotrpabich.talktactics.course.word;

import com.piotrpabich.talktactics.course.word.dto.CourseWordDto;
import com.piotrpabich.talktactics.course.word.dto.CourseWordQueryCriteria;
import com.piotrpabich.talktactics.course.word.dto.CourseWordPreview;
import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import com.piotrpabich.talktactics.exception.NotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;
import static com.piotrpabich.talktactics.common.QueryHelp.getPredicate;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseWordService {

    private final CourseWordRepository courseWordRepository;

    public Page<CourseWordPreview> queryAll(
            final CourseWordQueryCriteria criteria,
            final Pageable pageable
    ) {
        return courseWordRepository.findAll(getQueryCourseWordsSpecification(criteria), pageable)
                .map(CourseWordPreview::of);
    }

    public CourseWordDto getCourseWordByUuid(final UUID uuid) {
        return courseWordRepository.findByUuid(uuid)
                .map(CourseWordDto::of)
                .orElseThrow(() -> new NotFoundException(String.format("Course word with uuid: %s was not found", uuid)));
    }

    public void delete(final UUID uuid, final User requester) {
        validateIfUserAdmin(requester);
        if (!courseWordRepository.existsByUuid(uuid)) {
            throw new NotFoundException(String.format("Course word with uuid: %s was not found", uuid));
        }
        courseWordRepository.deleteByUuid(uuid);
    }

    private Specification<CourseWord> getQueryCourseWordsSpecification(
            final CourseWordQueryCriteria criteria
    ) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(
                    getPredicate(root, criteria, criteriaBuilder),
                    criteriaBuilder.equal(root.get("course").get("uuid"), criteria.getCourseUuid())
                );
    }

}
