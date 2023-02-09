import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
const API_URL = "http://localhost:8080";

export const todoSlice = createSlice({
    name: "stock",
    initialState: {
        data: []
    },
    reducers: {
        addStock: (state, action) => {
            state.data.push(action.payload);
        },
        getStock: (state, action) => {
            state.data = [action.payload];
        }
    }
});

export const getStockAsync = () => async (dispatch) => {
    try {
        const response = await axios.get(`${API_URL}/products`);
        dispatch(getStock(response.data));
    } catch (err) {
        throw new Error(err);
    }
};

export const addStockAsync = (data) => async (dispatch) => {
    try {
        const response = await axios.post(`${API_URL}/products`, data);
        dispatch(addStock(response.data));
    } catch (err) {
        throw new Error(err);
    }
};


export const showStock = (state) => state.stock.data;

export const { addStock, getStock } = todoSlice.actions;
export default todoSlice.reducer;
