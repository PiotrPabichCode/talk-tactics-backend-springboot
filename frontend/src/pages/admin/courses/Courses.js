import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import { request } from '../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default function Courses() {
  const [courses, setCourses] = useState([]);
  const [level, setLevel] = useState('');
  const [searchedCourses, setSearchedCourses] = useState([]);

  useEffect(() => {
    loadCourses();
  }, []);

  useEffect(() => {
    const loadSearchedCourses = async () => {
      try {
        if (level) {
          const response = await request('GET', `/api/courses/level/${level}`);
          setSearchedCourses(response.data);
        } else {
          setSearchedCourses([]);
        }
      } catch (error) {
        console.log(error);
      }
    };

    loadSearchedCourses();
  }, [level]);

  const loadCourses = async () => {
    try {
      const response = await request('GET', '/api/courses');
      setCourses(response.data);
    } catch (error) {
      toast.error('Something went wrong');
      console.log(error);
    }
  };

  const deleteCourse = async (id) => {
    try {
      await request('DELETE', `/api/courses/${id}`);
      toast.success('Course has been deleted');
      loadCourses();
    } catch (error) {
      toast.error('Something went wrong');
      console.log(error);
    }
  };

  const handleInputChange = (event) => {
    setLevel(event.target.value);
  };

  const renderAllCourses = () => {
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
          {courses.map((course, index) => (
            <tr key={course.id}>
              <td>{index + 1}</td>
              <td>{course.name}</td>
              <td>{course.level}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewcourse/${course.id}`}>
                  More details
                </Link>
                <Link
                  className='btn btn-outline-primary mx-2'
                  to={`/editcourse/${course.id}`}>
                  Edit
                </Link>
                <button
                  className='btn btn-danger mx-2'
                  onClick={() => deleteCourse(course.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  const renderSearchedCourses = () => {
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
          {searchedCourses.map((course, index) => (
            <tr key={course.id}>
              <td>{index + 1}</td>
              <td>{course.name}</td>
              <td>{course.level}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewcourse/${course.id}`}>
                  More details
                </Link>
                <Link
                  className='btn btn-outline-primary mx-2'
                  to={`/editcourse/${course.id}`}>
                  Edit
                </Link>
                <button
                  className='btn btn-danger mx-2'
                  onClick={() => deleteCourse(course.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };
  return (
    <div className='container-fluid'>
      <div className='py-4'>
        <h1 className='text-light'>Courses</h1>
        <div className='row'>
          <div className='col'>
            <Link to={'/addcourse'}>
              <button className='btn btn-primary my-3 w-100'>
                Add new course
              </button>
            </Link>
          </div>
          <div className='col'>
            <form className='d-flex my-3'>
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
        {level ? renderSearchedCourses() : renderAllCourses()}
      </div>
    </div>
  );
}
