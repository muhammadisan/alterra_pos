import React, { useEffect, useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import http from "../http"
import { BiPlus, BiMinus } from "react-icons/bi";
import OrdersInvoice from "../components/OrdersInvoice";
import { useReactToPrint } from "react-to-print";
import { useAlert } from "react-alert";

const Home = () => {
  const user = useSelector(state => state.auth);
  const alert = useAlert();
  const [dataFood, setDataFood] = useState([]);
  const [dataPaymentMethod, setDataPaymentMethod] = useState([]);
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [subtotal, setSubtotal] = useState(0);
  const [discount, setDiscount] = useState(0);
  const [total, setTotal] = useState(0);
  const [userRole, setUserRole] = useState("");
  const [paymentMethodId, setPaymentMethodId] = useState(null);
  const componentRef = useRef();
  const [getOrderNo, setGetOrderNo] = useState("");
  const [tempOrderNo, setTempOrderNo] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user.isLoggedIn) setUserRole(user.user.role);
    async function doGetRequest() {
      let res = await http.get(`/products`);
      let dataFood = res.data;
      setDataFood(dataFood);

      let payment = await http.get(`/paymentMethod`);
      setDataPaymentMethod(payment.data)
    }

    doGetRequest();
  }, [user]);

  function addProduct(product) { // ADD button
    let amount = 0;
    for (let prod of selectedProducts) {
      if (prod.id === product.id) {
        amount = prod.amount;
        break;
      }
    }
    if (product.priceAndStock.stock <= amount) return;

    setSubtotal((prev) => prev + product.priceAndStock.price);
    setTotal((prev) => prev + product.priceAndStock.price);

    const newProduct = {
      id: product.id,
      name: product.name,
      amount: 1,
      stock: product.priceAndStock.stock,
      price: product.priceAndStock.price
    };

    const productIndex = selectedProducts.findIndex(
      (item) => item.id === product.id
    );

    if (productIndex === -1) {
      setSelectedProducts((prev) => [
        ...prev, newProduct
      ]);
      return;
    }

    setSelectedProducts((prev) =>
      prev.map((item, index) => {
        if (productIndex === index) {
          return {
            ...item,
            amount: item.amount + 1
          };
        }
        return item;
      })
    );
  }

  function reduceStock(product) { // (-) button
    setSelectedProducts((prev) => {
      if (product.amount === 1) {
        return prev.filter((item) => item.id !== product.id);
      }

      return prev.map((item) => {
        if (item.id === product.id) {
          return {
            ...item,
            amount: item.amount - 1
          };
        }
        return item;
      });
    });
    setSubtotal((prev) => prev - product.price);
    setTotal((prev) => prev - product.price);
  }

  function addStock(product) { // (+) button
    let amount = 0;
    for (let prod of selectedProducts) {
      if (prod.id === product.id) {
        amount = prod.amount;
        break;
      }
    }
    if (product.stock <= amount) return;

    setSelectedProducts((prev) => {
      return prev.map((item) => {
        if (item.stock > 0 && item.id === product.id) {
          return {
            ...item,
            amount: item.amount + 1
          };
        }
        return item;
      });
    });
    setSubtotal((prev) => prev + product.price);
    setTotal((prev) => prev + product.price);
  }

  function handleClear() {
    setSelectedProducts([]);
    setSubtotal(0);
    setDiscount(0);
    setTotal(0);
    setPaymentMethodId(null);
  }

  function handlePrint() {
    if (!user.isLoggedIn) {
      alert.error("You're not logged in");
      return;
    }
    if (selectedProducts.length == 0) {
      alert.error("Plese grab some menu first");
      return;
    }
    if (paymentMethodId == null) {
      alert.error("Pleas choose payment method");
      return;
    }

    let products = [];
    for (let product of selectedProducts) products = [...products, { productId: product.id, amount: product.amount }];

    if (user.user.role == "ROLE_MEMBERSHIP") {
      setLoading(true);
      http.post(`/orders`, { products, paymentMethodId, userId: user.user.id }, { responseType: 'blob' }).then((res) => {
        if (res.headers["content-type"] == "application/pdf") {
          alert.success("Success create order");
          handleClear();

          let fileURL = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }))
          let fileLink = document.createElement('a')

          fileLink.href = fileURL
          fileLink.setAttribute('download', 'order')
          document.body.appendChild(fileLink)
          fileLink.click();
        } else {
          alert.error(res.data.message);
        }
        setLoading(false);
      })
    } else if (user.user.role == "ROLE_ADMIN") {
      setLoading(true);
      http.post(`/receipts`, { orderNo: tempOrderNo, products, paymentMethodId, userId: user.user.id }, { responseType: 'blob' }).then((res) => {
        if (res.headers["content-type"] == "application/pdf") {
          alert.success("Success create receipt");
          handleClear();

          let fileURL = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }))
          let fileLink = document.createElement('a')

          fileLink.href = fileURL
          fileLink.setAttribute('download', 'receipt')
          document.body.appendChild(fileLink)
          fileLink.click();
        } else {
          alert.error(res.data.message);
        }
        setLoading(false);
      })
    }
  }

  function getOrders() {
    if (getOrderNo.length != 13) {
      alert.error("Order No length must be 13");
      return;
    }

    setLoading(true);
    setTempOrderNo(getOrderNo);
    http.get(`/orders/orderNo/${getOrderNo}`).then((res) => {
      if (res.data.length == 0) {
        alert.error("Order No not fond");
      } else {
        setSelectedProducts([]);
        setSubtotal(0);
        setDiscount(0);
        setTotal(0);
        setPaymentMethodId(res.data[0].paymentMethod.id);

        for (let data of res.data) {
          let product = {
            id: data.product.id,
            name: data.product.name,
            amount: data.amount,
            stock: data.product.priceAndStock.stock,
            price: data.price
          }
          setSelectedProducts((prev) => [...prev, product]);
          setSubtotal((prev) => prev + product.amount * product.price);
          setTotal((prev) => prev + product.amount * product.price);
        }

        alert.success("Success get orders");
      }
      setLoading(false);
    });
  }

  function formatRupiah(angka, prefix) {
    let number_string = angka.replace(/[^,\d]/g, "").toString(),
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
      <div className="w-full flex h-full bg-base-300">
        <div className="flex-1 h-full">
          <div className={`navbar h-[74px] ${user.isLoggedIn && user.user.role == "ROLE_ADMIN" ? "" : "hidden"}`}>
            <div className={`flex-1 mr-[100px]`}>
              <input type="text" placeholder="Check Order No" value={getOrderNo} onChange={(e) => setGetOrderNo(e.target.value)} className="input input-bordered w-100 max-w-xs mr-2" />
              <button className={`btn btn-success btn-outline ${loading ? "loading" : ""}`} onClick={getOrders}>Check</button>
            </div>
          </div>
          <div className="h-full">
            <div className="grid grid-cols-3 gap-4 px-4 mb-10 mt-5">
              {dataFood.map((item) => (
                <div
                  key={item.id}
                  className="card card-compact shadow-xl bg-base-100"
                >
                  {/* <figure>
                    <img src="/images/stock/photo-1606107557195-0e29a4b5b4aa.jpg" />
                  </figure> */}
                  <div className="card-body">
                    <h2 className="card-title">{item.name}</h2>
                    <p className="text-sm">{item.description}</p>
                    <div className="flex-grow w-full text-2xl">
                      <span className="font-light text-gray-400 text-md">
                        Rp{" "}
                      </span>
                      {formatRupiah(item.priceAndStock.price + "")}
                    </div>
                    <div className="card-actions justify-end">
                      <div className="place-content-center my-auto text-gray-400">Stock: {item.priceAndStock.stock}</div>
                      <button
                        onClick={() => addProduct(item)}
                        className="btn btn-success btn-sm"
                      >
                        Add
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="w-2/6 px-2 border-l-2 border-gray-500 overflow-auto">
          <div className="w-full h-screen items-center flex flex-col px-4">
            <div className="flex flex-row justify-between my-6 w-full">
              <h1 className="text-xl">Current Order</h1>
              <div>
                <button className="btn btn-outline btn-error btn-xs" onClick={handleClear}>
                  Clear
                </button>
              </div>
            </div>

            <div className={`h-3/6 overflow-y-auto flex flex-col w-full border-opacity-50 ${selectedProducts.length == 0 && "place-content-center"}`}>
              {selectedProducts.length == 0 && <div className="m-auto">No orders yet</div>}
              {selectedProducts.map((item) => (
                <div key={item.id}>
                  <div className="grid h-20 mb-1 card bg-base-100 rounded-box grid grid-cols-6 place-content-center">
                    <div className="col-span-4 ml-3">
                      <label>{item.name}</label>
                      <div className="text-sm">
                        <span className="font-light text-gray-400">
                          Rp{" "}
                        </span>
                        {formatRupiah(item.price * item.amount + "")}
                      </div>
                    </div>
                    <div className="col-span-2">
                      <button className="btn btn-outline btn-circle btn-success btn-xs" onClick={() => reduceStock(item)}><BiMinus /></button>
                      <label className="mx-2">{item.amount}</label>
                      <button className="btn btn-outline btn-circle btn-success btn-xs" onClick={() => addStock(item)}><BiPlus /></button>
                      <p className="text-gray-400 pt-2">Stock: {item.stock}</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className="w-full m-2 bg-base-100 rounded-box">
              <div className="card card-compact h-full py-2">
                <div className="text-xs px-4 grid grid-cols-2">
                  <div className="col-span-1 text-base">Subtotal</div>
                  <div className="col-span-1 flex justify-end">
                    <div className="text-base">
                      <span className="font-light">
                        Rp{" "}
                      </span>
                      {formatRupiah(subtotal + "")}
                    </div>
                  </div>
                </div>
                <div className="text-xs px-4 grid grid-cols-2">
                  <div className="col-span-1 text-base">Discount</div>
                  <div className="col-span-1 flex justify-end">
                    <div className="text-base">
                      <span className="font-light">
                        Rp{" "}
                      </span>
                      {formatRupiah("0")}
                    </div>
                  </div>
                </div>
                <div className="text-xs px-4 grid grid-cols-2">
                  <div className="col-span-1 text-base">Total</div>
                  <div className="col-span-1 flex justify-end">
                    <div className="text-base">
                      <span className="font-light">
                        Rp{" "}
                      </span>
                      {formatRupiah(total + "")}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="w-full h-[70px] card card-compact bg-base-100 rounded-box mt-4 mb-2 flex justify-center">
              <div className="card-body flex flex-row">
                <div className={`grid grid-cols-${dataPaymentMethod.length} w-full`}>
                  {dataPaymentMethod.map((payment) => {
                    return (
                      <div key={payment.id} className="col-span-1 flex flex-col">
                        <div className="flex justify-center"><input type="radio" name="radio-5" className="radio radio-success" onClick={() => setPaymentMethodId(payment.id)} checked={payment.id == paymentMethodId} /></div>
                        <label className="flex justify-center text-base">{payment.payment}</label>
                      </div>
                    )
                  })}
                </div>
              </div>
            </div>

            <div className="w-full flex justify-end mb-2"><button className={`w-full btn btn-success btn-outline ${loading ? 'loading' : ''}`} onClick={handlePrint}>Print {user.isLoggedIn && user.user.role == "ROLE_ADMIN" ? "Receipt" : "Order"}</button></div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Home;
