import React, { useState } from "react";
import axios from "axios";
import "./LoginRegister.css";
import { FaFacebook, FaTwitter, FaGoogle } from "react-icons/fa";
import { Link } from "react-router-dom";

const SignIn = (e) => {
  const [errorMessage, setErrorMessage] = useState("");

  const [user, setUser] = useState({
    login: "",
    password: "",
  });

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleLoginForm = async (e) => {
    e.preventDefault();
    setErrorMessage("");
    try {
      const response = await axios.post(
        "http://localhost:8080/api/users/login",
        user
      );
      console.log(response.data);
      setUser({ login: "", password: "" });
    } catch (error) {
      console.log(errorMessage);
      console.error(error.response.data);
    }
  };

  return (
    <>
      <div className="wrapper">
        <form onSubmit={handleLoginForm}>
          <div className="mycontainer">
            <img className="nav-logo" src="\logo192.png" alt="logo" />
            <div className="nav-item-text">Talk Tactics</div>
            <div className="input-container">
              <input
                type="text"
                name="login"
                value={user.login}
                onChange={handleChange}
                placeholder="Login"
                className="input-style"
                required
              />
            </div>
            <div className="input-container">
              <input
                type="password"
                name="password"
                value={user.password}
                onChange={handleChange}
                placeholder="Password"
                className="input-style"
                required
              />
            </div>
            <div className="input-container">
              <button type="submit" className="input-style">
                Sign in
              </button>
            </div>

            <p style={{ color: "white", fontSize: "20px" }}>
              Or sign in by using
            </p>
            <div className="icons-register">
              <a href="#">
                <FaGoogle className="icon-register" />
              </a>
              <a href="#">
                <FaFacebook className="icon-register" />
              </a>
              <a href="#">
                <FaTwitter className="icon-register" />
              </a>
            </div>
            <div className="links">
              <p>Don't have an account?</p>
              <Link to={"/register"} className="register-text register-link">
                {" "}
                Sign up{" "}
              </Link>
            </div>
            <br />
          </div>
        </form>
      </div>
    </>
  );
};

export default SignIn;
