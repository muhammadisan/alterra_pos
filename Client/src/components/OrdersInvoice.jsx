import "./OrdersInvoice.css"
import React from "react";

const OrdersInvoice = React.forwardRef((props, ref) => {
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
    <div id="invoice-POS" ref={ref}>
      <center id="top">
        <div className="logo" />
        <div className="info">
          <h2>R.M. Tidak Sederhana</h2>
        </div>
      </center>
      <div id="mid">
        <div className="info">
          {/* <h2>Contact Info</h2> */}
          <p>
            Order No. : <span style={{ fontWeight: "bold" }}>{props.orderNo == null ? "Error!" : props.orderNo}</span><br />
            {/* Date   : {new Date() + ""}<br /> */}
            {/* Phone   : 555-555-5555<br /> */}
          </p>
        </div>
      </div>
      <div id="bot">
        <div id="table">
          <table>
            <tbody><tr className="tabletitle">
              <td className="item" style={{ width: "45%" }}><h2>Item</h2></td>
              <td className="Hours" style={{ width: "25%" }}><h2>Qty</h2></td>
              <td className="Rate" style={{ width: "40%" }}><h2>Sub Total</h2></td>
            </tr>
              {props.data.map((product) => {
                return <tr key={product.id} className="service">
                  <td className="tableitem"><p className="itemtext">{product.name}</p></td>
                  <td className="tableitem"><p className="itemtext">{product.amount}</p></td>
                  <td className="tableitem"><p className="itemtext">Rp {formatRupiah(product.price * product.amount + "")}</p></td>
                </tr>
              })}
              <tr className="tabletitle">
                <td />
                <td className="Rate"><h2>Subtotal</h2></td>
                <td className="payment"><h2>Rp {formatRupiah(props.subtotal + "")}</h2></td>
              </tr>
              <tr className="tabletitle">
                <td />
                <td className="Rate"><h2>Discount</h2></td>
                <td className="payment"><h2>Rp {formatRupiah(props.discount + "")}</h2></td>
              </tr>
              <tr className="tabletitle">
                <td />
                <td className="Rate"><h2>Total</h2></td>
                <td className="payment"><h2>Rp {formatRupiah(props.total + "")}</h2></td>
              </tr>
            </tbody></table>
        </div>
        <div id="legalcopy">
          <p className="legal" style={{ fontSize: "10px" }}><strong>Thank you for your order!</strong><br />Give this invoice to the cashier to process your order
          </p>
        </div>
      </div>
    </div>
  );
});

export default OrdersInvoice;