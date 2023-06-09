import { request } from '../../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default async function deleteCourse(id) {
  try {
    await request('DELETE', `/api/courses/${id}`);
    toast.success('Course has been deleted');
  } catch (error) {
    console.log(error);
    toast.error('Something went wrong');
  }
}
