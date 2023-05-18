import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";

export default function Users() {
  const [users, setUsers] = useState([]);
  const [userName, setUserName] = useState("");
  const [searchedUsers, setSearchedUsers] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadUsers();
  }, []);

  useEffect(() => {
    if (userName) {
      loadSearchedUsers();
    } else {
      setSearchedUsers([]);
    }
  }, [userName]);

  const loadUsers = async () => {
    const result = await axios.get("http://localhost:8080/api/users");
    console.log(result.data);
    setUsers(result.data);
  };

  const loadSearchedUsers = async () => {
    try {
      const result = await axios.get(
        "http://localhost:8080/api/users/login/" + userName
      );
      setSearchedUsers(result.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleInputChange = (event) => {
    setUserName(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    loadSearchedUsers();
  };

  const renderAllUsers = () => {
    return (
      <table className="table border shadow text-light bg-dark">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Login</th>
            <th scope="col">Password</th>
            <th scope="col">Admin</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user, index) => (
            <tr>
              <th scope="row" key={index}>
                {index + 1}
              </th>
              <td>{user.login}</td>
              <td>{user.password}</td>
              <td>{user.admin === true ? "True" : "False"}</td>
              <td>
                <Link
                  className="btn btn-primary mx-2"
                  to={`/viewuser/${user.id}`}>
                  View
                </Link>
                <Link
                  className="btn btn-outline-primary mx-2"
                  to={`/edituser/${user.id}`}>
                  Edit
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  const renderSearchedUsers = () => {
    return (
      <table className="table border shadow text-light bg-dark">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Login</th>
            <th scope="col">Password</th>
            <th scope="col">Admin</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {searchedUsers.map((user, index) => (
            <tr>
              <th scope="row" key={index}>
                {index + 1}
              </th>
              <td>{user.login}</td>
              <td>{user.password}</td>
              <td>{user.admin === true ? "True" : "False"}</td>
              <td>
                <Link
                  className="btn btn-primary mx-2"
                  to={`/viewuser/${user.id}`}>
                  View
                </Link>
                <Link
                  className="btn btn-outline-primary mx-2"
                  to={`/edituser/${user.id}`}>
                  Edit
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
        <h1 className="text-light">Users</h1>
        <form class="d-flex my-4" onSubmit={handleSubmit}>
          <input
            className="form-control me-1"
            type="search"
            value={userName}
            onChange={handleInputChange}
            placeholder="Search"
            aria-label="Search"
          />
          <button className="btn btn-primary" type="submit">
            <SearchIcon />
          </button>
        </form>
        {userName ? renderSearchedUsers() : renderAllUsers()}
      </div>
    </div>
  );
}
