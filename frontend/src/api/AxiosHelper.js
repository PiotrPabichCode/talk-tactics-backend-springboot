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

export const getAuthToken = () => {
  return getUserData()?.token ?? null;
};

export const getUserRole = () => {
  return getUserData()?.role ?? null;
};

export const getUsername = () => {
  return getUserData()?.username ?? null;
};

export const request = (method, url, data) => {
  let headers = {};
  if (getAuthToken() !== null && getAuthToken() !== 'null') {
    headers = { Authorization: `Bearer ${getAuthToken()}` };
  }

  return axios({
    method: method,
    headers: headers,
    url: url,
    data: data,
  });
};
