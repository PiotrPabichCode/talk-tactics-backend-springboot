package com.example.talktactics.services;

import com.example.talktactics.exceptions.CourseNotFoundException;
import com.example.talktactics.models.Course;
import com.example.talktactics.models.CourseItem;
import com.example.talktactics.models.User;
import com.example.talktactics.repositories.CourseItemRepository;
import com.example.talktactics.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseItemService {
    private final CourseItemRepository courseItemRepository;

    public List<CourseItem> getAll() {
        return courseItemRepository.findAll();
    }

    public List<CourseItem> getByCourseId(int id) {
        List<CourseItem> courseItems = courseItemRepository.findByCourseId(id);
        courseItems.sort(Comparator.comparingInt(CourseItem::getId));
        return courseItems;
    }

    public Optional<CourseItem> findById(Long id) {
        return courseItemRepository.findById(id);
    }

    public void deleteCourseItem(Long id) {
        if (!courseItemRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }
        courseItemRepository.deleteById(id);
    }

}
