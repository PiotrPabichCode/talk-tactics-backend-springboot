import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import { useLoadUsers } from './hooks/useLoadUsers';
import { useSearchUsers } from './hooks/useSearchUsers';
import { useTranslation } from 'react-i18next';

export default function Users() {
  const { t } = useTranslation();
  const [username, setUsername] = useState('');
  const users = useLoadUsers();
  const searchedUsers = useSearchUsers(username);

  const handleInputChange = (event) => {
    setUsername(event.target.value);
  };

  const renderUsers = () => {
    const userList = username ? searchedUsers : users;
    return (
      <table className='table border shadow text-light bg-dark'>
        <thead>
          <tr>
            <th scope='col'>ID</th>
            <th scope='col'>Login</th>
            <th scope='col'>Email</th>
            <th scope='col'>Admin</th>
            <th scope='col'>{t('admin.users.users.action')}</th>
          </tr>
        </thead>
        <tbody>
          {userList.map((user, index) => (
            <tr key={index}>
              <td>{index + 1}</td>
              <td>{user.login}</td>
              <td>{user.email}</td>
              <td>{user.role === 'ADMIN' ? 'True' : 'False'}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewuser/${user.id}`}>
                  {t('admin.users.users.details')}
                </Link>
                <Link
                  className='btn btn-outline-primary mx-2'
                  to={`/edituser/${user.id}`}>
                  {t('admin.users.users.edit')}
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
        <h1 className='text-light'>{t('admin.users.users.title')}</h1>
        <form className='d-flex my-3'>
          <input
            className='form-control me-1'
            type='search'
            value={username}
            onChange={handleInputChange}
            placeholder={t('admin.users.users.search_placeholder')}
            aria-label='Search'
          />
          <button className='btn btn-primary' type='button'>
            <SearchIcon />
          </button>
        </form>
        {renderUsers()}
      </div>
    </div>
  );
}
