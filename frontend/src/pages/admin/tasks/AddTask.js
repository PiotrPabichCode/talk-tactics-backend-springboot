import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { request } from '../../../api/AxiosHelper';
import { toast } from 'react-toastify';

export default function AddTask() {
  const navigate = useNavigate();
  const url = '/admin?isTasksDisplayed=true';
  const [courses, setCourses] = useState([]);
  const partsOfSpeech = [
    { value: 'Phrase' },
    { value: 'Verb' },
    { value: 'Adjective' },
  ];

  const [task, setTask] = useState({
    name: '',
    word: '',
    partOfSpeech: '',
    description: '',
    course: {},
  });

  const { name, word, partOfSpeech, description, course } = task;

  useEffect(() => {
    const loadCourses = async () => {
      try {
        const response = await request('GET', '/api/courses');
        setCourses(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadCourses();
  }, []);

  const onInputChange = (e) => {
    setTask({ ...task, [e.target.name]: e.target.value });
  };

  const onCourseChange = (e) => {
    const courseID = Number(e.target.value);
    if (courseID !== 0) {
      const course = courses.find((course) => course.id === courseID);
      setTask({ ...task, course: course });
    } else {
      setTask({ ...task, course: {} });
    }
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
        await request('POST', '/api/task', task);
        toast.success('Added new task successfully');
        navigate(url);
      } else {
        toast.error('Something went wrong');
      }
    } catch (error) {
      console.log(error);
      toast.error('Something went wrong');
    }
  };

  return (
    <div className='container-fluid pb-3 bg-secondary text-light'>
      <div className='row'>
        <div className='col-md-6 offset-md-3 bg-dark border rounded p-4 mt-2 shadow position-relative'>
          <Link
            className='btn btn-primary position-absolute end-0 me-4'
            to={url}>
            Back
          </Link>
          <h2 className='text-center m-4'>Add new task</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className='mb-3'>
              <label htmlFor='Name' className='form-label'>
                Name
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter name'
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
                defaultValue={''}
                className='form-control'
                name='partOfSpeech'
                value={partOfSpeech}
                onChange={(e) => onInputChange(e)}>
                <option value={''}>Choose part of speech...</option>
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
                defaultValue={0}
                className='form-control'
                name='course'
                value={course.id}
                onChange={(e) => onCourseChange(e)}>
                <option value={0}>Choose course...</option>
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
}
