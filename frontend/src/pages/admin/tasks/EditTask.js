import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

export default function EditTask() {
  let navigate = useNavigate();

  const { id } = useParams();

  const [task, setTask] = useState({
    name: "",
    word: "",
    partOfSpeech: "",
    description: "",
    course: [],
  });

  const { name, word, partOfSpeech, description, course } = task;

  const onInputChange = (e) => {
    setTask({ ...task, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    loadTask();
  }, []);

  const onSubmit = async (e) => {
    e.preventDefault();
    await axios.put(`http://localhost:8080/api/task/${id}`, task);
    navigate(url);
  };

  const loadTask = async () => {
    const result = await axios.get(`http://localhost:8080/api/task/${id}`);
    setTask(result.data);
  };

  const url = "/admin?isTasksDisplayed=true";

  return (
    <div className="container">
      <div className="row text-light">
        <div className="col-md-6 offset-md-3 bg-dark opacity-100 border rounded p-4 mt-2 shadow position-relative">
          <Link
            className="btn btn-primary position-absolute end-0 me-4"
            to={url}>
            Back
          </Link>
          <h2 className="text-center m-4">Edit Task</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className="mb-3">
              <label htmlFor="Name" className="form-label">
                Name
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter task name"
                name="name"
                value={name}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Word" className="form-label">
                Word
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter word"
                name="word"
                value={word}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="PartOfSpeech" className="form-label">
                Part of speech
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Choose part of speech"
                name="partOfSpeech"
                value={partOfSpeech}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Description" className="form-label">
                Description
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter description"
                name="description"
                value={description}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Course" className="form-label">
                Course
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Choose course"
                name="course"
                value={course}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <button type="submit" className="btn btn-outline-primary">
              Submit
            </button>
            <Link className="btn btn-outline-danger mx-2" to={url}>
              Cancel
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
}
