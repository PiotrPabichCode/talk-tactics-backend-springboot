import { useEffect, useState } from 'react';
import { request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

export default function useLoadCourses() {
  const { t } = useTranslation();
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    const loadCourses = async () => {
      try {
        const response = await request('GET', '/api/courses');
        setCourses(response.data);
      } catch (error) {
        CustomToast(TOAST_ERROR, t('toast.load_error'), TOAST_AUTOCLOSE_SHORT);
        console.log(error);
      }
    };
    loadCourses();
  }, []);

  return [courses, setCourses];
}
