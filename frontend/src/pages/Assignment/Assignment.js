import React from "react";
import "./Assignment.css";

const Assignment = () => {
  const tempData = [
    { word: "home", partOfSpeech: "noun", definiton: "ala ma kota" },
    { word: "home1", partOfSpeech: "noun1", definiton: "ala ma kota1" },
    { word: "home2", partOfSpeech: "noun2", definiton: "ala ma kota2" },
  ];

  const generateTableHeader = () => {
    return (
      <thead>
        <tr>
          <th>Word</th>
          <th>Part of speech</th>
          <th>Definition</th>
        </tr>
      </thead>
    );
  };

  const generateSingleTask = (task) => {
    return (
      <>
        <td>{task.word}</td>
        <td>{task.partOfSpeech}</td>
        <td>{task.definiton}</td>
      </>
    );
  };

  function generateTableContent() {
    return (
      <tbody>
        {tempData.map((task) => {
          <tr key={task.id}>{generateSingleTask(task)}</tr>;
        })}
      </tbody>
    );
  }

  function generateTaskTable() {
    return (
      <div>
        <div className="table table-responsive task-content_wrapper">
          {generateTableHeader()}
          {generateTableContent()}
        </div>
      </div>
    );
  }

  return (
    <div className="task_wrapper">
      <div className="task">
        <h1>Tytuł zadania</h1>
        <div className="task_line"></div>
        <h2>Definicje</h2>
        {generateTaskTable()}
        <div className="task_line"></div>
        <h2>Odpowiedzi</h2>
        <p>4. Odpowiedź</p>
      </div>
    </div>
  );
};

export default Assignment;
