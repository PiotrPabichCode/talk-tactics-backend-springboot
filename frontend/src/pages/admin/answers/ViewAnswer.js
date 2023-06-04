import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { request } from '../../../api/AxiosHelper';

export default function ViewAnswer() {
  const url = '/admin?isAnswersDisplayed=true';
  const [answerDetails, setAnswerDetails] = useState({});

  const { id } = useParams();

  useEffect(() => {
    const loadAnswer = async () => {
      const response = await request('GET', `/api/answers/${id}`);
      console.log(response.data);
      setAnswerDetails(response.data);
    };
    loadAnswer();
  }, [id]);

  const convertFinishTime = () => {
    const time = answerDetails.finishTime;
    const year = time[0];
    const month = time[1] - 1;
    const day = time[2];
    const hours = time[3];
    const minutes = time[4];
    const seconds = time[5];

    const formattedTime = `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    return formattedTime;
  };

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
                <p className='text-muted mb-0'>{convertFinishTime()}</p>
              </div>
            </div>
            <hr />
          </div>
        </div>
      )
    );
  };

  return (
    <div className='container-fluid p-4 bg-secondary'>
      <div className='row'>
        <div className='container-fluid rounded p-4 shadow bg-dark position-relative'>
          <Link
            className='btn btn-primary position-absolute end-0 me-4'
            to={url}>
            Back
          </Link>
          <h2 className='text-center m-4 text-light'>Answer Details</h2>
          {renderAnswerDetails()}
        </div>
      </div>
    </div>
  );
}
