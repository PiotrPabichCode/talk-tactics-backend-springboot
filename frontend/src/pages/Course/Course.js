import React, { useEffect, useState } from "react";
import "./Course.css";
import axios from "axios";

const Course = () => {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    loadCourses();
  }, []);

  const loadCourses = async () => {
    const result = await axios.get("http://localhost:8080/courses");
    setCourses(result.data);
  };

  return (
    <>
      <div className="course__wrapper">
        {courses.map((course, index) => (
          <div className="course__body">
            <p>{index + 1}.</p>
            <h3>
              <b>Nazwa kursu:</b> {course.name}
            </h3>
            <p className="course__line"></p>
            <p>
              <b>Poziom trudności:</b> {course.level}
            </p>
            <p>
              <b>Opis kursu:</b> {course.description}
            </p>
          </div>
        ))}
      </div>
    </>
  );
};

export default Course;
