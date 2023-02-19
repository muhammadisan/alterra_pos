import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { register, login, logout } from "../actions/auth"
import { useAlert } from "react-alert";

const Navbar = () => {
    const user = useSelector(state => state.auth);
    const dispatch = useDispatch();
    const alert = useAlert();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);

    function signIn() {
        if (username && password) {
            setLoading(true);
            dispatch(login(username, password)).then((response) => {
                if (response.success) alert.success("Logged in");
                else alert.error(response.message);
                setLoading(false);
            })
        }
    }

    function logOut() {
        setLoading(true);
        dispatch(logout());
        alert.success("Logged out");
        setLoading(false);
    }

    return (
        <div className="navbar bg-base-100 h-10 shadow-2xl">
            <div className="flex-1">
                <a className="btn btn-ghost normal-case text-xl">Alterra Resto</a>
            </div>
            {user.isLoggedIn
                ? <div><span className="mx-3">Hello, {user.user.username}</span><button className={`btn btn-sm btn-success btn-outline mr-4 ${loading ? "loading" : ""}`} onClick={logOut}>Log Out</button></div>
                : <div className="dropdown dropdown-end">
                    <label tabIndex={0} className={`btn btn-sm btn-success btn-outline ${loading ? "loading" : ""}`}>
                        Sign In
                    </label>
                    <div tabIndex={0} className="mt-2 card card-compact dropdown-content w-52 bg-base-300 shadow">
                        <div className="card-body">
                            <input value={username} onChange={(e) => setUsername(e.target.value)} type="text" placeholder="Username" className="input input-bordered input-success w-full max-w-xs" />
                            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="Password" className="input input-bordered input-success w-full max-w-xs" />
                            <div className="card-actions">
                                <button className={`btn btn-block btn-success ${loading ? "loading" : ""}`} onClick={signIn}>Sign In</button>
                            </div>
                        </div>
                    </div>
                </div>}
        </div>
    )
}

export default Navbar;