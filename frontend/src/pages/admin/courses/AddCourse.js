import { useState } from 'react';
import { request } from 'api/AxiosHelper';
import { useNavigate, Link } from 'react-router-dom';
import { levels } from './utils/levels';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

const AddCourse = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const url = '/admin?isCoursesDisplayed=true';

  const [course, setCourse] = useState({
    name: '',
    description: '',
    level: '',
  });

  const { name, description, level } = course;

  const onInputChange = (e) => {
    setCourse({ ...course, [e.target.name]: e.target.value });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      if (name !== '' && description !== '' && level !== '') {
        request('POST', `/api/courses`, course);
        CustomToast(
          TOAST_SUCCESS,
          t('toast.success.add.course'),
          TOAST_AUTOCLOSE_SHORT
        );
        navigate(url);
      } else {
        CustomToast(
          TOAST_ERROR,
          t('toast.error.add.course'),
          TOAST_AUTOCLOSE_SHORT
        );
      }
    } catch (error) {
      console.log(error);
      CustomToast(
        TOAST_ERROR,
        t('toast.database_error'),
        TOAST_AUTOCLOSE_SHORT
      );
    }
  };

  return (
    <div className='container-fluid'>
      <div className='row text-light'>
        <div className='col-md-6 offset-md-3 bg-dark opacity-100 border rounded p-4 mt-2 shadow position-relative'>
          <Link
            className='btn btn-primary position-absolute end-0 me-4'
            to={url}>
            {t('admin.courses.add_course.back')}
          </Link>
          <h2 className='text-center m-4'>
            {t('admin.courses.add_course.title')}
          </h2>

          <form onSubmit={onSubmit}>
            <div className='mb-3'>
              <label htmlFor='Name' className='form-label'>
                {t('admin.courses.add_course.form.name')}
              </label>
              <input
                type='text'
                className='form-control'
                placeholder={t(
                  'admin.courses.add_course.form.name_placeholder'
                )}
                name='name'
                value={name}
                onChange={onInputChange}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Description' className='form-label'>
                {t('admin.courses.add_course.form.description')}
              </label>
              <input
                type='text'
                className='form-control'
                placeholder={t(
                  'admin.courses.add_course.form.description_placeholder'
                )}
                name='description'
                value={description}
                onChange={onInputChange}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Level' className='form-label'>
                {t('admin.courses.add_course.form.level')}
              </label>
              <select
                defaultValue={''}
                className='form-control'
                name='level'
                value={level}
                onChange={onInputChange}>
                <option value={''}>
                  {t('admin.courses.add_course.form.level_placeholder')}
                </option>
                {levels.map((item, index) => (
                  <option key={index} value={item.value}>
                    {item.value}
                  </option>
                ))}
              </select>
            </div>
            <button type='submit' className='btn btn-outline-primary'>
              {t('admin.courses.add_course.form.submit')}
            </button>
            <Link className='btn btn-outline-danger mx-2' to={url}>
              {t('admin.courses.add_course.form.cancel')}
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddCourse;
