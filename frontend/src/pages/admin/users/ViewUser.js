import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { useLoadUserDetails } from './hooks/useLoadUserDetails';

export default function ViewUser() {
  const url = '/admin?isUserDisplayed=true';
  const { id } = useParams();
  const userDetails = useLoadUserDetails(id);

  const renderUserDetails = () => {
    return (
      userDetails && (
        <div className='card mb-4'>
          <div className='card-header'>Account details</div>
          <div className='card-body'>
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>First name</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{userDetails.firstName}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Last name</p>
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
                <p className='mb-0'>Password</p>
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
                  {userDetails.role === 'ADMIN' ? 'True' : 'False'}
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
          Back
        </Link>
        <h2 className='text-center m-4 text-light'>User Details</h2>
        {renderUserDetails()}
      </div>
    </div>
  );
}
