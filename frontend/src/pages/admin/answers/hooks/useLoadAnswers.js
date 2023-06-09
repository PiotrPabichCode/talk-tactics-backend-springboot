import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';

const useLoadAnswers = () => {
  const [answers, setAnswers] = useState([]);

  useEffect(() => {
    const loadAnswers = async () => {
      try {
        const response = await request('GET', '/api/answers');
        setAnswers(response.data);
      } catch (error) {
        console.log(error);
      }
    };

    loadAnswers();
  }, []);

  return answers;
};

export default useLoadAnswers;
