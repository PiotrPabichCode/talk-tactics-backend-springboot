import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

export default function EditUser() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const url = '/admin?isUserDisplayed=true';
  const roles = [{ value: 'ADMIN' }, { value: 'USER' }];

  const { id } = useParams();

  const [updateData, setUpdateData] = useState({
    id: 0,
    login: '',
    firstName: '',
    lastName: '',
    email: '',
    role: '',
  });

  const { login, firstName, lastName, email, role } = updateData;

  const onInputChange = (e) => {
    setUpdateData({ ...updateData, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    const loadUser = async () => {
      try {
        const response = await request('GET', `/api/users/${id}`);
        console.log(response.data);
        setUpdateData((updateData) => ({
          ...updateData,
          id: response.data.id,
          login: response.data.login,
          firstName: response.data.firstName,
          lastName: response.data.lastName,
          email: response.data.email,
          role: response.data.role,
        }));
      } catch (error) {
        console.log(error);
      }
    };
    loadUser();
  }, [id]);

  useEffect(() => {
    console.log(updateData);
  }, [updateData]);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      console.log(updateData);
      await request('PUT', `/api/update_user`, updateData);
      CustomToast(
        TOAST_SUCCESS,
        t('toast.success.edit.user'),
        TOAST_AUTOCLOSE_SHORT
      );
      navigate(url);
    } catch (error) {
      CustomToast(
        TOAST_ERROR,
        t('toast.error.edit.user'),
        TOAST_AUTOCLOSE_SHORT
      );
      console.log(error);
    }
  };

  return (
    <div className='container-fluid px-4 pb-4'>
      <div className='row text-light'>
        <div className='col-md-6 offset-md-3 bg-dark opacity-100 border rounded p-4 mt-2 shadow position-relative'>
          <Link
            className='btn btn-primary position-absolute end-0 me-4'
            to={url}>
            {t('admin.users.edit_user.back')}
          </Link>
          <h2 className='text-center m-4'>
            {t('admin.users.edit_user.title')}
          </h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className='mb-3'>
              <label htmlFor='Login' className='form-label'>
                Login
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder={t('admin.users.edit_user.form.login_placeholder')}
                name='login'
                value={login}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='FirstName' className='form-label'>
                {t('admin.users.edit_user.form.first_name')}
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder={t(
                  'admin.users.edit_user.form.first_name_placeholder'
                )}
                name='firstName'
                value={firstName}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='LastName' className='form-label'>
                {t('admin.users.edit_user.form.last_name')}
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder={t(
                  'admin.users.edit_user.form.last_name_placeholder'
                )}
                name='lastName'
                value={lastName}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Email' className='form-label'>
                Email
              </label>
              <input
                type={'email'}
                className='form-control'
                placeholder={t('admin.users.edit_user.form.email_placeholder')}
                name='email'
                value={email}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Admin' className='form-label'>
                Admin
              </label>
              <select
                className='form-control'
                name='role'
                value={role}
                onChange={(e) => onInputChange(e)}>
                {roles.map((item) => (
                  <option key={item.value} value={item.value}>
                    {item.value}
                  </option>
                ))}
              </select>
            </div>
            <button type='submit' className='btn btn-outline-primary'>
              {t('admin.users.edit_user.form.submit')}
            </button>
            <Link className='btn btn-outline-danger mx-2' to={url}>
              {t('admin.users.edit_user.form.cancel')}
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
}
