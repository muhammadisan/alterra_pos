import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { getStockAsync, addStockAsync, showStock } from "../features/stockSlice";

export default function Stock() {
    const stock = useSelector(showStock);
    const dispatch = useDispatch();
    const [newStock, setNewStock] = useState({
        name: "",
        description: "",
        priceAndStock: {
            price: 0,
            stock: 0
        },
        createdBy: ""
    });

    useEffect(() => {
        dispatch(getStockAsync())
    }, [])

    const addNewStock = () => {
        dispatch(addStockAsync(newStock));
    };

    return (
        <div className="App">
            <h1>Stock</h1>
        </div>
    );
}
