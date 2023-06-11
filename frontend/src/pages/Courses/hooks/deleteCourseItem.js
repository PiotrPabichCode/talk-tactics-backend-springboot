import { request } from '../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default async function deleteCourseItem(id) {
  try {
    await request('DELETE', `/api/course-items/${id}`);
    toast.success('Course item has been deleted');
  } catch (error) {
    console.log(error);
    toast.error('Something went wrong');
  }
}
