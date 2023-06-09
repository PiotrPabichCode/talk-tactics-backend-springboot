import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import useLoadUserCourses from './hooks/useLoadUserCourses';
import useSearchUserCourses from './hooks/useSearchUserCourses';

const UserCourses = () => {
  const [level, setLevel] = useState('');
  const [courses, setCourses] = useLoadUserCourses();
  const [searchedCourses, setSearchedCourses] = useState([]);

  useEffect(() => {
    const filterCoursesByLevel = () => {
      const filteredCourses = courses.filter((course) =>
        course.level.toLowerCase().includes(level.toLowerCase())
      );
      setSearchedCourses(filteredCourses);
    };
    filterCoursesByLevel();
  }, [level, courses]);

  const handleInputChange = (event) => {
    setLevel(event.target.value);
  };

  const renderCourses = () => {
    const coursesList = level ? searchedCourses : courses;
    return (
      <table className='table table-responsive table-dark border shadow text-light'>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Level</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {coursesList.map((course, index) => (
            <tr key={course.id}>
              <td>{index + 1}</td>
              <td>{course.name}</td>
              <td>{course.level}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewcourse/${course.id}`}>
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
