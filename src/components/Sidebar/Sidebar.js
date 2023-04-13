import React from "react";
import "./Sidebar.css";
import { SidebarData } from "./SidebarData";
import LocalLibraryIcon from "@mui/icons-material/LocalLibrary";
import SwitchLeftIcon from "@mui/icons-material/SwitchLeft";
import SwitchRightIcon from "@mui/icons-material/SwitchRight";
import { Link } from "react-router-dom";
function Sidebar() {
  return (
    <>
      <div className="Sidebar">
        <Link to={"/"} style={{textDecoration: 'none'}}>
          <div className="logo">
            <h1>TalkTactics</h1>
          </div>
        </Link>
        <ul className="SidebarList">
          {SidebarData.map((val, key) => {
            return (
              <Link to={val.link}  style={{textDecoration: 'none'}}>
                <li
                  key={key}
                  className="row"
                  id={window.location.pathname == val.link ? "active" : ""}>
                  <div id="icon">{val.icon}</div>{" "}
                  <div id="title">{val.title}</div>
                </li>
              </Link>
            );
          })}
        </ul>
      </div>
    </>
  );
}


const pages = document.querySelectorAll(".row");

pages.forEach((item) => {
  item.addEventListener('click', active_item);
})

function active_item () {
  pages.forEach((item) => {
    item.classList.remove('active-link');
  });
  this.classList.add('active-link');
}


export default Sidebar;
