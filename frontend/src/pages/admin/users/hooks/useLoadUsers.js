import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';

export const useLoadUsers = () => {
  const [users, setUsers] = useState([]);

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

  return users;
};
