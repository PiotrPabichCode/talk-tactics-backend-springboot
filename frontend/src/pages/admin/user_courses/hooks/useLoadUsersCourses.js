import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default function useLoadUsersCourses() {
  const [userCourses, setUserCourses] = useState([]);

  useEffect(() => {
    const loadUserCourses = async () => {
      try {
        const response = await request('GET', '/api/user-courses');
        setUserCourses(response.data);
      } catch (error) {
        toast.error('Something went wrong');
        console.log(error);
      }
    };
    loadUserCourses();
  }, []);

  return [userCourses, setUserCourses];
}
