import React from 'react';
import { Link } from 'react-router-dom';
import { getUserRole } from '../../api/AxiosHelper';

export default function Navbar() {
  const logoutUser = () => {
    localStorage.clear();
    window.location.reload(); // TODO: Change to use Context
  };
  const role = getUserRole();
  return (
    <div>
      <nav className='navbar navbar-expand-lg navbar-dark bg-dark'>
        <div className='container-fluid'>
          <Link className='navbar-brand' to='/'>
            TalkTactics
          </Link>
          <ul className='navbar-nav mr-auto'>
            {!role && (
              <>
                <li className='nav-item me-1'>
                  <Link className='btn btn-outline-light' to='/login'>
                    Login
                  </Link>
                </li>
                <li className='nav-item me-1'>
                  <Link className='btn btn-outline-light' to='/register'>
                    Register
                  </Link>
                </li>
              </>
            )}
            {role && (
              <>
                <li className='nav-item me-1'>
                  <Link className='btn btn-outline-light' to='/courseItems'>
                    Course items
                  </Link>
                </li>
                <li className='nav-item me-1'>
                  <Link
                    className='btn btn-outline-light'
                    to='/courseItemDetails'>
                    Courses item details
                  </Link>
                </li>
                <li className='nav-item me-1'>
                  <Link className='btn btn-outline-light' to='/courses'>
                    Courses
                  </Link>
                </li>
                <li className='nav-item me-1'>
                  <Link
                    className='btn btn-outline-light'
                    to={role === 'USER' ? '/user' : '/admin'}>
                    Account
                  </Link>
                </li>
                <li className='nav-item'>
                  <Link
                    className='btn btn-outline-light'
                    onClick={logoutUser}
                    to='/'>
                    Logout
                  </Link>
                </li>
              </>
            )}
          </ul>
          {/* <Link className="btn btn-outline-light" to="/adduser">
            Add User
          </Link> */}
        </div>
      </nav>
    </div>
  );
}
