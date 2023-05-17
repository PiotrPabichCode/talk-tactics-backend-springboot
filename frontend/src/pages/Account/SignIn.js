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
      <div className="pt-4 justify-content-center align-items-center">
        <div className="container">
          <div className="row d-flex justify-content-center">
            <div className="col-12 col-md-8 col-lg-6">
              <div className="card bg-white shadow-lg">
                <div className="card-body p-5">
                  <form className="mb-3 mt-md-4">
                    <h2 className="fw-bold mb-2 text-uppercase ">
                      TalkTactics
                    </h2>
                    <div className="mb-3">
                      <label for="login" className="form-label ">
                        Login
                      </label>
                      <input
                        type="text"
                        className="form-control"
                        id="login"
                        placeholder="Username"
                      />
                    </div>
                    <div className="mb-3">
                      <label for="password" className="form-label ">
                        Password
                      </label>
                      <input
                        type="password"
                        className="form-control"
                        id="password"
                        placeholder="*******"
                      />
                    </div>
                    <div className="d-grid">
                      <button className="btn btn-outline-dark" type="submit">
                        Sign in
                      </button>
                    </div>
                  </form>
                  <div>
                    <p className="mb-0  text-center">
                      Don't have an account?{" "}
                      <Link to="/register" className="text-primary fw-bold">
                        Sign up
                      </Link>
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default SignIn;
