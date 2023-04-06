import "./App.css";
import React from "react";
import Sidebar from "./components/Sidebar";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import Account from "./pages/Account";
import Faq from "./pages/Faq";
import Tasks from "./pages/Tasks";
import Assignment from "./pages/Assignment";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Sidebar />

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
