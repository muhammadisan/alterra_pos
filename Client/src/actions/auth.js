import {
    REGISTER_SUCCESS,
    REGISTER_FAIL,
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT,
    SET_MESSAGE,
} from "./types";

import http from "../http";

export const register = (name, position, phone, username, password, role) => (dispatch) => {
    return http.post(`/auth/register`, { name, position, phone, username, password, role }).then((response) => {
        if (response.data.success) {
            dispatch({
                type: REGISTER_SUCCESS,
            });

            dispatch({
                type: SET_MESSAGE,
                payload: response.data.message,
            });
        } else {
            dispatch({
                type: REGISTER_FAIL,
            });

            dispatch({
                type: SET_MESSAGE,
                payload: response.data.message,
            });
        }

        return response.data;
    });
};

export const login = (username, password) => (dispatch) => {
    return http.post(`/auth/login`, { username, password }).then((response) => {

        if (response.data.success && response.data.data.token) {
            localStorage.setItem("user", JSON.stringify(response.data.data));
            dispatch({
                type: LOGIN_SUCCESS,
                payload: { user: response.data.data },
            });
        } else {
            dispatch({
                type: LOGIN_FAIL,
            });

            dispatch({
                type: SET_MESSAGE,
                payload: response.data.message,
            });
        }

        return response.data;
    });
};

export const logout = () => (dispatch) => {
    localStorage.removeItem("user");

    dispatch({
        type: LOGOUT,
    });
};
