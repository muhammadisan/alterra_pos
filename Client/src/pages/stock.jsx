import React, { useState, useEffect } from "react";
import DataTable from 'react-data-table-component';
import FilterComponent from "../components/FilterComponent";
import "./stock.css"
import http from "../http"
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Modal from '@mui/material/Modal';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Button from '@mui/material/Button';
import DeleteForeverOutlinedIcon from '@mui/icons-material/DeleteForeverOutlined';
import Edit from '@mui/icons-material/Edit';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

export default function Stock() {
    const [stocks, setStocks] = useState([]);
    const [categories, setCategories] = useState([]);
    const [data, setData] = useState([]);
    const [filterText, setFilterText] = React.useState('');
    const [resetPaginationToggle, setResetPaginationToggle] = React.useState(false);
    const [newStock, setNewStock] = useState({
        name: "",
        description: "",
        priceAndStock: {
            price: 0,
            stock: 0
        },
        createdBy: ""
    });
    const [newCategoryId, setNewCategoryId] = useState('');
    const [modalAddShow, setModalAddShow] = useState(false);
    const [modalEditShow, setModalEditShow] = useState(false);
    const [modalDeleteShow, setModalDeleteShow] = useState(false);

    const columns = [
        {
            name: 'Category',
            selector: row => row.category,
            sortable: true,
            width: '10%',
        },
        {
            name: 'Name',
            selector: row => row.name,
            sortable: true,
            width: '30%',
        },
        {
            name: 'Description',
            selector: row => row.description,
            sortable: true,
            width: '30%',
        },
        {
            name: 'Price',
            selector: row => row.price,
            sortable: true,
            width: '10%',
        },
        {
            name: 'Stock',
            selector: row => row.stock,
            sortable: true,
            width: '10%',
        },
        {
            name: 'Actions',
            cell: (row) =>
                <div style={{ display: "flex" }} >
                    <Button onClick={() => setModalEditShow(true)} color="warning"><Edit></Edit></Button>
                    <Button onClick={() => setModalDeleteShow(true)} color="error"><DeleteForeverOutlinedIcon></DeleteForeverOutlinedIcon></Button>
                </div >,
            ignoreRowClick: true,
            allowOverflow: true,
            button: true,
            width: '10%',
        },
    ]

    const filteredItems = data.filter(
        item => item.name && item.name.toLowerCase().includes(filterText.toLowerCase()),
    );
    const subHeaderComponentMemo = React.useMemo(() => {
        const handleClear = () => {
            if (filterText) {
                setResetPaginationToggle(!resetPaginationToggle);
                setFilterText('');
            }
        };

        return (
            <FilterComponent onFilter={e => setFilterText(e.target.value)} onClear={handleClear} filterText={filterText} />
        );
    }, [filterText, resetPaginationToggle]);

    const actions = (<Button variant="contained" color="success" onClick={() => setModalAddShow(true)}>Add New Product</Button>);
    const conditionalRowStyles = [
        {
            when: row => row.category == 'Food',
            style: {
                backgroundColor: '#9c27b0',
                color: 'white',
                '&:hover': {
                    cursor: 'pointer',
                },
            },
        },
        {
            when: row => row.category == 'Drink',
            style: {
                backgroundColor: '#ba68c8',
                color: 'white',
                '&:hover': {
                    cursor: 'pointer',
                },
            },
        },
        {
            when: row => row.category == 'Meals',
            style: {
                backgroundColor: '#7b1fa2',
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
                    description: stock.description,
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
                    product_id: res.data.id,
                    no: data.length + 1,
                    category: res.data.category.name,
                    name: res.data.name,
                    price: res.data.priceAndStock.price,
                    stock: res.data.priceAndStock.stock
                }])
            })
            setModalAddShow(false)
        }
    };

    return (
        <div className="App" style={{ width: "100%" }}>
            <DataTable
                title="Product Stocks"
                columns={columns}
                defaultSortField="Category"
                data={filteredItems}
                conditionalRowStyles={conditionalRowStyles}
                subHeaderComponent={subHeaderComponentMemo}
                actions={actions}
                pagination
                subHeader
                persistTableHead
            />

            <Modal
                open={modalAddShow}
                onClose={() => setModalAddShow(false)}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <div style={{ textAlign: "center", fontSize: "20px" }}>New Product</div>
                    <FormControl variant="filled" sx={{ m: 1, minWidth: 120, display: "flex" }}>
                        <InputLabel id="select-category-label">Category</InputLabel>
                        <Select
                            style={{ margin: "10px" }}
                            labelId="select-category-label"
                            id="select-category"
                            value={newCategoryId}
                            onChange={(e) => setNewCategoryId(e.target.value)}
                        >
                            {categories.map((category) => {
                                return (<MenuItem key={category.id} value={category.id}>{category.name}</MenuItem>)
                            })}
                        </Select>
                        <TextField style={{ margin: "10px" }} onChange={handleChange} value={newStock.name} name="name" label="Name" variant="filled" />
                        <TextField style={{ margin: "10px" }} onChange={handleChange} value={newStock.description} name="description" label="Description" variant="filled" multiline rows={2} />
                        <div style={{ display: "flex" }}>
                            <div><TextField style={{ margin: "10px" }} onChange={handleChange} value={newStock.priceAndStock.price} name="price" label="Price" variant="filled" type="number" /></div>
                            <div><TextField style={{ margin: "10px" }} onChange={handleChange} value={newStock.priceAndStock.stock} name="stock" label="Stock" variant="filled" type="number" /></div>
                        </div>
                    </FormControl>
                    <Button variant="contained" sx={{ float: "right" }} onClick={addNewStock} color="success">ADD</Button>
                </Box>
            </Modal>

            <Modal
                open={modalAddShow}
                onClose={() => setModalAddShow(false)}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <div style={{ textAlign: "center", fontSize: "20px" }}>New Product</div>
                    <FormControl variant="filled" sx={{ m: 1, minWidth: 120, display: "flex" }}>
                        <InputLabel id="select-category-label">Category</InputLabel>
                        <Select
                            style={{ margin: "10px" }}
                            labelId="select-category-label"
                            id="select-category"
                            value={newCategoryId}
                            onChange={(e) => setNewCategoryId(e.target.value)}
                        >
                            {categories.map((category) => {
                                return (<MenuItem key={category.id} value={category.id}>{category.name}</MenuItem>)
                            })}
                        </Select>
                        <TextField style={{ margin: "10px" }} onChange={handleChange} value={newStock.name} name="name" label="Name" variant="filled" />
                        <TextField style={{ margin: "10px" }} onChange={handleChange} value={newStock.description} name="description" label="Description" variant="filled" multiline rows={2} />
                        <div style={{ display: "flex" }}>
                            <div><TextField style={{ margin: "10px" }} onChange={handleChange} value={newStock.priceAndStock.price} name="price" label="Price" variant="filled" type="number" /></div>
                            <div><TextField style={{ margin: "10px" }} onChange={handleChange} value={newStock.priceAndStock.stock} name="stock" label="Stock" variant="filled" type="number" /></div>
                        </div>
                    </FormControl>
                    <Button variant="contained" sx={{ float: "right" }} onClick={addNewStock} color="success">ADD</Button>
                </Box>
            </Modal>
        </div>
    );
}
