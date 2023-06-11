import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import useLoadUserCourses from './hooks/useLoadUserCourses';
import useSearchUserCourses from './hooks/useSearchUserCourses';
import { getUserID } from '../../api/AxiosHelper';

const UserCourses = () => {
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
            <th>Name</th>
            <th>Level</th>
            <th>Progress</th>
            <th>Completed</th>
            <th>Action</th>
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
              <td>{userCourse.completed ? 'Yes' : 'No'}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/users/${userCourse.user.id}/user-courses/${userCourse.id}`}>
                  Open course
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
        <h1 className='text-light'>Courses</h1>
        <div className='row my-3'>
          <div className='col'>
            <form className='d-flex'>
              <input
                className='form-control me-1'
                type='search'
                value={level}
                onChange={handleInputChange}
                placeholder='Search course level'
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
