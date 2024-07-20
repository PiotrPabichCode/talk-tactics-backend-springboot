package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.common.BaseMapper;
import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper extends BaseMapper<CourseDto, Course> {

}