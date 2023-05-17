import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";

export default function Users() {
  const [users, setUsers] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    const result = await axios.get("http://localhost:8080/api/users");
    console.log(result.data);
    setUsers(result.data);
  };

  return (
    <div className="container">
      <div className="py-4">
        <h1 className="text-light">Users</h1>
        <form class="d-flex my-4">
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
      </div>
    </div>
  );
}
