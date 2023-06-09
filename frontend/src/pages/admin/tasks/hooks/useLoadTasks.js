import { useState, useEffect } from 'react';
import { request } from '../../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export const useLoadTasks = () => {
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    const loadTasks = async () => {
      try {
        const response = await request('GET', '/api/tasks');
        setTasks(response.data);
      } catch (error) {
        console.log(error);
        toast.error('Something went wrong');
      }
    };
    loadTasks();
  }, [tasks]);

  return tasks;
};
