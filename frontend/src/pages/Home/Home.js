import React from 'react';
import './Home.css';
import { Link } from 'react-router-dom';
import home1 from '../../assets/home-img1.svg';
import home2 from '../../assets/home-img2.svg';

function Home() {
  return (
    <>
      <div className='homecontainer bg-secondary'>
        <div className='description1 align-items-center'>
          <h1>
            Chcesz nauczyć się języka angielskiego, ale nie wiesz od czego
            zacząć?
          </h1>
          <p>
            Witaj w TalkTactics - twoim źródle wiedzy na temat nauki języków
            obcych!
          </p>
          <Link to={'/register'} className='text-light text-decoration-none'>
            <button className='btn btn-primary'>Start now!</button>
          </Link>
        </div>
        <img src={home1} alt='Background 1' />
      </div>
      <div className='homecontainer bg-secondary'>
        <img src={home2} alt='Background 2' />
        <div className='description1'>
          <p id='home2desc'>
            Nasza strona oferuje kompleksowe materiały edukacyjne, kursy
            językowe online oraz cenne wskazówki i porady dotyczące nauki języka
            angielskiego. Niezależnie od Twojego poziomu zaawansowania, nasza
            strona pomoże Ci osiągnąć Twoje cele językowe!
          </p>
        </div>
      </div>
    </>
  );
}

export default Home;
