import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";

export default function Courses() {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    loadCourses();
  }, []);

  const loadCourses = async () => {
    const result = await axios.get("http://localhost:8080/api/courses");
    console.log(result.data);
    setCourses(result.data);
  };

  const deleteCourse = async (id) => {
    await axios.delete(`http://localhost:8080/api/course/${id}`);
    loadCourses();
  };

  return (
    <div className="container">
      <div class="py-4">
        <h1 className="text-light">Courses</h1>
        <div className="row border mx-0">
          <div className="col">
            <Link to={"/addcourse"}>
              <button className="btn btn-primary my-3 w-100">
                Add new course
              </button>
            </Link>
          </div>
          <div className="col">
            <form class="d-flex my-3">
              <input
                className="form-control me-1"
                type="search"
                placeholder="Search"
                aria-label="Search"
              />
              <button className="btn btn-primary" type="submit">
                <SearchIcon />
              </button>
            </form>
          </div>
        </div>
        <div className="card-body">
          <table className="table table-responsive table-dark border shadow text-light">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Level</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {courses.map((course, index) => (
                <tr key={course.id}>
                  <td>{index + 1}</td>
                  <td>{course.name}</td>
                  <td>{course.description}</td>
                  <td>{course.level}</td>
                  <td>
                    <Link
                      className="btn btn-primary mx-2"
                      to={`/viewcourse/${course.id}`}>
                      View
                    </Link>
                    <Link
                      className="btn btn-outline-primary mx-2"
                      to={`/editcourse/${course.id}`}>
                      Edit
                    </Link>
                    <button
                      className="btn btn-danger mx-2"
                      onClick={() => deleteCourse(course.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
