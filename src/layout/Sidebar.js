import React from "react";
import "../App.css";
import { SidebarData } from "./SidebarData";
import LocalLibraryIcon from "@mui/icons-material/LocalLibrary";
import SwitchLeftIcon from "@mui/icons-material/SwitchLeft";
import SwitchRightIcon from "@mui/icons-material/SwitchRight";

function Sidebar() {
  return (
    <>
      <div className="Sidebar">
        <div className="toggle-sidebar">
          <SwitchRightIcon />
        </div>
        <div className="logo">
          <LocalLibraryIcon></LocalLibraryIcon>
          <h1
            onClick={() => {
              window.location.pathname = "/";
            }}>
            TalkTactics
          </h1>
        </div>
        <ul className="SidebarList">
          {SidebarData.map((val, key) => {
            return (
              <li
                key={key}
                className="row"
                id={window.location.pathname == val.link ? "active" : ""}
                onClick={() => {
                  window.location.pathname = val.link;
                }}>
                <div id="icon">{val.icon}</div>{" "}
                <div id="title">{val.title}</div>
              </li>
            );
          })}
        </ul>
      </div>
    </>
  );
}

export default Sidebar;
