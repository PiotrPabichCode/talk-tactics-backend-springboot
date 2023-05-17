import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useLocation, useParams } from "react-router-dom";

export default function User() {
  const [isSettingsDisplayed, setIsSettingsDisplayed] = useState(false);
  const [isAssignmentsDisplayed, setIsAssigmentsDisplayed] = useState(false);

  const location = useLocation();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const isSettingsDisplayedValue = searchParams.get("isSettingsDisplayed");
    const isAssigmentsDisplayedValue = searchParams.get(
      "isAssigmentsDisplayed"
    );

    setIsSettingsDisplayed(isSettingsDisplayedValue === "true");
    setIsAssigmentsDisplayed(isAssigmentsDisplayedValue === "true");
  }, [location.search]);

  const clearDisplays = () => {
    setIsSettingsDisplayed(false);
    setIsAssigmentsDisplayed(false);
  };

  const handleSettingsDisplay = () => {
    clearDisplays();
    setIsSettingsDisplayed(true);
  };

  const handleAssignmentsDisplay = () => {
    clearDisplays();
    setIsAssigmentsDisplayed(true);
  };

  return (
    <>
      {/* User panel */}
      <section className="container-fluid py-4 bg-secondary">
        <div className="container rounded bg-dark py-3">
          <h4 className="display-4 text-center text-light">User panel</h4>
          <hr className="text-light"></hr>
          <br />
          <button
            className="btn btn-outline-light me-2"
            onClick={handleSettingsDisplay}>
            Settings
          </button>
          <button
            className="btn btn-outline-light me-2"
            onClick={handleAssignmentsDisplay}>
            Your assigments
          </button>
        </div>
      </section>
    </>
  );
}
