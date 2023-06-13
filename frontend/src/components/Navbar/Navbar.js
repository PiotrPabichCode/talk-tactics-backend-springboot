import React from 'react';
import { Link } from 'react-router-dom';
import { getUserRole } from 'api/AxiosHelper';
import { useTranslation } from 'react-i18next';

export default function Navbar() {
  const { t } = useTranslation();
  const logoutUser = () => {
    localStorage.clear();
    window.location.reload(); // TODO: Change to use Context
  };
  const role = getUserRole();
  return (
    <div>
      <nav className='navbar navbar-expand-lg navbar-dark bg-dark'>
        <div className='container-fluid'>
          <Link className='navbar-brand' to='/'>
            TalkTactics
          </Link>
          <ul className='navbar-nav mr-auto'>
            {!role && (
              <>
                <li className='nav-item me-1'>
                  <Link className='btn btn-outline-light' to='/login'>
                    {t('nav.login')}
                  </Link>
                </li>
                <li className='nav-item me-1'>
                  <Link className='btn btn-outline-light' to='/register'>
                    {t('nav.register')}
                  </Link>
                </li>
              </>
            )}
            {role && (
              <>
                <li className='nav-item me-1'>
                  <Link className='btn btn-outline-light' to='/courses'>
                    {t('nav.courses')}
                  </Link>
                </li>
                <li className='nav-item me-1'>
                  <Link
                    className='btn btn-outline-light'
                    to={role === 'USER' ? '/user' : '/admin'}>
                    {t('nav.account')}
                  </Link>
                </li>
                <li className='nav-item'>
                  <Link
                    className='btn btn-outline-light'
                    onClick={logoutUser}
                    to='/'>
                    {t('nav.logout')}
                  </Link>
                </li>
              </>
            )}
          </ul>
        </div>
      </nav>
    </div>
  );
}
