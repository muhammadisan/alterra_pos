import React, { useMemo } from "react";
import { useState } from "react";
import { useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import http from "../http";

const Order = () => {
  const user = useSelector(state => state.auth);
  const navigate = useNavigate(); 
  const [data, setData] = useState([]);
  const [filterValue, setFilterValue] = useState("");

  const getOrders = async () => {
    try {
      const response = await http.get(`/orders`);
      return response.data;
    } catch (err) {
      return err;
    }
  };

  const getReceipts = async () => {
    try {
      const response = await http.get(`/receipts`);
      return response.data;
    } catch (err) {
      return err;
    }
  };

  const getAll = async () => {
    const orders = await getOrders();
    const receipts = await getReceipts();

    let setReceipts = new Set();
    for (let receipt of receipts) setReceipts.add(receipt.orderNo);

    let selectedOrders = [];
    for (let order of orders) if (!setReceipts.has(order.orderNo)) selectedOrders.push(order);

    let data = [...receipts, ...selectedOrders];
    data.sort((a, b) => { return b.createdAt - a.createdAt });
    setData(data);
  };

  const handleFilterOrder = (event) => setFilterValue(event.target.value);

  const filteredData = useMemo(
    () => data.filter((item) => item.orderNo.includes(filterValue)),
    [filterValue, data]
  );

  const formatDate = (date) => {
    let tanggal = new Date(date);
    return tanggal.toLocaleString();
  }
  useEffect(() => {
    if (!user.isLoggedIn) navigate("/");
    if (user.user.role != "ROLE_ADMIN") navigate("/home");
    getAll();
  }, []);

  return (
    <div className="w-full bg-white py-3 overflow-x-hidden">
      <div className="flex flex-row justify-between">
        <h2 className="pl-4 text-2xl">Orders</h2>
        <div className="text-end" style={{ paddingRight: "20px" }}>
          <input type="text" placeholder="Order No" onChange={handleFilterOrder} className="input input-bordered w-100 max-w-xs mr-2" />
        </div>
      </div>
      <div className="px-4 py-4 -mx-4 overflow-x-auto sm:-mx-8 sm:px-8">
        <div className="inline-block min-w-full overflow-hidden rounded-lg shadow">
          <table className="table table-compact min-w-full">
            <thead>
              <tr>
                <th className="text-base pl-8">Order No</th>
                <th className="text-base pl-8">Receipt No</th>
                <th className="text-base pl-8">Payment</th>
                <th className="text-base pl-8">Modified By</th>
                <th className="text-base pl-8">Modified At</th>
              </tr>
            </thead>
            <tbody>
              {filteredData?.map((res) => {
                return (
                  <tr key={res.id} className="hover">
                    <td className="text-base pl-8">{res.orderNo}</td>
                    <td className="text-base pl-8">{res.receiptNo}</td>
                    <td className="text-base pl-8">{res.paymentMethod}</td>
                    <td className="text-base pl-8">{res.username}</td>
                    <td className="text-base pl-8">{formatDate(res.createdAt)}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Order;
