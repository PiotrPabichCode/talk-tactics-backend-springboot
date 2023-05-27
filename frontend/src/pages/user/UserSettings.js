import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link, useParams } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import { request } from '../../api/AxiosHelper';

export default function UserSettings() {
  const [settings, setSettings] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    const loadSettings = async () => {
      try {
        const response = await request('GET', `/api/user${id}`);
        console.log(response.data);
        setSettings(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadSettings();
  }, []);

  return (
    <div className='container'>
      <div className='py-4'>
        <h1 className='text-light'>Users</h1>
        <form className='d-flex my-4'>
          <input
            className='form-control me-1'
            type='search'
            placeholder='Search'
            aria-label='Search'
          />
          <button className='btn btn-primary' type='submit'>
            <SearchIcon />
          </button>
        </form>

        <table className='table border shadow text-light bg-dark'>
          <thead>
            <tr>
              <th scope='col'>ID</th>
              <th scope='col'>Login</th>
              <th scope='col'>Password</th>
              <th scope='col'>Admin</th>
              <th scope='col'>Action</th>
            </tr>
          </thead>
          <tbody></tbody>
        </table>
      </div>
    </div>
  );
}
