package com.example.talktactics.service.course;

import com.example.talktactics.common.BaseMapper;
import com.example.talktactics.dto.course.CourseDto;
import com.example.talktactics.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper extends BaseMapper<CourseDto, Course> {

}