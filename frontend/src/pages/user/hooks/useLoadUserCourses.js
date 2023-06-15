import { useEffect, useState } from 'react';
import { getUsername, request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

export default function useLoadUserCourses() {
  const { t } = useTranslation();
  const [userCourses, setUserCourses] = useState([]);
  const login = getUsername();

  useEffect(() => {
    const loadUserCourses = async () => {
      try {
        console.log(login);
        const response = await request(
          'GET',
          `/api/user-courses/users/login/${login}`
        );
        console.log(response.data);
        setUserCourses(response.data);
      } catch (error) {
        CustomToast(TOAST_ERROR, t('toast.load_error'), TOAST_AUTOCLOSE_SHORT);
        console.log(error);
      }
    };
    loadUserCourses();
  }, [login, t]);

  return [userCourses, setUserCourses];
}
