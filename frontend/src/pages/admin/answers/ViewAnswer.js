import React from 'react';
import { Link, useParams } from 'react-router-dom';
import useLoadAnswerDetails from './hooks/useLoadAnswersDetails';
import { convertFinishTime } from './utils/convertFinishTime';

export default function ViewAnswer() {
  const url = '/admin?isAnswersDisplayed=true';
  const { id } = useParams();
  const answerDetails = useLoadAnswerDetails(id);

  const renderAnswerDetails = () => {
    return (
      answerDetails &&
      answerDetails.task &&
      answerDetails.user && (
        <div className='card mb-4'>
          <div className='card-header'>Answer details</div>
          <div className='card-body'>
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Course name</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>
                  {answerDetails.task.course.name}
                </p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Task name</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{answerDetails.task.name}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Answer</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{answerDetails.content}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>User name</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>
                  {answerDetails.user.firstName +
                    ' ' +
                    answerDetails.user.lastName +
                    ' / ' +
                    answerDetails.user.login}
                </p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>Finish time</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>
                  {convertFinishTime(answerDetails.finishTime)}
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
        <h2 className='text-center m-4 text-light'>Answer Details</h2>
        {renderAnswerDetails()}
      </div>
    </div>
  );
}
