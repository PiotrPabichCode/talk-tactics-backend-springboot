import { useEffect, useState } from 'react';
import { request } from 'api/AxiosHelper';

export default function useCourseItemDetails(id) {
  const [itemDetails, setItemDetails] = useState({});

  useEffect(() => {
    const loadCourseItemDetails = async () => {
      try {
        const response = await request('GET', `/api/course-items/${id}`);
        setItemDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadCourseItemDetails();
  }, [id]);

  return itemDetails;
}
