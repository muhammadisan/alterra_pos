import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Stock from "./pages/stock";
import Home from "./pages/home";
import Order from "./pages/order";

const App = () => {
  return (
    <div className="w-full">
      <BrowserRouter>
        <div style={{ display: "flex" }}>
          <Sidebar />
          <Routes>
            <Route key={"/home"} path="/home" element={<Home />} />
            <Route exact path="/stock" element={<Stock />} />
            <Route path="/order" element={<Order />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default App;
