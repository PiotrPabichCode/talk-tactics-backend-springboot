import { request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';

export default async function deleteCourseItem(id) {
  try {
    await request('DELETE', `/api/course-items/${id}`);
    CustomToast(
      TOAST_SUCCESS,
      'Course item deleted successfully',
      TOAST_AUTOCLOSE_SHORT
    );
  } catch (error) {
    CustomToast(
      TOAST_ERROR,
      'Unable to delete course item',
      TOAST_AUTOCLOSE_SHORT
    );
    console.log(error);
  }
}
