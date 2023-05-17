import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

export default function ViewUser() {
  const [user, setUser] = useState({
    login: "",
    password: "",
    admin: "",
  });

  const { id } = useParams();

  useEffect(() => {
    loadUser();
  }, []);

  const loadUser = async () => {
    const result = await axios.get(`http://localhost:8080/api/user/${id}`);
    setUser(result.data);
  };

  const url = "/admin?isUserDisplayed=true";

  return (
    <div className="container py-4 bg-secondary">
      <div className="row">
        <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow bg-dark position-relative">
          <Link
            className="btn btn-primary position-absolute end-0 me-4"
            to={url}>
            Back
          </Link>
          <h2 className="text-center m-4 text-light">User Details</h2>

          <div className="card bg-dark text-light">
            <div className="card-header table-dark">
              <h4>User ID: {user.id}</h4>
            </div>
            <div className="card-body">
              <table className="table table-bordered table-dark">
                <thead>
                  <tr>
                    <th>Login</th>
                    <th>Password</th>
                    <th>Admin</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>{user.login}</td>
                    <td>{user.password}</td>
                    <td>{user.admin === true ? "True" : "False"}</td>
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
