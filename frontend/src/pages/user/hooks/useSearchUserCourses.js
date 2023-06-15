import { useEffect, useState } from 'react';
import { request, getUsername } from 'api/AxiosHelper';

const useSearchUserCourses = (level) => {
  const [searchedCourses, setSearchedCourses] = useState([]);
  const login = getUsername();
  useEffect(() => {
    const loadSearchedCourses = async () => {
      try {
        if (level) {
          const response = await request(
            'GET',
            `/api/users/${login}/courses/level/${level}`
          );
          setSearchedCourses(response.data);
        } else {
          setSearchedCourses([]);
        }
      } catch (error) {
        console.log(error);
      }
    };
    loadSearchedCourses();
  }, [level, login]);

  return searchedCourses;
};

export default useSearchUserCourses;
