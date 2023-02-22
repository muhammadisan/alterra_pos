import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../actions/auth"
import { useAlert } from "react-alert";
import { useNavigate } from "react-router-dom";

const Navbar = () => {
    const user = useSelector(state => state.auth);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const alert = useAlert();
    const [loading, setLoading] = useState(false);

    function logOut() {
        setLoading(true);
        dispatch(logout());
        navigate("/");
        alert.success("Logged out");
        setLoading(false);
    }

    return (
        <div className="navbar bg-base-100 h-10 shadow-xl" style={{ border: "1px groove" }}>
            <div className="flex-1">
                <a onClick={() => navigate("/home")} className="btn btn-ghost normal-case text-xl text-success">Alterra Resto</a>
            </div>
            {user.isLoggedIn
                ? <div><span className="mx-3">Hello, {user.user.username}</span><button className={`btn btn-sm btn-success btn-outline mr-4 ${loading ? "loading" : ""}`} onClick={logOut}>Log Out</button></div>
                : ""}
        </div>
    )
}

export default Navbar;