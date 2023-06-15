import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.post['Content-type'] = 'application/json';

export const setUserData = (user) => {
  return window.localStorage.setItem('user_data', JSON.stringify(user));
};

export const getUserData = () => {
  var userData = window.localStorage.getItem('user_data');
  if (!userData) return null;
  return JSON.parse(userData);
};

export const clearUserData = () => {
  window.localStorage.removeItem('user_data');
};

export const getUserID = () => {
  return getUserData()?.id ?? null;
};

export const getAuthToken = () => {
  return getUserData()?.token ?? null;
};

export const getAuthRefreshToken = () => {
  return getUserData()?.refreshToken ?? null;
};

export const getUserRole = () => {
  return getUserData()?.role ?? null;
};

export const getUsername = () => {
  return getUserData()?.username ?? null;
};

export const request = async (method, url, data) => {
  let headers = {};
  if (getAuthToken() !== null && getAuthToken() !== 'null') {
    headers = { Authorization: `Bearer ${getAuthToken()}` };
  }

  try {
    const response = await axios({
      method: method,
      headers: headers,
      url: url,
      data: data,
    });

    return response;
  } catch (error) {
    if (error.response.status === 403) {
      try {
        console.log('Token expired - try refresh token');
        if (
          getAuthRefreshToken() !== null &&
          getAuthRefreshToken() !== 'null'
        ) {
          headers = { Authorization: `Bearer ${getAuthRefreshToken()}` };
        }

        // Request new token by passing refresh token
        const refreshToken = await axios({
          method: 'POST',
          headers: headers,
          url: '/api/auth/refresh-token',
          data: {
            login: getUsername(),
            refreshToken: getAuthRefreshToken(),
          },
        });

        setUserData(refreshToken.data);
        if (getAuthToken() !== null && getAuthToken() !== 'null') {
          headers = { Authorization: `Bearer ${getAuthToken()}` };
        }

        return axios({
          method: method,
          headers: headers,
          url: url,
          data: data,
        });
      } catch (error) {
        clearUserData();
        console.log('Refresh token has expired. Login required');
      }
    }
    throw error;
  }
};
