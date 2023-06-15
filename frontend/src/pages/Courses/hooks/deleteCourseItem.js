import { request } from 'api/AxiosHelper';

export default async function deleteCourseItem(id) {
  try {
    await request('DELETE', `/api/course-items/${id}`);
  } catch (error) {
    throw error;
  }
}
