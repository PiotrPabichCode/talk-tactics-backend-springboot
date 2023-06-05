import React from 'react';
import './Footer.css';

const Footer = () => {
  return (
    <footer className='footer bg-dark text-center text-lg-start'>
      <div className='text-center text-light p-3'>
        © 2023 Copyright:{' '}
        <a
          className='text-light'
          style={{ textDecoration: 'none' }}
          href='https://github.com/PiotrPabichCode'>
          Piotr Pabich
        </a>
      </div>
    </footer>
  );
};

export default Footer;
