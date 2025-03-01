package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.course_item.dto.CourseItemPreview;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CourseItemService {
    Page<CourseItemPreview> queryAll(CourseItemQueryCriteria criteria, Pageable pageable);
    CourseItemDto getCourseItemByUuid(UUID uuid);
    void delete(UUID uuid, User requester);
}
