import { request } from '../../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default async function deleteTask(id, tasks) {
  try {
    await request('DELETE', `/api/tasks/${id}`);
    toast.success('Task deleted successfully');
    tasks = tasks.filter((task) => task.id !== id);
  } catch (error) {
    console.log(error);
    toast.error('Something went wrong');
  }
}
