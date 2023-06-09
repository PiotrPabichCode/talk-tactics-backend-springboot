import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';

const useSearchCourses = (level) => {
  const [searchedCourses, setSearchedCourses] = useState([]);

  useEffect(() => {
    const loadSearchedCourses = async () => {
      try {
        if (level) {
          const response = await request('GET', `/api/courses/level/${level}`);
          setSearchedCourses(response.data);
        } else {
          setSearchedCourses([]);
        }
      } catch (error) {
        console.log(error);
      }
    };
    loadSearchedCourses();
  }, [level]);

  return searchedCourses;
};

export default useSearchCourses;
