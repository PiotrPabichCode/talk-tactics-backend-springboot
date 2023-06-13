import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import useLoadUserCourses from './hooks/useLoadUserCourses';
import useSearchUserCourses from './hooks/useSearchUserCourses';
import { getUserID } from 'api/AxiosHelper';
import { useTranslation } from 'react-i18next';

const UserCourses = () => {
  const { t } = useTranslation();
  const [level, setLevel] = useState('');
  const [userCourses, setUserCourses] = useLoadUserCourses();
  const [searchedCourses, setSearchedCourses] = useState([]);

  useEffect(() => {
    const filterCoursesByLevel = () => {
      const filteredCourses = userCourses.filter((userCourse) =>
        userCourse.course.level.toLowerCase().includes(level.toLowerCase())
      );
      setSearchedCourses(filteredCourses);
    };
    filterCoursesByLevel();
  }, [level, userCourses]);

  const handleInputChange = (event) => {
    setLevel(event.target.value);
  };

  const renderCourses = () => {
    const coursesList = level ? searchedCourses : userCourses;
    return (
      <table className='table table-responsive table-dark border shadow text-light'>
        <thead>
          <tr>
            <th>ID</th>
            <th>{t('user.user_courses.header_name')}</th>
            <th>{t('user.user_courses.header_level')}</th>
            <th>{t('user.user_courses.header_progress')}</th>
            <th>{t('user.user_courses.header_completed')}</th>
            <th>{t('user.user_courses.header_action')}</th>
          </tr>
        </thead>
        <tbody>
          {coursesList.map((userCourse, index) => (
            <tr key={userCourse.id}>
              {console.log(userCourse)}
              <td>{index + 1}</td>
              <td>{userCourse.course.name}</td>
              <td>{userCourse.course.level}</td>
              <td>{userCourse.progress.toFixed(2)}%</td>
              <td>
                {userCourse.completed
                  ? t('user.user_courses.yes')
                  : t('user.user_courses.no')}
              </td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/users/${userCourse.user.id}/user-courses/${userCourse.id}`}>
                  {t('user.user_courses.open')}
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  return (
    <div className='container-fluid'>
      <div className='py-3'>
        <h1 className='text-light'>{t('user.user_courses.title')}</h1>
        <div className='row my-3'>
          <div className='col'>
            <form className='d-flex'>
              <input
                className='form-control me-1'
                type='search'
                value={level}
                onChange={handleInputChange}
                placeholder={t('user.user_courses.search_placeholder')}
                aria-label='Search'
              />
              <button className='btn btn-primary' type='button'>
                <SearchIcon />
              </button>
            </form>
          </div>
        </div>
        {renderCourses()}
      </div>
    </div>
  );
};

export default UserCourses;
