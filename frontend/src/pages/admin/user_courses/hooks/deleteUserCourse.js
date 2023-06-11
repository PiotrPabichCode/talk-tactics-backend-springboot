import { request } from '../../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default async function deleteUserCourse(id) {
  try {
    await request('DELETE', `/api/user-courses/${id}`);
    toast.success('User course has been deleted');
  } catch (error) {
    console.log(error);
    toast.error('Something went wrong');
  }
}
