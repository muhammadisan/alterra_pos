import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Navbar from "./components/Navbar";
import Stock from "./pages/stock";
import Home from "./pages/home";
import Order from "./pages/order";

const App = () => {
  return (
    <div className="w-full">
      <BrowserRouter>
        <div className="flex h-screen">
          <div className="h-screen">
            <Sidebar />
          </div>
          <div className="w-full h-screen">
            <div>
              <Navbar />
            </div>
            <div className="max-h-[calc(100vh-65px)] overflow-y-auto">
              <Routes>
                <Route key={"/home"} path="/home" element={<Home />} />
                <Route exact path="/stock" element={<Stock />} />
                <Route exact path="/order" element={<Order />} />
              </Routes>
            </div>
          </div>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default App;
