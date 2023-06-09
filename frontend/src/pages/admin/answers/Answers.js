import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import useLoadSearchedAnswers from './hooks/useLoadSearchedAnswers';
import useLoadAnswers from './hooks/useLoadAnswers';

export default function Answers() {
  const [username, setUsername] = useState('');
  const answers = useLoadAnswers();
  const searchedAnswers = useLoadSearchedAnswers(username);

  const handleInputChange = (event) => {
    setUsername(event.target.value);
  };

  const renderAnswers = () => {
    const answersList = username ? searchedAnswers : answers;
    return (
      <table className='table border shadow text-light bg-dark'>
        <thead>
          <tr>
            <th scope='col'>ID</th>
            <th scope='col'>Course</th>
            <th scope='col'>Task</th>
            <th scope='col'>Username</th>
            <th scope='col'>Action</th>
          </tr>
        </thead>
        <tbody>
          {answersList.map((answer, index) => (
            <tr key={index}>
              <td>{index + 1}</td>
              <td>{answer.task.course.name}</td>
              <td>{answer.task.name}</td>
              <td>{answer.user.login}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewanswer/${answer.id}`}>
                  More details
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
        <h1 className='text-light'>Answers</h1>
        <form className='d-flex my-3'>
          <input
            className='form-control me-1'
            type='search'
            value={username}
            onChange={handleInputChange}
            placeholder='Enter username'
            aria-label='Search'
          />
          <button className='btn btn-primary' type='button'>
            <SearchIcon />
          </button>
        </form>
        {renderAnswers()}
      </div>
    </div>
  );
}
