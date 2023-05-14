import React from "react";
import "./Home.css";
import { Link } from "react-router-dom";

function Home() {
  return (
    <>
      <div className="home_wrapper">
        <div className="homecontainer">
          <div className="description1">
            <h1>
              Chcesz nauczyć się języka angielskiego, ale nie wiesz od czego
              zacząć?
            </h1>
            <p>
              Witaj w TalkTactics - twoim źródle wiedzy na temat nauki języków
              obcych!
            </p>
            <button>
              <Link to={"/login"} style={{ textDecoration: "none" }}>
                Start now!
              </Link>
            </button>
          </div>
          <img src="home1.svg" alt=""></img>
        </div>
        <div className="homecontainer">
          <img src="home2.svg" alt=""></img>
          <div className="description1">
            <p id="home2desc">
              Nasza strona oferuje kompleksowe materiały edukacyjne, kursy
              językowe online oraz cenne wskazówki i porady dotyczące nauki
              języka angielskiego. Niezależnie od Twojego poziomu zaawansowania,
              nasza strona pomoże Ci osiągnąć Twoje cele językowe!
            </p>
          </div>
        </div>
      </div>
    </>
  );
}

export default Home;
