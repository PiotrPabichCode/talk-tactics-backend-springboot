import React, { useState } from 'react';
import './LoginRegister.css';
import { Link, useNavigate } from 'react-router-dom';
import { request, setUserData } from '../../api/AxiosHelper';
import { toast } from 'react-toastify';

const SignUp = () => {
  const navigate = useNavigate();

  const [user, setUser] = useState({
    login: '',
    password: '',
    repeatPassword: '',
    email: '',
    firstName: '',
    lastName: '',
  });

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleRegisterForm = async (e) => {
    e.preventDefault();
    try {
      const response = await request('POST', '/api/v1/auth/register', user);
      setUserData(response.data);
      setUser({
        login: '',
        password: '',
        repeatPassword: '',
        email: '',
        firstName: '',
        lastName: '',
      });
      toast.success('Sign up successfully');
      navigate('/');
      window.location.reload(); // TODO: Change to use Context
    } catch (error) {
      toast.error(error.response.data);
      console.log(error.response);
    }
  };

  const renderRegisterForm = () => {
    return (
      <form className='mb-3 mt-md-4' onSubmit={handleRegisterForm}>
        <h2 className='fw-bold mb-2 text-uppercase'>TalkTactics</h2>
        <div className='mb-3'>
          <label htmlFor='firstName' className='form-label'>
            First name
          </label>
          <input
            type='text'
            className='form-control'
            id='firstName'
            name='firstName'
            value={user.firstName}
            onChange={handleChange}
            placeholder='First name'
          />
        </div>
        <div className='mb-3'>
          <label htmlFor='lastName' className='form-label'>
            Last name
          </label>
          <input
            type='text'
            className='form-control'
            id='lastName'
            name='lastName'
            value={user.lastName}
            onChange={handleChange}
            placeholder='Last name'
          />
        </div>
        <div className='mb-3'>
          <label htmlFor='email' className='form-label'>
            Email
          </label>
          <input
            type='email'
            className='form-control'
            id='email'
            name='email'
            value={user.email}
            onChange={handleChange}
            placeholder='email@xyz.com'
          />
        </div>
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
            placeholder='Login'
          />
        </div>
        <div className='mb-3'>
          <label htmlFor='password' className='form-label '>
            Password
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
        <div className='mb-3'>
          <label htmlFor='repeatPassword' className='form-label'>
            Repeat password
          </label>
          <input
            type='password'
            className='form-control'
            id='repeatPassword'
            name='repeatPassword'
            value={user.repeatPassword}
            onChange={handleChange}
            placeholder='*******'
          />
        </div>
        <div className='d-grid'>
          <button className='btn btn-outline-dark' type='submit'>
            Sign up
          </button>
        </div>
      </form>
    );
  };

  return (
    <div className='justify-content-center align-items-center'>
      <div className='container-fluid bg-secondary p-4'>
        <div className='row d-flex justify-content-center'>
          <div className='col-12 col-md-8 col-lg-6'>
            <div className='card bg-white shadow-lg'>
              <div className='card-body p-5'>
                {renderRegisterForm()}
                <div>
                  <p className='mb-0 text-center'>
                    Do you have an account?{' '}
                    <Link to='/login' className='text-primary fw-bold'>
                      Sign in
                    </Link>
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignUp;
