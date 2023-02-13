import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Stock from "./pages/stock";
import Home from "./pages/home";

const App = () => {
  return (
    <div>
      <BrowserRouter>
        <div style={{ display: "flex" }}>
          <Sidebar />
          <Routes>
            <Route key={"/home"} path="/home" element={<Home />} />
            <Route exact path="/stock" element={<Stock />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default App;
