import axios from "axios";
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function AddTask() {
  let navigate = useNavigate();

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

  const onSubmit = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:8080/api/task", task);
    // navigate("/");
  };

  const url = "/admin?isTasksDisplayed=true";

  return (
    <div className="container text-light">
      <div className="row">
        <div className="col-md-6 offset-md-3 bg-dark border rounded p-4 mt-2 shadow position-relative">
          <Link
            className="btn btn-primary position-absolute end-0 me-4"
            to={url}>
            Back
          </Link>
          <h2 className="text-center m-4">Add new task</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className="mb-3">
              <label htmlFor="Name" className="form-label">
                Name
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter name"
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
