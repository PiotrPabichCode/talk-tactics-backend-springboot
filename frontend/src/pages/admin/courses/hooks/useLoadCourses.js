import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default function useLoadCourses() {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    const loadCourses = async () => {
      try {
        const response = await request('GET', '/api/courses');
        setCourses(response.data);
      } catch (error) {
        toast.error('Something went wrong');
        console.log(error);
      }
    };
    loadCourses();
  }, []);

  return [courses, setCourses];
}
