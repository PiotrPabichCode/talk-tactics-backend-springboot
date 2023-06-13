import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import useLoadCourses from './hooks/useLoadCourses';
import useSearchCourses from './hooks/useSearchCourses';
import deleteCourse from './hooks/deleteCourse';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

const Courses = () => {
  const { t } = useTranslation();
  const [level, setLevel] = useState('');
  const searchedCourses = useSearchCourses(level);
  const [courses, setCourses] = useLoadCourses();

  const handleInputChange = (event) => {
    setLevel(event.target.value);
  };

  const handleDeleteAction = async (id) => {
    try {
      await deleteCourse(id);
      setCourses((prevCourses) => {
        return prevCourses.filter((course) => course.id !== id);
      });
    } catch (error) {
      console.log(error);
      CustomToast(
        TOAST_ERROR,
        t('toast.error.delete.course'),
        TOAST_AUTOCLOSE_SHORT
      );
    }
  };

  const renderCourses = () => {
    const coursesList = level ? searchedCourses : courses;
    return (
      <table className='table table-responsive table-dark border shadow text-light'>
        <thead>
          <tr>
            <th>ID</th>
            <th>{t('admin.courses.courses.header_name')}</th>
            <th>{t('admin.courses.courses.header_level')}</th>
            <th>{t('admin.courses.courses.header_action')}</th>
          </tr>
        </thead>
        <tbody>
          {coursesList.map((course, index) => (
            <tr key={course.id}>
              <td>{index + 1}</td>
              <td>{course.name}</td>
              <td>{course.level}</td>
              <td>
                <Link
                  className='btn btn-primary mx-2'
                  to={`/viewcourse/${course.id}`}>
                  {t('admin.courses.courses.more_details')}
                </Link>
                <Link
                  className='btn btn-outline-primary mx-2'
                  to={`/editcourse/${course.id}`}>
                  {t('admin.courses.courses.edit')}
                </Link>
                <button
                  className='btn btn-danger mx-2'
                  onClick={() => handleDeleteAction(course.id)}>
                  {t('admin.courses.courses.delete')}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  return (
    <div className='container-fluid'>
      <div className='py-3'>
        <h1 className='text-light'>{t('admin.courses.courses.title')}</h1>
        <div className='row my-3'>
          <div className='col'>
            <Link to={'/addcourse'}>
              <button className='btn btn-primary w-100'>
                {t('admin.courses.courses.add')}
              </button>
            </Link>
          </div>
          <div className='col'>
            <form className='d-flex'>
              <input
                className='form-control me-1'
                type='search'
                value={level}
                onChange={handleInputChange}
                placeholder={t('admin.courses.courses.search_placeholder')}
                aria-label='Search'
              />
              <button className='btn btn-primary' type='button'>
                <SearchIcon />
              </button>
            </form>
          </div>
        </div>
        {renderCourses()}
      </div>
    </div>
  );
};

export default Courses;
