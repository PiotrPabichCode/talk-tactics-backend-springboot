import { useEffect, useState } from 'react';
import { request } from '../../../api/AxiosHelper';
import { toast } from 'react-toastify';
import { getUsername } from '../../../api/AxiosHelper';

export default function useLoadUserCourses() {
  const [courses, setCourses] = useState([]);
  const login = getUsername();

  useEffect(() => {
    const loadCourses = async () => {
      try {
        const response = await request('GET', `/api/users/${login}/courses`);
        setCourses(response.data);
      } catch (error) {
        toast.error('Something went wrong');
        console.log(error);
      }
    };
    loadCourses();
  }, [login]);

  return [courses, setCourses];
}
