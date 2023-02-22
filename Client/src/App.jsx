import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Navbar from "./components/Navbar";
import Stock from "./pages/stock";
import Home from "./pages/home";
import Login from "./pages/login"
import Order from "./pages/order";
import { useSelector } from "react-redux";

const App = () => {
  const user = useSelector(state => state.auth);

  return (
    <div className="w-full">
      <BrowserRouter>
        <div className="flex h-screen">
          {user.isLoggedIn && user.user.role == "ROLE_ADMIN" && <div className="h-screen"><Sidebar /></div>}
          <div className="w-full h-screen">
            {user.isLoggedIn && <div><Navbar /></div>}
            <div className={`${user.isLoggedIn ? "max-h-[calc(100vh-65px)]" : "h-screen"} overflow-y-auto`}>
              <Routes>
                <Route exact path="/" element={<Login />} />
                <Route exact path="/home" element={<Home />} />
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
