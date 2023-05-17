import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

export default function ViewUser() {
  const [course, setCourse] = useState({
    name: "",
    description: "",
    level: "",
  });

  const { id } = useParams();

  useEffect(() => {
    loadCourse();
  }, []);

  const loadCourse = async () => {
    const result = await axios.get(`http://localhost:8080/api/course/${id}`);
    setCourse(result.data);
  };

  const url = "/admin?isCoursesDisplayed=true";

  return (
    <div className="container bg-secondary py-4">
      <div className="row">
        <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow bg-dark position-relative">
          <Link
            className="btn btn-primary position-absolute end-0 me-4"
            to={url}>
            Back
          </Link>
          <h2 className="text-center m-4 text-light">Course Details</h2>

          <div className="card bg-dark text-light">
            <div className="card-header table-dark">
              <h4>Course ID: {course.id}</h4>
            </div>
            <div className="card-body">
              <table className="table table-bordered table-responsive table-dark">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>level</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>{course.name}</td>
                    <td>{course.description}</td>
                    <td>{course.level}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
