import React, { useState } from 'react';
import SearchIcon from '@mui/icons-material/Search';
// import useSearchCourses from './hooks/useSearchCourses';
import { Link } from 'react-router-dom';
import useLoadUsersCourses from './hooks/useLoadUsersCourses';
import deleteUserCourse from './hooks/deleteUserCourse';
import { toast } from 'react-toastify';

const UserCourses = () => {
  const [level, setLevel] = useState('');
  //   const searchedCourses = useSearchCourses(level);
  const searchedCourses = useState([]);
  const [userCourses, setUserCourses] = useLoadUsersCourses();

  const handleInputChange = (event) => {
    setLevel(event.target.value);
  };

  const handleDeleteAction = async (id) => {
    try {
      await deleteUserCourse(id);
      setUserCourses((prevCourses) => {
        return prevCourses.filter((course) => course.id !== id);
      });
    } catch (error) {
      console.log(error);
      toast.error('Something went wrong');
    }
  };

  const renderUserCourses = () => {
    const usersCoursesList = level ? searchedCourses : userCourses;
    return (
      <table className='table table-responsive table-dark border shadow text-light'>
        <thead>
          <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Name</th>
            <th>Level</th>
            <th>Progress</th>
            <th>Completed</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {usersCoursesList.map((userCourse, index) => {
            return (
              <tr key={index}>
                <td>{index + 1}</td>
                <td>{userCourse.user.login}</td>
                <td>{userCourse.course.name}</td>
                <td>{userCourse.course.level}</td>
                <td>{userCourse.progress.toFixed(2)}%</td>
                <td>{userCourse.completed ? 'Yes' : 'No'}</td>
                <td>
                  <Link
                    className='btn btn-primary mx-2'
                    to={`/viewcourse/${userCourse.course.id}`}>
                    More details
                  </Link>
                  <button
                    className='btn btn-danger mx-2'
                    onClick={() => handleDeleteAction(userCourse.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    );
  };

  return (
    <div className='container-fluid'>
      <div className='py-3'>
        <h1 className='text-light'>User courses</h1>
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
        {renderUserCourses()}
      </div>
    </div>
  );
};

export default UserCourses;
