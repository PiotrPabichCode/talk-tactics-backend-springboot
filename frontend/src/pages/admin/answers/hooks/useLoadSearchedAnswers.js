import { useEffect, useState } from 'react';
import { request } from '../../../../api/AxiosHelper';
import { toast } from 'react-toastify';

const useLoadSearchedAnswers = (username) => {
  const [searchedAnswers, setSearchedAnswers] = useState([]);

  useEffect(() => {
    if (username) {
      const loadSearchedAnswers = async () => {
        try {
          const response = await request(
            'GET',
            `/api/answers/username/${username}`
          );
          setSearchedAnswers(response.data);
        } catch (error) {
          toast.error('Unable to fetch data', {
            autoClose: 1000,
          });
        }
      };
      loadSearchedAnswers();
    } else {
      setSearchedAnswers([]);
    }
  }, [username]);

  return searchedAnswers;
};

export default useLoadSearchedAnswers;
