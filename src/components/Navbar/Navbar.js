import React from "react";
import { Link } from "react-router-dom";
import NavbarData from "./NavbarData";
import "./Navbar.css"
import KeyboardDoubleArrowRightIcon from '@mui/icons-material/KeyboardDoubleArrowRight';

function Navbar() {
  return (
    <>
      <nav className="navbar">
        <ul className="navbar-nav">
          <li className="logo">
            <Link to="/"  className="nav-link">
              <span className="link-text">TalkTactics</span>
              <KeyboardDoubleArrowRightIcon />
              </Link>
          </li>
        {NavbarData.map((val) => {
            return (
              <li className="nav-item">
                <Link to={val.link}  className="nav-link">
                  <div id="icon">{val.icon}</div>
                  <span className="link-text">{val.title}</span>
                  </Link>
              </li>
            );
          })}
        </ul>
      </nav>
    </>
  );
}

export default Navbar;
