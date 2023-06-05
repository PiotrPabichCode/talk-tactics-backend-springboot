import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Users from './users/Users';
import Courses from './courses/Courses';
import Tasks from './tasks/Tasks';
import Answers from './answers/Answers';

export default function Admin() {
  const [isUsersDisplayed, setIsUsersDisplayed] = useState(false);
  const [isCoursesDisplayed, setIsCoursesDisplayed] = useState(false);
  const [isTasksDisplayed, setIsTasksDisplayed] = useState(false);
  const [isAnswersDisplayed, setIsAnswersDisplayed] = useState(false);

  const location = useLocation();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const isUsersDisplayedValue = searchParams.get('isUserDisplayed');
    const isCoursesDisplayedValue = searchParams.get('isCoursesDisplayed');
    const isTasksDisplayedValue = searchParams.get('isTasksDisplayed');
    const isAnswersDisplayedValue = searchParams.get('isAnswersDisplayed');

    setIsUsersDisplayed(isUsersDisplayedValue === 'true');
    setIsCoursesDisplayed(isCoursesDisplayedValue === 'true');
    setIsTasksDisplayed(isTasksDisplayedValue === 'true');
    setIsAnswersDisplayed(isAnswersDisplayedValue === 'true');
  }, [location.search]);

  const clearDisplays = () => {
    setIsUsersDisplayed(false);
    setIsCoursesDisplayed(false);
    setIsTasksDisplayed(false);
    setIsAnswersDisplayed(false);
  };

  const handleUsersDisplay = () => {
    clearDisplays();
    setIsUsersDisplayed(true);
  };

  const handleCoursesDisplay = () => {
    clearDisplays();
    setIsCoursesDisplayed(true);
  };

  const handleTasksDisplay = () => {
    clearDisplays();
    setIsTasksDisplayed(true);
  };

  const handleAnswersDisplay = () => {
    clearDisplays();
    setIsAnswersDisplayed(true);
  };

  return (
    <>
      {/* Admin panel */}
      <section className='container-fluid py-4 bg-secondary'>
        <div className='container-fluid rounded bg-dark py-3'>
          <h4 className='display-4 text-center text-light'>Admin panel</h4>
          <hr className='text-light'></hr>
          <br />
          <button
            className='btn btn-outline-light me-2'
            onClick={handleUsersDisplay}>
            Users
          </button>
          <button
            className='btn btn-outline-light me-2'
            onClick={handleCoursesDisplay}>
            Courses
          </button>
          <button
            className='btn btn-outline-light me-2'
            onClick={handleTasksDisplay}>
            Tasks
          </button>
          <button
            className='btn btn-outline-light'
            onClick={handleAnswersDisplay}>
            Answers
          </button>
          {isUsersDisplayed && <Users />}
          {isCoursesDisplayed && <Courses />}
          {isTasksDisplayed && <Tasks />}
          {isAnswersDisplayed && <Answers />}
        </div>
      </section>
    </>
  );
}
