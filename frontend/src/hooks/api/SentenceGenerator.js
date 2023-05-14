import React, { useEffect, useState } from "react";

const SentenceGenerator = () => {
  const [data, setData] = useState(null);
  const [partOfSpeech, setPartOfSpeech] = useState("");
  const [word, setWord] = useState("");
  const [definition, setDefiniton] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          "https://api.dictionaryapi.dev/api/v2/entries/en/hello"
        );
        const json = await response.json();
        setData(json[0]);
        console.log(json[0]);
        setWord(data.word);
        setPartOfSpeech(data.meanings[0].partOfSpeech);
        setDefiniton(data.meanings[0].definitions[0].definition);
      } catch (error) {
        console.log(error);
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      {data ? (
        <div>
          <h2>{word}</h2>
          <p>{partOfSpeech}</p>
          <p>{definition}</p>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default SentenceGenerator;
