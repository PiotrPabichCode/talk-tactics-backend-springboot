import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';

export const useSearchUsers = (username) => {
  const [searchedUsers, setSearchedUsers] = useState([]);

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

  return searchedUsers;
};
