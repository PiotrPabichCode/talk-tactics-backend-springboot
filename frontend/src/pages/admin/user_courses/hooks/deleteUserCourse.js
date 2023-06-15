import { request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';

export default async function deleteUserCourse(id) {
  try {
    await request('DELETE', `/api/user-courses/${id}`);
    CustomToast(
      TOAST_SUCCESS,
      'User course has been deleted',
      TOAST_AUTOCLOSE_SHORT
    );
  } catch (error) {
    CustomToast(
      TOAST_ERROR,
      'Unable to delete user course',
      TOAST_AUTOCLOSE_SHORT
    );
    console.log(error);
  }
}
