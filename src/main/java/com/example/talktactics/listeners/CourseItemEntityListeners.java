package com.example.talktactics.listeners;

import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.repository.CourseRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
public class CourseItemEntityListeners {

    @Autowired
    private ObjectFactory<CourseRepository> courseRepositoryProvider;

    @PrePersist
    public void beforeSave(CourseItem courseItem) {
        if(courseItem.getLevel() == null) {
            courseItem.setLevel(courseItem.getCourse().getLevel());
        }
    }

//    @PostPersist
//    @Transactional
//    public void afterSave(CourseItem courseItem) {
//        Course course = courseItem.getCourse();
//        course.setQuantity(course.getQuantity() + 1);
//        CourseRepository courseRepository = this.courseRepositoryProvider.getObject();
//        try {
//
//        courseRepository.save(course);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
