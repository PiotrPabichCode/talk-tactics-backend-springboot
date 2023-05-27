import React, { useEffect, useState } from 'react';
import { Link, useLocation, useParams } from 'react-router-dom';
import AccountDetails from './AccountDetails';
import UserSettings from './UserSettings';

export default function User() {
  const [isAccountDetailsDisplayedValue, setIsAccountDetailsDisplayedValue] =
    useState(false);
  const [isSettingsDisplayed, setIsSettingsDisplayed] = useState(false);
  const [isAssignmentsDisplayed, setIsAssigmentsDisplayed] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const isAccountDetailsDisplayedValue = Boolean(
      searchParams.get('isAccountDetailsDisplayedValue')
    );
    const isSettingsDisplayedValue = Boolean(
      searchParams.get('isSettingsDisplayed')
    );
    const isAssigmentsDisplayedValue = Boolean(
      searchParams.get('isAssigmentsDisplayed')
    );

    setIsAccountDetailsDisplayedValue(isAccountDetailsDisplayedValue === true);
    setIsSettingsDisplayed(isSettingsDisplayedValue === true);
    setIsAssigmentsDisplayed(isAssigmentsDisplayedValue === true);
  }, [location.search]);

  const clearDisplays = () => {
    setIsAccountDetailsDisplayedValue(false);
    setIsSettingsDisplayed(false);
    setIsAssigmentsDisplayed(false);
  };

  const handleAccountDetailsDisplay = () => {
    clearDisplays();
    setIsAccountDetailsDisplayedValue(true);
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
      <section className='container-fluid py-4 bg-secondary'>
        <div className='container rounded bg-dark py-3'>
          <h4 className='display-4 text-center text-light'>User panel</h4>
          <hr className='text-light'></hr>
          <br />
          <button
            className='btn btn-outline-light me-2'
            onClick={handleAccountDetailsDisplay}>
            Account details
          </button>
          <button
            className='btn btn-outline-light me-2'
            onClick={handleSettingsDisplay}>
            Settings
          </button>
          <button
            className='btn btn-outline-light me-2'
            onClick={handleAssignmentsDisplay}>
            Your assigments
          </button>
          {isAccountDetailsDisplayedValue && <AccountDetails />}
          {isSettingsDisplayed && <UserSettings />}
        </div>
      </section>
    </>
  );
}
