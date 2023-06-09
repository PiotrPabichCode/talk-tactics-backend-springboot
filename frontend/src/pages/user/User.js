import React, { useEffect, useState } from 'react';
import { Link, useLocation, useParams } from 'react-router-dom';
import AccountDetails from './AccountDetails';
import UserCourses from './UserCourses';

export default function User() {
  const [isAccountDetailsDisplayed, setIsAccountDetailsDisplayed] =
    useState(false);
  const [isCoursesDisplayed, setIsCoursesDisplayed] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const isAccountDetailsDisplayed = Boolean(
      searchParams.get('isAccountDetailsDisplayed')
    );
    const isCoursesDisplayed = Boolean(searchParams.get('isCoursesDisplayed'));

    setIsAccountDetailsDisplayed(isAccountDetailsDisplayed === true);
    setIsCoursesDisplayed(isCoursesDisplayed === true);
  }, [location.search]);

  const clearDisplays = () => {
    setIsAccountDetailsDisplayed(false);
    setIsCoursesDisplayed(false);
  };

  const handleAccountDetailsDisplay = () => {
    clearDisplays();
    setIsAccountDetailsDisplayed(true);
  };

  const handleCoursesDisplay = () => {
    clearDisplays();
    setIsCoursesDisplayed(true);
  };

  return (
    <>
      {/* User panel */}
      <section className='container-fluid py-4'>
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
            onClick={handleCoursesDisplay}>
            Your courses
          </button>
          {isAccountDetailsDisplayed && <AccountDetails />}
          {isCoursesDisplayed && <UserCourses />}
        </div>
      </section>
    </>
  );
}
