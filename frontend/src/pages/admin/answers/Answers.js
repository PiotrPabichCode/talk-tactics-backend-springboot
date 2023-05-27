import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import { request } from '../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default function Answers() {
  const [answers, setAnswers] = useState([]);
  const [username, setUsername] = useState('');
  const [searchedAnswers, setSearchedAnswers] = useState([]);

  useEffect(() => {
    loadAnswers();
  }, []);

  useEffect(() => {
    if (username) {
      const loadSearchedAnswers = async () => {
        try {
          const response = await request(
            'GET',
            `/api/answers/username/${username}`
          );
          setSearchedAnswers(response.data);
        } catch (error) {
          toast.error('Unable to fetch data', {
            autoClose: 1000,
          });
        }
      };
      loadSearchedAnswers();
    } else {
      setSearchedAnswers([]);
    }
  }, [username]);

  const loadAnswers = async () => {
    try {
      const response = await request('GET', '/api/answers');
      setAnswers(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleInputChange = (event) => {
    setUsername(event.target.value);
  };

  const renderAllAnswers = () => {
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
          {answers.map((answer, index) => (
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

  const renderSearchedAnswers = () => {
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
          {searchedAnswers.map((answer, index) => (
            <tr>
              <th scope='row' key={index}>
                {index + 1}
              </th>
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
    <div className='container'>
      <div className='py-4'>
        <h1 className='text-light'>Answers</h1>
        <form className='d-flex my-4'>
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
        {username ? renderSearchedAnswers() : renderAllAnswers()}
      </div>
    </div>
  );
}
