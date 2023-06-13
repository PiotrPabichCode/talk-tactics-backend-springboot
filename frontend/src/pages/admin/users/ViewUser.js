import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { useLoadUserDetails } from './hooks/useLoadUserDetails';
import { useTranslation } from 'react-i18next';

export default function ViewUser() {
  const { t } = useTranslation();
  const url = '/admin?isUserDisplayed=true';
  const { id } = useParams();
  const userDetails = useLoadUserDetails(id);

  const renderUserDetails = () => {
    return (
      userDetails && (
        <div className='card mb-4'>
          <div className='card-header'>{t('admin.users.view_user.title')}</div>
          <div className='card-body'>
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>{t('admin.users.view_user.first_name')}</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{userDetails.firstName}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>{t('admin.users.view_user.last_name')}</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{userDetails.lastName}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Email</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{userDetails.email}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Login</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>********</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>{t('password')}</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>********</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Admin</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>
                  {userDetails.role === 'ADMIN'
                    ? t('admin.users.view_user.true')
                    : t('admin.users.view_user.false')}
                </p>
              </div>
            </div>
            <hr />
          </div>
        </div>
      )
    );
  };

  return (
    <div className='container-fluid p-4'>
      <div className='container-fluid rounded p-4 shadow bg-dark position-relative'>
        <Link className='btn btn-primary position-absolute end-0 me-4' to={url}>
          {t('admin.users.view_user.back')}
        </Link>
        <h2 className='text-center m-4 text-light'>
          {t('admin.users.view_user.title')}
        </h2>
        {renderUserDetails()}
      </div>
    </div>
  );
}
