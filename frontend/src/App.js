import "./App.css";
import React from "react";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Home from "./pages/Home/Home";
import Assignment from "./pages/Assignment/Assignment";
import Navbar from "./components/Navbar/Navbar";
import SignIn from "./pages/Account/SignIn";
import SignUp from "./pages/Account/SignUp";
import Admin from "./pages/admin/Admin";
import AddUser from "./pages/admin/users/AddUser";
import EditUser from "./pages/admin/users/EditUser";
import ViewUser from "./pages/admin/users/ViewUser";
import EditCourse from "./pages/admin/courses/EditCourse";
import ViewCourse from "./pages/admin/courses/ViewCourse";
import EditTask from "./pages/admin/tasks/EditTask";
import ViewTask from "./pages/admin/tasks/ViewTask";
import ViewAnswer from "./pages/admin/answers/ViewAnswer";
import User from "./pages/user/User";
import AddTask from "./pages/admin/tasks/AddTask";
import AddCourse from "./pages/admin/courses/AddCourse";
function App() {
  return (
    <div className="App bg-secondary">
      <BrowserRouter>
        <Navbar />

        <Routes>
          <Route exact path="/" element={<Home />} />
          <Route exact path="/login" element={<SignIn />} />
          <Route exact path="/register" element={<SignUp />} />
          <Route exact path="/assignment" element={<Assignment />} />

          <Route exact path="/user" element={<User />} />
          <Route exact path="/admin" element={<Admin />} />
          <Route exact path="/viewanswer/:id" element={<ViewAnswer />} />
          <Route exact path="/addcourse" element={<AddCourse />} />
          <Route exact path="/editcourse/:id" element={<EditCourse />} />
          <Route exact path="/viewcourse/:id" element={<ViewCourse />} />
          <Route exact path="/addtask" element={<AddTask />} />
          <Route exact path="/edittask/:id" element={<EditTask />} />
          <Route exact path="/viewtask/:id" element={<ViewTask />} />
          <Route exact path="/adduser" element={<AddUser />} />
          <Route exact path="/edituser/:id" element={<EditUser />} />
          <Route exact path="/viewuser/:id" element={<ViewUser />} />
          <Route exact path="*" element={<Navigate to="/" />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
