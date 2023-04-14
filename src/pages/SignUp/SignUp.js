import React from "react";
import "./SignUp.css";
import { FaFacebook, FaTwitter, FaGoogle } from "react-icons/fa";
import { Link } from "react-router-dom";

function SignUp() {
  return (
    <>
    <div className="wrapper">
        <div className="mycontainer">
        <img className="nav-logo" src="\logo192.png" alt="logo" />
        <div className="nav-item-text">Talk Tactics</div>
        <div className="input-container">
          <input
              type="text"
              placeholder="Username"
              className="input-style"
              id="usernameID"
            />
        </div>
        <div className="input-container">
          <input
              type="password"
              placeholder="Password"
              className="input-style"
              id="passwordID"
            />
        </div>
        <div className="input-container">
          <input
              type="password"
              placeholder="Repeat password"
              className="input-style"
              id="repeatPasswordID"
            />
        </div>
        <div className="input-container">
        <button
            type="submit"
            className="input-style"
          >
            Sign up
          </button>
        </div>

        <p style={{color: "white", fontSize:"20px"}}>Or Sign in by using</p>
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
        <Link to={"/login"} className="register-text register-link"> Sign in </Link>
        </div>
        <br />
    </div>
    </div>
    </>
  );
}

export default SignUp;
