import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { request } from 'api/AxiosHelper';
import { levels } from './utils/levels';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

export default function EditUser() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const url = '/admin?isCoursesDisplayed=true';

  const { id } = useParams();

  const LevelEnum = {
    BEGINNER: 'BEGINNER',
    INTERMEDIATE: 'INTERMEDIATE',
    ADVANCED: 'ADVANCED',
  };

  const [course, setCourse] = useState({
    name: '',
    description: '',
    level: LevelEnum.BEGINNER,
  });

  const { name, description, level } = course;

  const onInputChange = (e) => {
    setCourse({ ...course, [e.target.name]: e.target.value });
    console.log(course);
  };

  useEffect(() => {
    const loadCourse = async () => {
      try {
        const response = await request('GET', `/api/courses/${id}`);
        setCourse(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadCourse();
  }, [id]);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      if (name !== '' && description !== '' && level !== '') {
        const updatedCourse = { ...course, level: LevelEnum[level] };
        await request('PUT', `/api/courses/${id}`, updatedCourse);
        navigate(url);
        CustomToast(
          TOAST_SUCCESS,
          t('toast.success.edit.course'),
          TOAST_AUTOCLOSE_SHORT
        );
      } else {
        CustomToast(
          TOAST_ERROR,
          t('toast.error.edit.course'),
          TOAST_AUTOCLOSE_SHORT
        );
      }
    } catch (error) {
      CustomToast(
        TOAST_ERROR,
        t('toast.database_error'),
        TOAST_AUTOCLOSE_SHORT
      );
      console.log(error);
    }
  };

  return (
    <div className='container-fluid'>
      <div className='col-md-6 text-light offset-md-3 bg-dark opacity-100 border rounded p-4 mt-2 shadow position-relative'>
        <Link className='btn btn-primary position-absolute end-0 me-4' to={url}>
          {t('admin.courses.edit_course.back')}
        </Link>
        <h2 className='text-center m-4'>
          {t('admin.courses.edit_course.title')}
        </h2>

        <form onSubmit={(e) => onSubmit(e)}>
          <div className='mb-3'>
            <label htmlFor='Name' className='form-label'>
              {t('admin.courses.edit_course.form.name')}
            </label>
            <input
              type={'text'}
              className='form-control'
              placeholder={t('admin.courses.edit_course.form.name_placeholder')}
              name='name'
              value={name}
              onChange={(e) => onInputChange(e)}
            />
          </div>
          <div className='mb-3'>
            <label htmlFor='Description' className='form-label'>
              {t('admin.courses.edit_course.form.description')}
            </label>
            <input
              type={'text'}
              className='form-control'
              placeholder={t(
                'admin.courses.edit_course.form.description_placeholder'
              )}
              name='description'
              value={description}
              onChange={(e) => onInputChange(e)}
            />
          </div>
          <div className='mb-3'>
            <label htmlFor='Level' className='form-label'>
              {t('admin.courses.edit_course.form.level')}
            </label>
            <select
              className='form-control'
              name='level'
              value={level}
              onChange={(e) => onInputChange(e)}>
              {levels.map((item) => (
                <option key={item.id} value={item.value}>
                  {item.value}
                </option>
              ))}
            </select>
          </div>
          <button type='submit' className='btn btn-outline-primary'>
            {t('admin.courses.edit_course.form.submit')}
          </button>
          <Link className='btn btn-outline-danger mx-2' to={url}>
            {t('admin.courses.edit_course.form.cancel')}
          </Link>
        </form>
      </div>
    </div>
  );
}