import React, { useMemo } from "react";
import { useState } from "react";
import { useEffect } from "react";
import http from "../http";

const Order = () => {
  const [data, setData] = useState([]);
  const [filterValue, setFilterValue] = useState("");
  const [fetchStatus] = useState(true);

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
    getAll();
  }, []);

  return (
    <div className="w-full bg-white py-3">
      <div className="flex flex-row justify-between">
        <h2 className="pl-4 text-2xl">Orders</h2>
        <div className="text-end">
          <input
            onChange={handleFilterOrder}
            type="text"
            className="rounded-lg border-transparent flex-1 appearance-none border border-gray-300 w-full py-2 px-4 bg-white text-gray-700 placeholder-gray-400 shadow-sm text-base focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
            placeholder="Order No"
          />
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
                  <tr key={res.id}>
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
