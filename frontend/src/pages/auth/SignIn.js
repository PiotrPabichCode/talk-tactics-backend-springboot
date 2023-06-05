import React, { useState } from 'react';
import './LoginRegister.css';
import { Link, useNavigate } from 'react-router-dom';
import { request, setUserData } from '../../api/AxiosHelper';
import { toast } from 'react-toastify';

const SignIn = () => {
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
      console.log(user);
      const response = await request('POST', '/api/v1/auth/authenticate', user);
      console.log(response.data);
      setUserData(response.data);
      toast.success('Sign in successfully');
      setUser({ login: '', password: '' });
      navigate('/');
      window.location.reload(); // TODO: Change to use Context
    } catch (error) {
      toast.error('Something went wrong');
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
            placeholder='Username'
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
        <div className='d-grid'>
          <button className='btn btn-outline-dark' type='submit'>
            Sign in
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
                      Don't have an account?{' '}
                      <Link to='/register' className='text-primary fw-bold'>
                        Sign up
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
