import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";

export default function Answers() {
  const [answers, setAnswers] = useState([]);
  const [username, setUsername] = useState("");
  const [searchedAnswers, setSearchedAnswers] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadAnswers();
  }, []);

  useEffect(() => {
    if (username) {
      loadSearchedAnswers();
    } else {
      setSearchedAnswers([]);
    }
  }, [username]);

  const loadAnswers = async () => {
    try {
      const result = await axios.get("http://localhost:8080/api/answers");
      setAnswers(result.data);
    } catch (error) {
      console.log(error);
    }
  };

  const loadSearchedAnswers = async () => {
    try {
      const result = await axios.get(
        "http://localhost:8080/api/answers/username/" + username
      );
      setSearchedAnswers(result.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleInputChange = (event) => {
    setUsername(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    loadSearchedAnswers();
  };

  const renderAllAnswers = () => {
    return (
      <table className="table border shadow text-light bg-dark">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Content</th>
            <th scope="col">Finish Time</th>
            <th scope="col">Task</th>
            <th scope="col">Username</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {answers.map((answer, index) => (
            <tr>
              <th scope="row" key={index}>
                {index + 1}
              </th>
              <td>{answer.content}</td>
              <td>{answer.finishTime}</td>
              <td>{answer.task.name}</td>
              <td>{answer.user.login}</td>
              <td>
                <Link
                  className="btn btn-primary mx-2"
                  to={`/viewanswer/${answer.id}`}>
                  View
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  const renderSearchedAnswers = () => {
    return (
      <table className="table border shadow text-light bg-dark">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Content</th>
            <th scope="col">Finish Time</th>
            <th scope="col">Task</th>
            <th scope="col">Username</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {searchedAnswers.map((answer, index) => (
            <tr>
              <th scope="row" key={index}>
                {index + 1}
              </th>
              <td>{answer.content}</td>
              <td>{answer.finishTime}</td>
              <td>{answer.task.name}</td>
              <td>{answer.user.login}</td>
              <td>
                <Link
                  className="btn btn-primary mx-2"
                  to={`/viewanswer/${answer.id}`}>
                  View
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  return (
    <div className="container">
      <div className="py-4">
        <h1 className="text-light">Answers</h1>
        <form class="d-flex my-4" onSubmit={handleSubmit}>
          <input
            className="form-control me-1"
            type="search"
            value={username}
            onChange={handleInputChange}
            placeholder="Enter username"
            aria-label="Search"
          />
          <button className="btn btn-primary" type="submit">
            <SearchIcon />
          </button>
        </form>
        {username ? renderSearchedAnswers() : renderAllAnswers()}
      </div>
    </div>
  );
}
