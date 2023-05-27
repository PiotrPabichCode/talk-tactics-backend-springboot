import React, { useEffect, useState } from 'react';
import { getUsername, request } from '../../api/AxiosHelper';

const AccountDetails = () => {
  const [userDetails, setUserDetails] = useState({});

  useEffect(() => {
    const username = getUsername();
    console.log(username);
    const loadUserDetails = async () => {
      try {
        const response = await request(
          'GET',
          `/api/users/by/login/${username}`
        );
        console.log(response.data);
        setUserDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadUserDetails();
  }, []);

  return (
    <>
      <div className='container'>
        <div className='py-4'>
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
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default AccountDetails;
