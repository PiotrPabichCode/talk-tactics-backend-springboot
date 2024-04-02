package com.example.talktactics.service.course;

import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.exception.CourseRuntimeException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.*;
import com.example.talktactics.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

//  PUBLIC
    public Course create(Course course) throws CourseRuntimeException {
        return courseRepository.save(course);
    }
    public Course getById(Long id) throws CourseRuntimeException {
        return courseRepository.findById(id).orElseThrow(() -> new CourseRuntimeException(Constants.COURSE_NOT_FOUND_EXCEPTION));
    }
    public List<CoursePreviewProjection> getPreviewList() throws CourseRuntimeException {
        return courseRepository.findCoursePreviews();
    }
    public Course update(Long id, Course newCourse) throws CourseRuntimeException {
        Course course = getById(id);

        course.setDescription(newCourse.getDescription());
        course.setLevel(newCourse.getLevel());
        course.setTitle(newCourse.getTitle());

        return course;
    }
    public void delete(Long id) throws CourseRuntimeException {
        if (!courseRepository.existsById(id)) {
            throw new CourseRuntimeException(Constants.COURSE_NOT_FOUND_EXCEPTION);
        }
        courseRepository.deleteById(id);
    }
    public List<Course> filterByLevel(String level) throws CourseRuntimeException {
        return courseRepository.findByLevelName(level);
    }

//  PRIVATE
}
