import { useEffect, useState } from 'react';
import { request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

export default function useLoadCourseItems(id) {
  const { t } = useTranslation();
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
        CustomToast(TOAST_ERROR, t('toast.load_error'), TOAST_AUTOCLOSE_SHORT);
        console.log(error);
      }
    };
    loadCourseItems();
  }, [id, t]);

  return [courseItems, setCourseItems];
}
