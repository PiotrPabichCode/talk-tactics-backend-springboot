package com.example.talktactics.specification.course;

import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseLevel;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class CourseSpecification {
    public static Specification<Course> courseTitleContains(String title) {
        if(title == null || title.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Course> courseDescriptionContains(String description) {
        if(description == null || description.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Course> courseLevelIn(Set<CourseLevel> levels) {
        if(levels == null || levels.isEmpty()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> root.get("level").in(levels);
    }

    public static Specification<Course> courseQuantityBetween(Integer minQuantity, Integer maxQuantity) {
        if(minQuantity == null && maxQuantity == null) {
            return null;
        }
        validateQuantity(minQuantity, maxQuantity);

        return (root, query, criteriaBuilder) -> {
            if(minQuantity != null && maxQuantity != null) {
                return criteriaBuilder.between(root.get("quantity"), minQuantity, maxQuantity);
            } else if(minQuantity != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), minQuantity);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), maxQuantity);
            }
        };
    }

    // PRIVATE

    private static void validateQuantity(Integer minQuantity, Integer maxQuantity) {
        if(minQuantity != null && minQuantity < 0) {
            throw new IllegalArgumentException("Min quantity must be greater than or equal to 0");
        }
        if(maxQuantity != null && maxQuantity < 0) {
            throw new IllegalArgumentException("Max quantity must be greater than or equal to 0");
        }
        if(minQuantity != null && maxQuantity != null && minQuantity > maxQuantity) {
            throw new IllegalArgumentException("Min quantity must be less than or equal to max quantity");
        }
    }
}
