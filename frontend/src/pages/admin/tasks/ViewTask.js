import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { useTaskDetails } from './hooks/useTaskDetails';

const ViewTask = () => {
  const url = '/admin?isTasksDisplayed=true';
  const { id } = useParams();
  const taskDetails = useTaskDetails(id);

  const renderTaskDetails = () => {
    return (
      taskDetails.course && (
        <div className='card mb-4'>
          <div className='card-header'>Task details</div>
          <div className='card-body'>
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Task name</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{taskDetails.name}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Word name</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{taskDetails.word}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Part of speech</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{taskDetails.partOfSpeech}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Course name</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{taskDetails.course.name}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Description</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{taskDetails.description}</p>
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
        <h2 className='text-center m-4 text-light'>Task Details</h2>
        {renderTaskDetails()}
      </div>
    </div>
  );
};

export default ViewTask;
