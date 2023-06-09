import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';

export const useLoadUserDetails = (id) => {
  const [userDetails, setUserDetails] = useState({});

  useEffect(() => {
    const loadUser = async () => {
      try {
        const response = await request('GET', `/api/users/${id}`);
        console.log(response.data);
        setUserDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadUser();
  }, [id]);

  return userDetails;
};
