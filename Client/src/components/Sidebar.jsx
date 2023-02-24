import React, { useState } from "react";
import { HiMenuAlt3 } from "react-icons/hi";
import { MdAddShoppingCart } from "react-icons/md";
import { AiOutlineHome } from "react-icons/ai";
import { BiBox } from "react-icons/bi";
import { Link } from "react-router-dom";
import { useSelector } from "react-redux";

const Sidebar = () => {
  const user = useSelector(state => state.auth);

  let menus = []
  if (user.isLoggedIn) {
    if (user.user.role == "ROLE_ADMIN") {
      menus = [
        { name: "Home", link: "/home", icon: AiOutlineHome },
        // { name: "Dashboard", link: "/dashboard", icon: MdOutlineDashboard },
        { name: "Order", link: "/order", icon: MdAddShoppingCart },
        { name: "Stock", link: "/stock", icon: BiBox },
        // { name: "Member", link: "/member", icon: BsFillPersonFill },
      ];
    }
  }

  const [open, setOpen] = useState(true);
  return (
    <div
      className={`bg-[#0e0e0e] h-full ${open ? "w-56" : "w-16"}
      duration-500 text-gray-100 px-4`}
    >
      <div className="py-3 flex justify-end">
        <HiMenuAlt3
          size={26}
          className="cursor-pointer"
          onClick={() => setOpen(!open)}
        />
      </div>
      <div className="mt-4 flex flex-col gap-4 relative">
        {menus?.map((menu, i) => (
          <Link
            to={menu?.link}
            key={i}
            className={` ${menu?.margin && "mt-5"
              } group flex items-center text-sm  gap-3.5 font-medium p-2 hover:bg-gray-800 rounded-md`}
          >
            <div>{React.createElement(menu?.icon, { size: "20" })}</div>
            <h2
              style={{
                transitionDelay: `${i + 3}00ms`,
              }}
              className={`whitespace-pre duration-500 ${!open && "opacity-0 translate-x-28 overflow-hidden"
                }`}
            >
              {menu?.name}
            </h2>
            <h2
              className={`${open && "hidden"
                } absolute left-48 bg-white font-semibold whitespace-pre text-gray-900 rounded-md drop-shadow-lg px-0 py-0 w-0 overflow-hidden group-hover:px-2 group-hover:py-1 group-hover:left-14 group-hover:duration-300 group-hover:w-fit z-20`}
            >
              {menu?.name}
            </h2>
          </Link>
        ))}
      </div>
    </div>
  );
};

export default Sidebar;
