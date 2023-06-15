import React, { useState } from 'react';
import './LoginRegister.css';
import { Link, useNavigate } from 'react-router-dom';
import { request, setUserData } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';
import { useAuth } from 'context/AuthContext';

const SignIn = () => {
  const { t } = useTranslation();
  const auth = useAuth();
  const navigate = useNavigate();
  const [user, setUser] = useState({
    login: '',
    password: '',
  });

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleLoginForm = async (e) => {
    e.preventDefault();
    try {
      const response = await request('POST', '/api/auth/authenticate', user);
      setUserData(response.data);
      CustomToast(
        TOAST_SUCCESS,
        t('auth.success.sign_in'),
        TOAST_AUTOCLOSE_SHORT
      );
      setUser({ login: '', password: '' });
      auth.login();
      navigate('/');
    } catch (error) {
      CustomToast(TOAST_ERROR, t('auth.error.sign_in'), TOAST_AUTOCLOSE_SHORT);
      console.log(error);
    }
  };

  const renderLoginForm = () => {
    return (
      <form className='mb-3 mt-md-4' onSubmit={handleLoginForm}>
        <h2 className='fw-bold mb-2 text-uppercase '>TalkTactics</h2>
        <div className='mb-3'>
          <label htmlFor='login' className='form-label '>
            Login
          </label>
          <input
            type='text'
            className='form-control'
            id='login'
            name='login'
            value={user.login}
            onChange={handleChange}
            placeholder={t('auth.sign_in.form.login_placeholder')}
          />
        </div>
        <div className='mb-3'>
          <label htmlFor='password' className='form-label '>
            {t('auth.sign_in.form.password')}
          </label>
          <input
            type='password'
            className='form-control'
            id='password'
            name='password'
            value={user.password}
            onChange={handleChange}
            placeholder='*******'
          />
        </div>
        <div className='d-grid'>
          <button className='btn btn-outline-dark' type='submit'>
            {t('auth.sign_in.form.submit')}
          </button>
        </div>
      </form>
    );
  };

  return (
    <>
      <div className='pt-4 justify-content-center align-items-center'>
        <div className='container'>
          <div className='row d-flex justify-content-center'>
            <div className='col-12 col-md-8 col-lg-6'>
              <div className='card bg-white shadow-lg'>
                <div className='card-body p-5'>
                  {renderLoginForm()}
                  <div>
                    <p className='mb-0 text-center'>
                      {t('auth.sign_in.question')}
                      <Link to='/register' className='text-primary fw-bold'>
                        {t('auth.sign_in.link')}
                      </Link>
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default SignIn;
