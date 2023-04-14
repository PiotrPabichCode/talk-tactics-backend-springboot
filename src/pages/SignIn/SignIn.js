import React from "react";
import "./SignIn.css";
import { FaFacebook, FaTwitter, FaGoogle } from "react-icons/fa";
import { Link } from "react-router-dom";

function SignIn() {
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
        <button
            type="submit"
            className="input-style"
          >
            Sign in
          </button>
        </div>

        <p style={{color: "white", fontSize:"20px"}}>Or sign in by using</p>
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
        <Link to={"/register"} className="register-text register-link"> Sign up </Link>
        </div>
        <br />
    </div>
    </div>
    </>
  );
}

export default SignIn;
