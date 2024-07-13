package com.piotrpabich.talktactics.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.listeners.CourseEntityListeners;
import com.piotrpabich.talktactics.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "courses")
@EntityListeners(CourseEntityListeners.class)
public class Course extends CommonEntity {
    @NotBlank(message = "Cannot be blank")
    private String title;
    @Column(length = 800)
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @JsonIgnore
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CourseItem> courseItems;

    private int quantity = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserCourse> userCourses;

    public int getPoints() {
        return switch (this.level) {
            case BEGINNER -> Constants.BEGINNER_COMPLETED_POINTS;
            case INTERMEDIATE -> Constants.INTERMEDIATE_COMPLETED_POINTS;
            case ADVANCED -> Constants.ADVANCED_COMPLETED_POINTS;
        };
    }

    public void copy(Course source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
