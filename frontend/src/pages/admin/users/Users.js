import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import { request } from '../../../api/AxiosHelper';

export default function Users() {
  const [users, setUsers] = useState([]);
  const [username, setUsername] = useState('');
  const [searchedUsers, setSearchedUsers] = useState([]);

  useEffect(() => {
    const loadUsers = async () => {
      try {
        const response = await request('GET', '/api/users');
        setUsers(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadUsers();
  }, []);

  useEffect(() => {
    if (username) {
      const loadSearchedUsers = async () => {
        try {
          const response = await request('GET', `/api/users/login/${username}`);
          setSearchedUsers(response.data);
        } catch (error) {
          console.log(error);
        }
      };
      loadSearchedUsers();
    } else {
      setSearchedUsers([]);
    }
  }, [username]);

  const handleInputChange = (event) => {
    setUsername(event.target.value);
  };

  const renderAllUsers = () => {
    return (
      <table className='table border shadow text-light bg-dark'>
        <thead>
          <tr>
            <th scope='col'>ID</th>
            <th scope='col'>Login</th>
            <th scope='col'>Email</th>
            <th scope='col'>Admin</th>
            <th scope='col'>Action</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user, index) => (
            <tr key={index}>
              <td>{index + 1}</td>
              <td>{user.login}</td>
              <td>{user.email}</td>
              <td>{user.role === 'ADMIN' ? 'True' : 'False'}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewuser/${user.id}`}>
                  More details
                </Link>
                <Link
                  className='btn btn-outline-primary mx-2'
                  to={`/edituser/${user.id}`}>
                  Edit
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  const renderSearchedUsers = () => {
    return (
      <table className='table border shadow text-light bg-dark'>
        <thead>
          <tr>
            <th scope='col'>ID</th>
            <th scope='col'>Login</th>
            <th scope='col'>Email</th>
            <th scope='col'>Admin</th>
            <th scope='col'>Action</th>
          </tr>
        </thead>
        <tbody>
          {searchedUsers.map((user, index) => (
            <tr>
              <th scope='row' key={index}>
                {index + 1}
              </th>
              <td>{user.login}</td>
              <td>{user.email}</td>
              <td>{user.admin === true ? 'True' : 'False'}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewuser/${user.id}`}>
                  More details
                </Link>
                <Link
                  className='btn btn-outline-primary mx-2'
                  to={`/edituser/${user.id}`}>
                  Edit
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
        <h1 className='text-light'>Users</h1>
        <form className='d-flex my-4'>
          <input
            className='form-control me-1'
            type='search'
            value={username}
            onChange={handleInputChange}
            placeholder='Search by login'
            aria-label='Search'
          />
          <button className='btn btn-primary' type='button'>
            <SearchIcon />
          </button>
        </form>
        {username ? renderSearchedUsers() : renderAllUsers()}
      </div>
    </div>
  );
}
