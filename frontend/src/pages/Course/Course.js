import React, { useEffect, useState } from "react";
import "./Course.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Course = () => {
  const [courses, setCourses] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [selectedCourseId, setSelectedCourseId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    loadCourses();
  }, []);

  const loadCourses = async () => {
    const result = await axios.get("http://localhost:8080/courses");
    setCourses(result.data);
  };

  const loadCourseTasks = async (id) => {
    const result = await axios.get(`http://localhost:8080/courses/${id}`);
    setTasks(result.data);
  };

  const handleCourseClick = (id) => {
    setSelectedCourseId(id);
    loadCourseTasks(id);
    // navigate("/courses/${id}");
  };

  const handleCoursesReturn = () => {
    setSelectedCourseId(null);
  };

  const displayTasks = () => {
    return (
      <div className="course__wrapper">
        <button
          className="task__back_button"
          onClick={() => handleCoursesReturn()}>
          Powrót
        </button>
        {tasks.map((task, index) => (
          <div className="course__body" key={index}>
            <p>{index + 1}.</p>
            <h3>
              <b>Name: </b> {task.name}
            </h3>
            <p className="course__line"></p>
            <p>
              <b>Word: </b> {task.word}
            </p>
            <p>
              <b>Part of speech: </b> {task.partOfSpeech}
            </p>
            <p>
              <b>Description: </b> {task.description}
            </p>
          </div>
        ))}
      </div>
    );
  };

  const displayCourses = () => {
    return (
      <>
        <div className="course__wrapper">
          {courses.map((course, index) => (
            <div
              className="course__body course__pointer"
              key={index}
              onClick={() => handleCourseClick(course.id)}>
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

  return <>{!selectedCourseId ? displayCourses() : displayTasks()}</>;
};

export default Course;
