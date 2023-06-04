package com.example.talktactics.services;

import com.example.talktactics.exceptions.CourseNotFoundException;
import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Course;
import com.example.talktactics.models.Task;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.CourseRepository;
import com.example.talktactics.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;
    private final AnswerRepository answerRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }
    public List<Course> filterCoursesByLevelName(String levelName) {
        return courseRepository.findByLevelName(levelName);
    }
    public Course updateCourse(Long id, Course newCourse) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setDescription(newCourse.getDescription());
                    course.setLevel(newCourse.getLevel());
                    course.setName(newCourse.getName());
                    return courseRepository.save(course);
                }).orElseThrow(() -> new CourseNotFoundException(id));
    }
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }

        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));

        List<Task> tasks = taskRepository.findByCourse(course);
        List<Answer> answers = answerRepository.findByTaskIn(tasks);
        answerRepository.deleteAll(answers);
        taskRepository.deleteAll(tasks);
        courseRepository.deleteById(id);
    }

}
