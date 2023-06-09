import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';

export const useTaskDetails = (id) => {
  const [taskDetails, setTaskDetails] = useState({});

  useEffect(() => {
    const loadTask = async () => {
      try {
        const response = await request('GET', `/api/tasks/${id}`);
        console.log(response.data);
        setTaskDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadTask();
  }, [id]);

  return taskDetails;
};
