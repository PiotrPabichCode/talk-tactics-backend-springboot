package com.example.talktactics.service.user_course;

import com.example.talktactics.common.BaseMapper;
import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.entity.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCourseMapper extends BaseMapper<UserCourseDto, UserCourse> {

}
