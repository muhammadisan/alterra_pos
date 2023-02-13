import React from "react";
import { useEffect } from "react";
import axios from "axios";
import { useState } from "react";
import { BiPlus, BiMinus } from "react-icons/bi";

const Home = () => {
  const [dataFood, setDataFood] = useState([]);
  const [selectedProducts, setSelectedProducts] = useState([]);

  useEffect(() => {
    async function doGetRequest() {
      let res = await axios.get("http://localhost:8080/products");
      let dataFood = res.data;
      setDataFood(dataFood);
    }

    doGetRequest();
  }, []);

  function addProduct(product) {
    const newProduct = {
      id: product.id,
      name: product.name,
      amount: 1,
      stock: product.priceAndStock.stock,
      price: product.priceAndStock.price,
    };

    const productIndex = selectedProducts.findIndex(
      (item) => item.id === product.id
    );
    // console.log(productIndex, "a");
    if (productIndex === -1) {
      setSelectedProducts((prev) => [
        ...prev,
        { ...newProduct, stock: newProduct.stock - 1 },
      ]);
      return;
    }

    setSelectedProducts((prev) =>
      prev.map((item, index) => {
        if (productIndex === index) {
          return {
            ...item,
            amount: item.amount + 1,
            price: item.price + product.priceAndStock.price,
            stock: item.stock - 1,
          };
        }
        return item;
      })
    );
  }
  // console.log(selectedProducts);

  function reduceStock(product) {
    setSelectedProducts((prev) => {
      if (product.amount === 1) {
        return prev.filter((item) => item.id !== product.id);
      }
      return prev.map((item) => {
        if (item.id === product.id) {
          return {
            ...item,
            amount: item.amount - 1,
            price: item.price - product.price / product.amount,
            stock: item.stock + 1,
          };
        }
        return item;
      });
    });
  }

  function addStock(product) {
    setSelectedProducts((prev) => {
      return prev.map((item) => {
        if (item.stock > 0 && item.id === product.id) {
          return {
            ...item,
            amount: item.amount + 1,
            price: item.price + product.price / product.amount,
            stock: item.stock - 1,
          };
        }
        return item;
      });
    });
  }

  function formatRupiah(angka, prefix) {
    var number_string = angka.replace(/[^,\d]/g, "").toString(),
      split = number_string.split(","),
      sisa = split[0].length % 3,
      rupiah = split[0].substr(0, sisa),
      ribuan = split[0].substr(sisa).match(/\d{3}/gi);

    // tambahkan titik jika yang di input sudah menjadi angka ribuan
    if (ribuan) {
      let separator = sisa ? "." : "";
      rupiah += separator + ribuan.join(".");
    }

    rupiah = split[1] != undefined ? rupiah + "," + split[1] : rupiah;
    return prefix == undefined ? rupiah : rupiah ? "Rp. " + rupiah : "";
  }

  return (
    <>
      <div className="flex h-screen bg-white">
        <div className="flex-1 ">
          <div className="navbar bg-base-100">
            <div className="flex-1">
              <a className="btn btn-ghost normal-case text-xl">
                Point of Sales
              </a>
            </div>
            <div className="flex-none gap-2">
              <div className="form-control">
                <input
                  type="text"
                  placeholder="Search"
                  className="input input-bordered"
                />
              </div>
              <div className="dropdown dropdown-end">
                <label tabIndex={0} className="btn btn-ghost btn-circle avatar">
                  <div className="w-10 rounded-full">
                    <img src="/images/stock/photo-1534528741775-53994a69daeb.jpg" />
                  </div>
                </label>
                <ul
                  tabIndex={0}
                  className="mt-3 p-2 shadow menu menu-compact dropdown-content bg-base-100 rounded-box w-52"
                >
                  <li>
                    <a className="justify-between">
                      Profile
                      <span className="badge">New</span>
                    </a>
                  </li>
                  <li>
                    <a>Settings</a>
                  </li>
                  <li>
                    <a>Logout</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div></div>
          <div className="grid grid-cols-3 gap-4 px-4">
            {dataFood.map((item) => (
              <div
                key={item.id}
                className="card card-compact bg-base-100 shadow-xl"
              >
                <figure>
                  <img src="/images/stock/photo-1606107557195-0e29a4b5b4aa.jpg" />
                </figure>
                <div className="card-body">
                  <h2 className="card-title">{item.name}</h2>
                  <p>{item.description}</p>
                  <p className="flex-grow w-full text-2xl text-gray-700">
                    <span className="font-light text-gray-400 text-md">
                      Rp{" "}
                    </span>
                    {formatRupiah(item.priceAndStock.price + "")}
                  </p>
                  <div className="card-actions justify-end">
                    <button
                      onClick={() => addProduct(item)}
                      className="btn btn-primary"
                    >
                      Add
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className="w-2/6 px-6 py-6 border-l-2 border-gray-500 overflow-auto">
          <div className="h-3/4">
            <card className="w-full h-auto items-center flex flex-col px-4">
              <div className="flex flex-row justify-between my-6 w-full">
                <h1 className="text-xl">Current Order</h1>
                <div>
                  <button className="btn btn-outline btn-info btn-sm">
                    Clear
                  </button>
                </div>
              </div>
              {selectedProducts.map((item) => (
                <div
                  key={item.id}
                  className="overflow-x-auto h-24 flex-row flex w-full justify-between"
                >
                  <div>
                    <label>{item.name}</label>
                  </div>
                  <div className="flex-row flex justify-between gap-4">
                    <div className="flex gap-4">
                      <button
                        onClick={() => reduceStock(item)}
                        type="button"
                        className="text-white btn-sm bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm p-2.5 text-center inline-flex items-center  dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                      >
                        <BiMinus fontSize={20} />
                      </button>
                      <label>{item.amount}</label>
                      <button
                        onClick={() => addStock(item)}
                        type="button"
                        className="text-white btn-sm bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm p-2.5 text-center inline-flex items-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                      >
                        <BiPlus fontSize={20} />
                      </button>
                    </div>
                    <div className="">
                      <label>{item.price} </label>
                      <label> {item.stock}</label>
                    </div>
                  </div>
                </div>
              ))}

              <div className="w-4/5 h-24">
                <div className="card card-compact bg-green-400 shadow-xl">
                  <p className="text-xs px-4 mb-2">Subtotal</p>
                  <p className="text-xs px-4 mb-2">Discount</p>
                  <p className="text-xs px-4 mb-2">Total</p>
                </div>
              </div>
            </card>
          </div>
          <div className="h-auto">
            <card className="bg-red-600 w-full h-40">
              <div className="card card-compact bg-base-100 shadow-xl">
                <div className="flex flex-row justify-between card-body">
                  <h1 className="text-xl">Payment Method</h1>
                </div>
              </div>
            </card>
          </div>
        </div>
      </div>
    </>
  );
};

export default Home;
