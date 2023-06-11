import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default function useLoadCourseItems(id) {
  const [courseItems, setCourseItems] = useState([]);

  useEffect(() => {
    const loadCourseItems = async () => {
      try {
        const response = await request(
          'GET',
          `/api/courses/${id}/course-items`
        );
        setCourseItems(response.data);
      } catch (error) {
        toast.error('Something went wrong');
        console.log(error);
      }
    };
    loadCourseItems();
  }, []);

  return [courseItems, setCourseItems];
}
