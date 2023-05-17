// import React from "react";
// import { Link } from "react-router-dom";
// import NavbarData from "./NavbarData";
// import "./Navbar.css";
// import KeyboardDoubleArrowRightIcon from "@mui/icons-material/KeyboardDoubleArrowRight";

// function Navbar() {
//   return (
//     <>
//       <div className="navbar">
//         <ul className="navbar-nav">
//           <li className="logo">
//             <Link to="/" className="nav-link">
//               TalkTactics
//             </Link>
//           </li>
//           {NavbarData.map((val) => {
//             return (
//               <li className="nav-item">
//                 <Link to={val.link} className="nav-link">
//                   <div id="icon">{val.icon}</div>
//                   <span className="link-text">{val.title}</span>
//                 </Link>
//               </li>
//             );
//           })}
//         </ul>
//       </div>
//     </>
//   );
// }

// export default Navbar;

import React from "react";
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <div>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container-fluid">
          <Link className="navbar-brand" to="/">
            TalkTactics
          </Link>
          <ul className="navbar-nav mr-auto">
            <li className="nav-item me-1">
              <Link className="btn btn-outline-light" to="/login">
                Login
              </Link>
            </li>
            <li className="nav-item me-1">
              <Link className="btn btn-outline-light" to="/register">
                Register
              </Link>
            </li>
            <li className="nav-item me-1">
              <Link className="btn btn-outline-light" to="/user">
                User panel
              </Link>
            </li>
            <li className="nav-item">
              <Link className="btn btn-outline-light" to="/admin">
                Admin panel
              </Link>
            </li>
          </ul>
          {/* <Link className="btn btn-outline-light" to="/adduser">
            Add User
          </Link> */}
        </div>
      </nav>
    </div>
  );
}
