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
    <div className="w-full bg-white">
      <div className="py-8">
        <div className="flex flex-row justify-between w-full mb-1 sm:mb-0">
          <h2 className="px-4 py- text-2xl leading-tight">Order</h2>
          <div className="text-end">
            <form className="flex flex-col justify-center w-3/4 max-w-sm space-y-3 md:flex-row md:w-full md:space-x-3 md:space-y-0">
              <div className=" relative ">
                <input
                  onChange={handleFilterOrder}
                  type="text"
                  id='"form-subscribe-Filter'
                  className=" rounded-lg border-transparent flex-1 appearance-none border border-gray-300 w-full py-2 px-4 bg-white text-gray-700 placeholder-gray-400 shadow-sm text-base focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
                  placeholder="Order No"
                />
              </div>
            </form>
          </div>
        </div>
        <div className="px-4 py-4 -mx-4 overflow-x-auto sm:-mx-8 sm:px-8">
          <div className="inline-block min-w-full overflow-hidden rounded-lg shadow">
            <table className="min-w-full leading-normal">
              <thead>
                <tr>
                  <th
                    scope="col"
                    className="px-5 py-3 text-sm font-normal text-left text-gray-800 bg-white border-b border-gray-200"
                  >
                    Order No
                  </th>
                  <th
                    scope="col"
                    className="px-5 py-3 text-sm font-normal text-left text-gray-800 bg-white border-b border-gray-200"
                  >
                    Receipt No
                  </th>
                  <th
                    scope="col"
                    className="px-5 py-3 text-sm font-normal text-left text-gray-800 bg-white border-b border-gray-200"
                  >
                    Payment
                  </th>
                  <th
                    scope="col"
                    className="px-5 py-3 text-sm font-normal text-left text-gray-800  bg-white border-b border-gray-200"
                  >
                    Modified By
                  </th>
                  <th
                    scope="col"
                    className="px-5 py-3 text-sm font-normal text-left text-gray-800  bg-white border-b border-gray-200"
                  >
                    Modified At
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredData?.map((res) => {
                  return (
                    <tr key={res.id}>
                      <td className="py-4 px-6 ">{res.orderNo}</td>
                      <td className="py-4 px-6 ">{res.receiptNo}</td>
                      <td className="py-4 px-6 ">{res.paymentMethod}</td>
                      <td className="py-4 px-6 ">{res.username}</td>
                      <td className="py-4 px-6 ">{formatDate(res.createdAt)}</td>
                      {/* <td className="py-4 px-6 ">{res.createdAt}</td> */}
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Order;
