import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";

export default function Tasks() {
  const [tasks, setTasks] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    const result = await axios.get("http://localhost:8080/api/tasks");
    console.log(result.data);
    setTasks(result.data);
  };

  const deleteTask = async (id) => {
    await axios.delete(`http://localhost:8080/api/task/${id}`);
    loadTasks();
  };

  return (
    <div className="container">
      <div className="py-4">
        <h1 className="text-light">Tasks</h1>
        <div className="row border mx-0">
          <div className="col">
            <Link to={"/addtask"}>
              <button className="btn btn-primary my-3 w-100">
                Add new task
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

        <table className="table table-responsive border shadow text-light bg-dark">
          <thead>
            <tr>
              <th scope="col">ID</th>
              <th scope="col">Name</th>
              <th scope="col">Word</th>
              <th scope="col">Part of speech</th>
              <th scope="col">Description</th>
              <th scope="col">Course</th>
              <th scope="col">Action</th>
            </tr>
          </thead>
          <tbody>
            {tasks.map((task, index) => (
              <tr>
                <th scope="row" key={index}>
                  {index + 1}
                </th>
                <td>{task.name}</td>
                <td>{task.word}</td>
                <td>{task.partOfSpeech}</td>
                <td>{task.description}</td>
                <td>{task.course.name}</td>
                <td>
                  <Link
                    className="btn btn-primary mx-2"
                    to={`/viewtask/${task.id}`}>
                    View
                  </Link>
                  <Link
                    className="btn btn-outline-primary mx-2"
                    to={`/edittask/${task.id}`}>
                    Edit
                  </Link>
                  <button
                    className="btn btn-danger mx-2"
                    onClick={() => deleteTask(task.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
