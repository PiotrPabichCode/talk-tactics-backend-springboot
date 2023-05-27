import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import { request } from '../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default function Tasks() {
  const [tasks, setTasks] = useState([]);
  const [courseName, setCourseName] = useState('');
  const [searchedTasks, setSearchedTasks] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadTasks();
  }, [id]);

  useEffect(() => {
    if (courseName) {
      loadSearchedTasks();
    } else {
      setSearchedTasks([]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [courseName]);

  const loadTasks = async () => {
    try {
      const response = await request('GET', '/api/tasks');
      setTasks(response.data);
    } catch (error) {
      console.log(error);
      toast.error('Something went wrong');
    }
  };

  const loadSearchedTasks = async () => {
    try {
      const response = await request(
        'GET',
        `/api/tasks/course/name/${courseName}`
      );
      setSearchedTasks(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const deleteTask = async (id) => {
    try {
      await request('DELETE', `/api/task/${id}`);
      toast.success('Task deleted successfully');
      courseName ? loadSearchedTasks() : loadTasks();
    } catch (error) {
      console.log(error);
      toast.error('Something went wrong');
    }
  };

  const handleInputChange = (event) => {
    setCourseName(event.target.value);
  };

  const renderAllTasks = () => {
    return (
      <table className='table table-responsive border shadow text-light bg-dark'>
        <thead>
          <tr>
            <th scope='col'>ID</th>
            <th scope='col'>Name</th>
            <th scope='col'>Word</th>
            <th scope='col'>Part of speech</th>
            <th scope='col'>Course</th>
            <th scope='col'>Action</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map((task, index) => (
            <tr key={index}>
              <td>{index + 1}</td>
              <td>{task.name}</td>
              <td>{task.word}</td>
              <td>{task.partOfSpeech}</td>
              <td>{task.course.name}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewtask/${task.id}`}>
                  More details
                </Link>
                <Link
                  className='btn btn-outline-primary mx-2'
                  to={`/edittask/${task.id}`}>
                  Edit
                </Link>
                <button
                  className='btn btn-danger mx-2'
                  onClick={() => deleteTask(task.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  const renderSearchedTasks = () => {
    return (
      <table className='table table-responsive border shadow text-light bg-dark'>
        <thead>
          <tr>
            <th scope='col'>ID</th>
            <th scope='col'>Name</th>
            <th scope='col'>Word</th>
            <th scope='col'>Part of speech</th>
            <th scope='col'>Course</th>
            <th scope='col'>Action</th>
          </tr>
        </thead>
        <tbody>
          {searchedTasks.map((task, index) => (
            <tr>
              <th scope='row' key={index}>
                {index + 1}
              </th>
              <td>{task.name}</td>
              <td>{task.word}</td>
              <td>{task.partOfSpeech}</td>
              <td>{task.course.name}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewtask/${task.id}`}>
                  More details
                </Link>
                <Link
                  className='btn btn-outline-primary mx-2'
                  to={`/edittask/${task.id}`}>
                  Edit
                </Link>
                <button
                  className='btn btn-danger mx-2'
                  onClick={() => deleteTask(task.id)}>
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
        <h1 className='text-light'>Tasks</h1>
        <div className='row'>
          <div className='col'>
            <Link to={'/addtask'}>
              <button className='btn btn-primary my-3 w-100'>
                Add new task
              </button>
            </Link>
          </div>
          <div className='col'>
            <form className='d-flex my-3'>
              <input
                className='form-control me-1'
                type='search'
                value={courseName}
                onChange={handleInputChange}
                placeholder='Enter course name'
                aria-label='Search'
              />
              <button className='btn btn-primary' type='button'>
                <SearchIcon />
              </button>
            </form>
          </div>
        </div>
        {courseName ? renderSearchedTasks() : renderAllTasks()}
      </div>
    </div>
  );
}
