import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import { useLoadTasks } from './hooks/useLoadTasks';
import { useSearchedTasks } from './hooks/useSearchedTasks';
import deleteTask from './hooks/deleteTask';

const Tasks = () => {
  const [courseName, setCourseName] = useState('');

  const tasks = useLoadTasks();
  const searchedTasks = useSearchedTasks(courseName);

  const handleInputChange = (event) => {
    setCourseName(event.target.value);
  };

  const renderTasks = () => {
    const tasksList = courseName ? searchedTasks : tasks;
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
          {tasksList.map((task, index) => (
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
                  onClick={() => deleteTask(task.id, tasks)}>
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
      <div className='py-3'>
        <h1 className='text-light'>Tasks</h1>
        <div className='row my-3'>
          <div className='col'>
            <Link to={'/addtask'}>
              <button className='btn btn-primary w-100'>Add new task</button>
            </Link>
          </div>
          <div className='col'>
            <form className='d-flex'>
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
        {renderTasks()}
      </div>
    </div>
  );
};

export default Tasks;
