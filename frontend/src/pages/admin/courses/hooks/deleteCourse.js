import { request } from 'api/AxiosHelper';

export default async function deleteCourse(id) {
  try {
    await request('DELETE', `/api/courses/${id}`);
  } catch (error) {
    throw error;
  }
}
