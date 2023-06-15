import React from 'react';
import './Home.css';
import { Link } from 'react-router-dom';
import home1 from 'assets/home-img1.svg';
import home2 from 'assets/home-img2.svg';
import { useTranslation } from 'react-i18next';

function Home() {
  const { t } = useTranslation();

  return (
    <>
      <div className='homecontainer'>
        <div className='description1 align-items-center'>
          <h1>{t('home.question')}</h1>
          <p>{t('home.welcome_message')}</p>
          <Link to={'/register'} className='text-light text-decoration-none'>
            <button className='btn btn-primary'>{t('home.start_now')}</button>
          </Link>
        </div>
        <img src={home1} alt='Background 1' />
      </div>
      <div className='homecontainer'>
        <img src={home2} alt='Background 2' />
        <div className='description1'>
          <p id='home2desc'>{t('home.information')}</p>
        </div>
      </div>
    </>
  );
}

export default Home;
