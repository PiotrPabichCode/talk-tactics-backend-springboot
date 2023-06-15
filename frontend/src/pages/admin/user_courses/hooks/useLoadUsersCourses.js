import { useEffect, useState } from 'react';
import { request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

export default function useLoadUsersCourses() {
  const { t } = useTranslation();
  const [userCourses, setUserCourses] = useState([]);

  useEffect(() => {
    const loadUserCourses = async () => {
      try {
        const response = await request('GET', '/api/user-courses');
        setUserCourses(response.data);
      } catch (error) {
        CustomToast(TOAST_ERROR, t('toast.load_error'), TOAST_AUTOCLOSE_SHORT);
        console.log(error);
      }
    };
    loadUserCourses();
  }, [t]);

  return [userCourses, setUserCourses];
}
