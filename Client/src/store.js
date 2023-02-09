import { configureStore } from '@reduxjs/toolkit'
import stockSlice from "./features/stockSlice";

export const store = configureStore({
    reducer: {
        stock: stockSlice
    }
})