import requests from 'api/config';
import { authInstance, defaultInstance } from '../axios';

// ν νΌλ GET
export const get_main_trades = async pageParam => {
  try {
    const { data } = await authInstance.get(requests.GET_MAIN_TRADE(pageParam));
    return data;
  } catch (error) {
    throw error;
  }
};
