import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

export default function ViewAnswer() {
  const [answer, setAnswer] = useState({
    content: "",
    finishTime: "",
    task: [],
    user: [],
  });
  const [course, setCourse] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadAnswer();
  }, []);

  const loadAnswer = async () => {
    const result = await axios.get(`http://localhost:8080/api/answer/${id}`);
    setAnswer(result.data);
    setCourse(result.data.task.course);
  };

  const url = "/admin?isAnswersDisplayed=true";

  return (
    <div className="container bg-secondary py-4">
      <div className="row">
        <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow bg-dark position-relative">
          <Link
            className="btn btn-primary position-absolute end-0 me-4"
            to={url}>
            Back
          </Link>
          <h2 className="text-center m-4 text-light">Answer Details</h2>

          <div className="card bg-dark text-light">
            <div className="card-header table-dark">
              <h4>Answer ID: {answer.id}</h4>
            </div>
            <div className="card-body">
              <table className="table table-bordered table-responsive table-dark">
                <thead>
                  <tr>
                    <th>Content</th>
                    <th>Finish time</th>
                    <th>Task</th>
                    <th>Course</th>
                    <th>User name</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>{answer.content}</td>
                    <td>{answer.finishTime}</td>
                    <td>{answer.task.name}</td>
                    <td>{course.name}</td>
                    <td>{answer.user.login}</td>
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
