import { request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';

export default async function deleteCourse(id) {
  try {
    await request('DELETE', `/api/courses/${id}`);
    CustomToast(
      TOAST_SUCCESS,
      'Course deleted successfully',
      TOAST_AUTOCLOSE_SHORT
    );
  } catch (error) {
    console.log(error);
    CustomToast(TOAST_ERROR, 'Unable to delete course', TOAST_AUTOCLOSE_SHORT);
  }
}
