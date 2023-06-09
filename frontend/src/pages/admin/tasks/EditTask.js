import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { request } from '../../../api/AxiosHelper';
import { toast } from 'react-toastify';

const EditTask = () => {
  const navigate = useNavigate();
  const url = '/admin?isTasksDisplayed=true';
  const [courses, setCourses] = useState([]);
  const partsOfSpeech = [
    { value: 'Phrase' },
    { value: 'Verb' },
    { value: 'Adjective' },
  ];

  const { id } = useParams();

  const [task, setTask] = useState({
    name: '',
    word: '',
    partOfSpeech: '',
    description: '',
    course: {},
  });

  const { name, word, partOfSpeech, description, course } = task;

  useEffect(() => {
    const loadTask = async () => {
      try {
        const response = await request('GET', `/api/tasks/${id}`);
        setTask(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    const loadCourses = async () => {
      try {
        const response = await request('GET', '/api/courses');
        setCourses(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadTask();
    loadCourses();
  }, [id]);

  const onInputChange = (e) => {
    setTask({ ...task, [e.target.name]: e.target.value });
  };

  const onCourseChange = (e) => {
    const courseID = e.target.value;
    console.log(typeof courseID);
    const course = courses.find((course) => course.id === Number(courseID));
    setTask({ ...task, course: course });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      if (
        name !== '' &&
        word !== '' &&
        partOfSpeech !== '' &&
        description !== '' &&
        Object.keys(course).length !== 0
      ) {
        await request('PUT', `/api/tasks/${id}`, task);
        toast.success('Task edited successfully');
        navigate(url);
      } else {
        toast.error('Something went wrong');
      }
    } catch (error) {
      toast.error('Something went wrong');
      console.log(error);
    }
  };

  return (
    <div className='container-fluid'>
      <div className='row text-light'>
        <div className='col-md-6 offset-md-3 bg-dark opacity-100 border rounded p-4 mt-2 shadow position-relative'>
          <Link
            className='btn btn-primary position-absolute end-0 me-4'
            to={url}>
            Back
          </Link>
          <h2 className='text-center m-4'>Edit Task</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className='mb-3'>
              <label htmlFor='TaskName' className='form-label'>
                Task name
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter task name'
                name='name'
                value={name}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Word' className='form-label'>
                Word
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter word'
                name='word'
                value={word}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='PartOfSpeech' className='form-label'>
                Part of speech
              </label>
              <select
                className='form-control'
                name='partOfSpeech'
                value={partOfSpeech}
                onChange={(e) => onInputChange(e)}>
                {partsOfSpeech.map((item) => (
                  <option key={item.id} value={item.value}>
                    {item.value}
                  </option>
                ))}
              </select>
            </div>
            <div className='mb-3'>
              <label htmlFor='Description' className='form-label'>
                Description
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter description'
                name='description'
                value={description}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Course' className='form-label'>
                Course
              </label>
              <select
                className='form-control'
                name='course'
                value={course.id}
                onChange={(e) => onCourseChange(e)}>
                {courses.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.name}
                  </option>
                ))}
              </select>
            </div>
            <button type='submit' className='btn btn-outline-primary'>
              Submit
            </button>
            <Link className='btn btn-outline-danger mx-2' to={url}>
              Cancel
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditTask;
