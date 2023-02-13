import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Stock from "./pages/stock";

const App = () => {
  return (
    <div>
      <BrowserRouter>
        <div style={{ display: "flex"}}>
          <Sidebar />
          <Routes>
            <Route exact path="/stock" element={<Stock />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default App;
