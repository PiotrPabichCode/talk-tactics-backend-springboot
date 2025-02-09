package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.common.BaseMapper;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCourseMapper extends BaseMapper<UserCourseDto, UserCourse> {

}
