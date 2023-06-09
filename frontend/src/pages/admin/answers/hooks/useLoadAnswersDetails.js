import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';

const useLoadAnswerDetails = (id) => {
  const [answerDetails, setAnswerDetails] = useState({});

  useEffect(() => {
    const loadAnswer = async () => {
      try {
        const response = await request('GET', `/api/answers/${id}`);
        setAnswerDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };

    loadAnswer();
  }, [id]);

  return answerDetails;
};

export default useLoadAnswerDetails;
