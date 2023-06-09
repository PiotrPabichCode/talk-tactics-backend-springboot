import { useState, useEffect } from 'react';
import { request } from '../../../../api/AxiosHelper';

export const useSearchedTasks = (courseName) => {
  const [searchedTasks, setSearchedTasks] = useState([]);

  useEffect(() => {
    if (courseName) {
      loadSearchedTasks();
    } else {
      setSearchedTasks([]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [courseName]);

  const loadSearchedTasks = async () => {
    try {
      const response = await request(
        'GET',
        `/api/tasks/course/name/${courseName}`
      );
      setSearchedTasks(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  return searchedTasks;
};
