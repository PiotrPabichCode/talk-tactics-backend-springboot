import React from "react";
import "./Account.css";
import Person2OutlinedIcon from "@mui/icons-material/Person2Outlined";

const Account = () => {
  return (
    <div className="account__wrapper">
      <div className="account_name_inline">
        <Person2OutlinedIcon className="person__icon" />
        <p>Piotr Pabich</p>
      </div>
      <p>Piotr Pabich</p>
      <div className="account_options_inline">
        <button>Your tasks</button>
        <button>Account settings</button>
      </div>
    </div>
  );
};

export default Account;
