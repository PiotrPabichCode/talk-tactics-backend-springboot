import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

export default function ViewTask() {
  const [task, setTask] = useState({
    name: "",
    word: "",
    partOfSpeech: "",
    description: "",
    course: [],
  });

  const { id } = useParams();

  useEffect(() => {
    loadTask();
  }, []);

  const loadTask = async () => {
    const result = await axios.get(`http://localhost:8080/api/task/${id}`);
    setTask(result.data);
  };

  const url = "/admin?isTasksDisplayed=true";

  return (
    <div className="container bg-secondary py-4">
      <div className="row">
        <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow bg-dark position-relative">
          <Link
            className="btn btn-primary position-absolute end-0 me-4"
            to={url}>
            Back
          </Link>
          <h2 className="text-center m-4 text-light">Task Details</h2>

          <div className="card bg-dark text-light">
            <div className="card-header table-dark">
              <h4>Task ID: {task.id}</h4>
            </div>
            <div className="card-body">
              <table className="table table-bordered table-responsive table-dark">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Word</th>
                    <th>Part of speech</th>
                    <th>Description</th>
                    <th>Course</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>{task.name}</td>
                    <td>{task.word}</td>
                    <td>{task.partOfSpeech}</td>
                    <td>{task.description}</td>
                    <td>{task.course.name}</td>
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
