import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { request } from '../../../api/AxiosHelper';

export default function EditUser() {
  const navigate = useNavigate();
  const url = '/admin?isCoursesDisplayed=true';
  const levels = [
    { value: 'Beginner' },
    { value: 'Intermediate' },
    { value: 'Advanced' },
  ];

  const { id } = useParams();

  const [course, setCourse] = useState({
    name: '',
    description: '',
    level: '',
  });

  const { name, description, level } = course;

  const onInputChange = (e) => {
    setCourse({ ...course, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    const loadCourse = async () => {
      try {
        const response = await request('GET', `/api/courses/${id}`);
        setCourse(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadCourse();
  }, [id]);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      if (name !== '' && description !== '' && level !== '') {
        await request('PUT', `/api/courses/${id}`, course);
        navigate(url);
        toast.success('Course edited successfully');
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
          <h2 className='text-center m-4'>Edit Course</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className='mb-3'>
              <label htmlFor='Name' className='form-label'>
                Name
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
              <label htmlFor='Description' className='form-label'>
                Description
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter task description'
                name='description'
                value={description}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Level' className='form-label'>
                Level
              </label>
              <select
                className='form-control'
                name='level'
                value={level}
                onChange={(e) => onInputChange(e)}>
                {levels.map((item) => (
                  <option key={item.id} value={item.value}>
                    {item.value}
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
