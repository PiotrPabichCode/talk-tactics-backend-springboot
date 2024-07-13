package com.piotrpabich.talktactics.service.course;

import com.piotrpabich.talktactics.common.BaseMapper;
import com.piotrpabich.talktactics.dto.course.CourseDto;
import com.piotrpabich.talktactics.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper extends BaseMapper<CourseDto, Course> {

}