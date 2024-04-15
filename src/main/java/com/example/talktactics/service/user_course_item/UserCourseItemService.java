package com.example.talktactics.service.user_course_item;

import com.example.talktactics.dto.user_course_item.req.GetUserCourseItemsPreviewDtoReq;
import com.example.talktactics.dto.user_course_item.res.GetUserCourseItemPreviewDtoResponse;
import com.example.talktactics.dto.user_course_item.res.LearnUserCourseItemDtoResponse;
import com.example.talktactics.entity.UserCourseItem;

public interface UserCourseItemService {
    UserCourseItem getById(Long id);
    LearnUserCourseItemDtoResponse updateIsLearned(Long id);
    GetUserCourseItemPreviewDtoResponse getUserCourseItemPreviewDtoResponse(GetUserCourseItemsPreviewDtoReq req);
}
