import axios from 'axios';
import requests from './config';

const BASE_URL = requests.base_url;

// 인증이 필요없는 axios 인스턴스
const axiosApi = (url, options) => {
  const instance = axios.create({ baseURL: url, ...options });
  return instance;
};

// 인증이 필요한 axios 인스턴스
const axiosAuthApi = (url, options) => {
  // const token = localStorage.getItem('token');
  // 임시토큰
  const token =
    'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWR4IjozLCJyb2xlIjoiVFlQRTUiLCJpYXQiOjE2NzUwNjA1MzQsImV4cCI6MTY3NzY1MjUzNH0.45BdBuqZeNQFO8zuDGYgCK2laTVvgJbIwcMqzfKQuNU';
  const instance = axios.create({
    baseURL: url,
    headers: { Authorization: token },
    ...options,
  });
  return instance;
};

export const defaultInstance = axiosApi(BASE_URL);
export const authInstance = axiosAuthApi(BASE_URL);
