import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

export default function EditUser() {
  let navigate = useNavigate();

  const { id } = useParams();

  const [user, setUser] = useState({
    login: "",
    password: "",
    admin: false,
  });

  const { login, password, admin } = user;

  const onInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    loadUser();
  }, []);

  const onSubmit = async (e) => {
    e.preventDefault();
    await axios.put(`http://localhost:8080/api/user/${id}`, user);
    navigate(url);
  };

  const loadUser = async () => {
    const result = await axios.get(`http://localhost:8080/api/user/${id}`);
    setUser(result.data);
  };

  const url = "/admin?isUserDisplayed=true";

  return (
    <div className="container">
      <div className="row text-light">
        <div className="col-md-6 offset-md-3 bg-dark opacity-100 border rounded p-4 mt-2 shadow position-relative">
          <Link
            className="btn btn-primary position-absolute end-0 me-4"
            to={url}>
            Back
          </Link>
          <h2 className="text-center m-4">Edit User</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className="mb-3">
              <label htmlFor="Login" className="form-label">
                Login
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter your login"
                name="login"
                value={login}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Password" className="form-label">
                Password
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter your password"
                name="password"
                value={password}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Admin" className="form-label">
                Admin
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Is admin"
                name="admin"
                value={admin}
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
