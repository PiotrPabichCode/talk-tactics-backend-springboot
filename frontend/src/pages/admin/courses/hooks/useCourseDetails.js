import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';

export const useCourseDetails = (id) => {
  const [courseDetails, setCourseDetails] = useState({});

  useEffect(() => {
    const loadCourse = async () => {
      try {
        const response = await request('GET', `/api/courses/${id}`);
        setCourseDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadCourse();
  }, [id]);

  return courseDetails;
};
