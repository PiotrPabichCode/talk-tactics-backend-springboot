import React from "react";
import "../App.css";
import { SidebarData } from "./SidebarData";
import LocalLibraryIcon from "@mui/icons-material/LocalLibrary";
import SwitchLeftIcon from "@mui/icons-material/SwitchLeft";
import SwitchRightIcon from "@mui/icons-material/SwitchRight";
import { Link } from "react-router-dom";

function Sidebar() {
  return (
    <>
      <div className="Sidebar">
        <Link to={"/"}>
          <div className="logo">
            <h1>TalkTactics</h1>
          </div>
        </Link>
        <ul className="SidebarList">
          {SidebarData.map((val, key) => {
            return (
              <Link to={val.link}>
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

export default Sidebar;
