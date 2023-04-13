import "./App.css";
import React from "react";
import Sidebar from "./components/Sidebar/Sidebar";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./pages/Home/Home";
import Account from "./pages/Account/Account";
import Faq from "./pages/Faq/Faq";
import Tasks from "./pages/Tasks/Tasks";
import Assignment from "./pages/Assignment/Assignment";
import Navbar from "./components/Navbar/Navbar";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Navbar />
        {/* <Sidebar /> */}
        
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/account" element={<Account />} />
          <Route path="/assignment" element={<Assignment />} />
          <Route path="/faq" element={<Faq />} />
          <Route path="/tasks" element={<Tasks />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
