import React, { useState } from "react";
import axios from "axios";
import "./LoginRegister.css";
import { FaFacebook, FaTwitter, FaGoogle } from "react-icons/fa";
import { Link } from "react-router-dom";

const SignUp = () => {
  const [errorMessage, setErrorMessage] = useState("");

  const [user, setUser] = useState({
    login: "",
    password: "",
    repeatPassword: "",
  });

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleRegisterForm = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    try {
      console.log(user);
      const response = await axios.post(
        "http://localhost:8080/api/users/register",
        user
      );
      console.log(response.data);
      setUser({ login: "", password: "", repeatPassword: "" });
    } catch (error) {
      if (error.response && error.response.status === 409) {
        setErrorMessage(error.response.data);
      } else {
        setErrorMessage("An error occurred. Please try again.");
      }
    }
    console.log(errorMessage);
  };

  return (
    <>
      <div className="wrapper">
        <form onSubmit={handleRegisterForm}>
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
              <input
                type="password"
                name="repeatPassword"
                value={user.repeatPassword}
                onChange={handleChange}
                placeholder="Repeat password"
                className="input-style"
                required
              />
            </div>
            <div className="input-container">
              <button type="submit" className="input-style">
                Sign up
              </button>
            </div>

            <p style={{ color: "white", fontSize: "20px" }}>
              Or Sign in by using
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
              <p>Do you have an account?</p>
              <Link to={"/login"} className="register-text register-link">
                {" "}
                Sign in{" "}
              </Link>
            </div>
            <br />
          </div>
        </form>
      </div>
    </>
  );
};

export default SignUp;
