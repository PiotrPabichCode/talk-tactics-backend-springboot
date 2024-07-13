package com.piotrpabich.talktactics.service.user_course;

import com.piotrpabich.talktactics.common.BaseMapper;
import com.piotrpabich.talktactics.dto.user_course.UserCourseDto;
import com.piotrpabich.talktactics.entity.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCourseMapper extends BaseMapper<UserCourseDto, UserCourse> {

}
