import axios from 'axios';
import { jwtDecode } from "jwt-decode";


export const getAuthToken = () => {
    return window.localStorage.getItem('auth_token');
};

export const setAuthHeader = (token) => {
    if (token !== null) {
      window.localStorage.setItem("auth_token", token);
    } else {
      window.localStorage.removeItem("auth_token");
    }
};

export const getRole = () => {
    const token = getAuthToken();
    if (!token) {
        return null;
    }
    
    try {
        console.log(jwtDecode(token));
        return jwtDecode(token).role;
    }
    catch (error) {
        console.error("Błąd dekodowania tokena:", error);
        return null;
    }
}

export const getDataFromToken = () => {
    const token = getAuthToken();
    if (!token) {
        return null;
    }
    
    try {
        return jwtDecode(token);
    }
    catch (error) {
        console.error("Błąd dekodowania tokena:", error);
        return null;
    }
};



axios.defaults.baseURL = 'http://localhost:8080';
// axios.defaults.headers.post['Content-Type'] = 'application/json';

export const request = (method, url, data, params, config = { responseType: 'json'}) => {

    let headers = {};
    if (getAuthToken() !== null && getAuthToken() !== "null") {
        headers = {'Authorization': `Bearer ${getAuthToken()}`};
    }

    return axios({
        method: method,
        url: url,
        headers: headers,
        data: data,
        params: params,
        ...config
    });
};