import { useState, useEffect } from "react";
import DataTable from 'react-data-table-component';
import "./stock.css"
import http from "../http"

export default function Stock() {
    const [stocks, setStocks] = useState([]);
    const [categories, setCategories] = useState([]);
    const [data, setData] = useState([]);

    const [newStock, setNewStock] = useState({
        name: "",
        description: "",
        priceAndStock: {
            price: 0,
            stock: 0
        },
        createdBy: ""
    });
    const [newCategoryId, setNewCategoryId] = useState(null);

    const columns = [
        {
            name: 'No',
            selector: row => row.no,
            sortable: true,
        },
        {
            name: 'Category',
            selector: row => row.category,
            sortable: true,
        },
        {
            name: 'Name',
            selector: row => row.name,
            sortable: true,
        },
        {
            name: 'Price',
            selector: row => row.price,
            sortable: true,
        },
        {
            name: 'Stock',
            selector: row => row.stock,
            sortable: true,
        }
    ]

    const conditionalRowStyles = [
        {
            when: row => row.category == 'Food',
            style: {
                backgroundColor: 'rgba(63, 195, 128, 0.9)',
                color: 'white',
                '&:hover': {
                    cursor: 'pointer',
                },
            },
        },
        {
            when: row => row.category == 'Drink',
            style: {
                backgroundColor: 'rgba(248, 148, 6, 0.9)',
                color: 'white',
                '&:hover': {
                    cursor: 'pointer',
                },
            },
        },
        {
            when: row => row.category == 'Meals',
            style: {
                backgroundColor: 'rgba(242, 38, 19, 0.9)',
                color: 'white',
                '&:hover': {
                    cursor: 'not-allowed',
                },
            },
        },
    ];

    useEffect(() => {
        http.get(`/products`).then((res) => {
            setStocks(res.data)
            let stock = res.data.map((stock, index) => {
                return {
                    no: index + 1,
                    category: stock.category.name,
                    name: stock.name,
                    price: stock.priceAndStock.price,
                    stock: stock.priceAndStock.stock
                }
            })

            setData(stock)
        })

        http.get(`categories`).then((res) => { setCategories(res.data) })
    }, [])

    const handleChange = e => {
        const { name, value } = e.target
        if (name == "name" || name == "description")
            setNewStock(prevState => ({
                ...prevState,
                [name]: value,
            }));
        else
            setNewStock(prevState => ({
                ...prevState,
                priceAndStock: {
                    ...prevState.priceAndStock,
                    [name]: value
                }
            }));
    }

    const addNewStock = () => {
        if (newStock.name && newCategoryId) {
            http.post(`/products/${newCategoryId}`, newStock).then((res) => {
                setStocks([...stocks, res.data])
                setData([...data, {
                    no: data.length + 1,
                    category: res.data.category.name,
                    name: res.data.name,
                    price: res.data.priceAndStock.price,
                    stock: res.data.priceAndStock.stock
                }])
            })
        }
    };

    return (
        <div className="App" style={{width: "100%"}}>
            {/* <input
                value={newStock.name}
                type="text"
                onChange={(handleChange)}
                name="name"
            />
            <input
                value={newStock.description}
                type="text"
                onChange={handleChange}
                name="description"
            />
            <input
                value={newStock.priceAndStock.price}
                type="number"
                onChange={handleChange}
                name="price"
            />
            <input
                value={newStock.priceAndStock.stock}
                type="number"
                onChange={handleChange}
                name="stock"
            />
            <button onClick={addNewStock}>add product</button> */}

            <DataTable
                title="Product Stocks"
                columns={columns}
                data={data}
                conditionalRowStyles={conditionalRowStyles}
            />
        </div>
    );
}
