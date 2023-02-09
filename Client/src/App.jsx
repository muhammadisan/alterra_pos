import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Stock from "./pages/stock";

const App = () => {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Sidebar />} />
          <Route exact path="/stock" element={<Stock />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
};

export default App;
