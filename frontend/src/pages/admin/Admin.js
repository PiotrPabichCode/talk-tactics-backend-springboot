import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Users from './users/Users';
import Courses from './courses/Courses';
import UserCourses from './user_courses/UserCourses';
import CourseItems from 'pages/courses/CourseItems';
import { useTranslation } from 'react-i18next';

export default function Admin() {
  const [isUsersDisplayed, setIsUsersDisplayed] = useState(false);
  const [isCoursesDisplayed, setIsCoursesDisplayed] = useState(false);
  const [isCourseItemsDisplayed, setIsCourseItemsDisplayed] = useState(false);
  const [isUserCoursesDisplayed, setIsUserCoursesDisplayed] = useState(false);

  const location = useLocation();
  const { t } = useTranslation();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const isUsersDisplayedValue = searchParams.get('isUserDisplayed');
    const isCoursesDisplayedValue = searchParams.get('isCoursesDisplayed');
    const isCourseItemsDisplayedValue = searchParams.get(
      'isCourseItemsDisplayed'
    );
    const isUserCoursesDisplayedValue = searchParams.get(
      'isUserCoursesDisplayed'
    );

    setIsUsersDisplayed(isUsersDisplayedValue === 'true');
    setIsCoursesDisplayed(isCoursesDisplayedValue === 'true');
    setIsCourseItemsDisplayed(isCourseItemsDisplayedValue === 'true');
    setIsUserCoursesDisplayed(isUserCoursesDisplayedValue === 'true');
  }, [location.search]);

  const clearDisplays = () => {
    setIsUsersDisplayed(false);
    setIsCoursesDisplayed(false);
    setIsCourseItemsDisplayed(false);
    setIsUserCoursesDisplayed(false);
  };

  const handleUsersDisplay = () => {
    clearDisplays();
    setIsUsersDisplayed(true);
  };

  const handleCoursesDisplay = () => {
    clearDisplays();
    setIsCoursesDisplayed(true);
  };

  const handleCourseItemsDisplay = () => {
    clearDisplays();
    setIsCourseItemsDisplayed(true);
  };

  const handleUserCoursesDisplay = () => {
    clearDisplays();
    setIsUserCoursesDisplayed(true);
  };

  return (
    <>
      {/* Admin panel */}
      <section className='container-fluid p-4'>
        <div className='container-fluid rounded bg-dark py-3'>
          <h4 className='display-4 text-center text-light'>
            {t('admin.title')}
          </h4>
          <hr className='text-light'></hr>
          <br />
          <button
            className='btn btn-outline-light me-2'
            onClick={handleUsersDisplay}>
            {t('admin.nav.users')}
          </button>
          <button
            className='btn btn-outline-light me-2'
            onClick={handleCoursesDisplay}>
            {t('admin.nav.courses')}
          </button>
          <button
            className='btn btn-outline-light me-2'
            onClick={handleCourseItemsDisplay}>
            {t('admin.nav.course_items')}
          </button>
          <button
            className='btn btn-outline-light me-2'
            onClick={handleUserCoursesDisplay}>
            {t('admin.nav.user_courses')}
          </button>
          {isUsersDisplayed && <Users />}
          {isCoursesDisplayed && <Courses />}
          {isCourseItemsDisplayed && <CourseItems />}
          {isUserCoursesDisplayed && <UserCourses />}
        </div>
      </section>
    </>
  );
}