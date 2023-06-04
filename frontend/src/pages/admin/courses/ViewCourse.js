import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { request } from '../../../api/AxiosHelper';

export default function ViewUser() {
  const url = '/admin?isCoursesDisplayed=true';
  const [courseDetails, setCourseDetails] = useState({});

  const { id } = useParams();

  useEffect(() => {
    const loadCourse = async () => {
      try {
        const response = await request('GET', `/api/courses/${id}`);
        setCourseDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadCourse();
  }, [id]);

  return (
    <div className='container-fluid p-4 bg-secondary'>
      <div className='row'>
        <div className='container-fluid rounded p-4 shadow bg-dark position-relative'>
          <Link
            className='btn btn-primary position-absolute end-0 me-4'
            to={url}>
            Back
          </Link>
          <h2 className='text-center m-4 text-light'>Course Details</h2>
          <div className='card mb-4'>
            <div className='card-header'>Course details</div>
            <div className='card-body'>
              <div className='row'>
                <div className='col-sm-3'>
                  <p className='mb-0'>Course name</p>
                </div>
                <div className='col-sm-9'>
                  <p className='text-muted mb-0'>{courseDetails.name}</p>
                </div>
              </div>
              <hr />
              <div className='row'>
                <div className='col-sm-3'>
                  <p className='mb-0'>Level</p>
                </div>
                <div className='col-sm-9'>
                  <p className='text-muted mb-0'>{courseDetails.level}</p>
                </div>
              </div>
              <hr />
              <div className='row'>
                <div className='col-sm-3'>
                  <p className='mb-0'>Description</p>
                </div>
                <div className='col-sm-9'>
                  <p className='text-muted mb-0'>{courseDetails.description}</p>
                </div>
              </div>
              <hr />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
