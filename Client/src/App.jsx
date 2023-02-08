import React from "react";
import { Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
// import Home from "./pages/home";

const App = () => {
  return (
    <div>
      <Routes>
        <Route path="/" element={<Sidebar />} />
        {/* <Route path="/" element={<Home />} /> */}
      </Routes>
    </div>
  );
};

export default App;
