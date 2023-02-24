import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { register, login } from "../actions/auth";
import { useNavigate } from "react-router-dom";
import { useAlert } from "react-alert";

const Login = () => {
    const user = useSelector(state => state.auth);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const alert = useAlert();
    const [signUpCard, setSignUpCard] = useState(false);
    const [name, setName] = useState("");
    const [phone, setPhone] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("ROLE_MEMBERSHIP");
    const [position, setPosition] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (user.isLoggedIn) navigate("/home");
    }, [user])

    function signIn() {
        if (username && password) {
            setLoading(true);
            dispatch(login(username, password)).then((response) => {
                if (response.success) {
                    alert.success("Logged in");
                    navigate("/home");
                }
                else alert.error(response.message);
                setLoading(false);
            })
        }
    }

    function signUp() {
        if (name && phone && username && password && role) {
            if (role == "ROLE_ADMIN" && position == "") return;
            if (role == "ROLE_MEMBERSHIP") setPosition("");
            setLoading(true);
            dispatch(register(name, position, phone, username, password, role, position)).then((response) => {
                if (response.success) {
                    alert.success("Success. Please Log in");
                    setSignUpCard(false);
                }
                else alert.error(response.message);
                setLoading(false);
            })
        }
    }

    return (
        <>
            <div className="bg-no-repeat bg-cover bg-center relative" style={{ backgroundImage: 'url(https://images.unsplash.com/photo-1569058242567-93de6f36f8e6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80)' }}>
                <div className="absolute bg-gradient-to-b from-green-500 to-green-400 opacity-75 inset-0 z-0" />
                <div className="min-h-screen sm:flex sm:flex-row mx-0 justify-center">
                    <div className="flex-col flex  self-center p-10 sm:max-w-5xl xl:max-w-2xl  z-10">
                        <div className="self-start hidden lg:flex flex-col  text-white">
                            <h1 className="mb-3 font-bold text-5xl">Hi? Welcome Back</h1>
                            <p className="pr-3">Selamat datang di Alterra Resto. Tamu diharap lapar</p>
                        </div>
                    </div>
                    {signUpCard
                        ? <div className="flex justify-center self-center z-10 p-5">
                            <div className="p-12 pt-6 bg-white mx-auto rounded-2xl w-100 ">
                                <div className="mb-4">
                                    <h3 className="font-semibold text-2xl text-gray-800">Sign Up </h3>
                                    <p className="text-gray-500">Please complete the form.</p>
                                </div>
                                <div className="space-y-5">
                                    <div className="space-y-0">
                                        <label className="text-sm font-medium text-gray-700 tracking-wide">Full Name</label>
                                        <input value={name} onChange={(e) => setName(e.target.value)} className=" w-full text-base px-4 py-2 border  border-gray-300 rounded-lg focus:outline-none focus:border-green-400" type="text" placeholder="Jhane Doe" />
                                    </div>
                                    <div className="space-y-0">
                                        <label className="text-sm font-medium text-gray-700 tracking-wide">Phone</label>
                                        <input value={phone} onChange={(e) => setPhone(e.target.value)} className=" w-full text-base px-4 py-2 border  border-gray-300 rounded-lg focus:outline-none focus:border-green-400" type="text" placeholder="08XXXXXXXXXX" />
                                    </div>
                                    <div className="space-y-0">
                                        <label className="text-sm font-medium text-gray-700 tracking-wide">Username</label>
                                        <input value={username} onChange={(e) => setUsername(e.target.value)} className=" w-full text-base px-4 py-2 border  border-gray-300 rounded-lg focus:outline-none focus:border-green-400" type="text" placeholder="username" />
                                    </div>
                                    <div className="space-y-0">
                                        <label className="mb-5 text-sm font-medium text-gray-700 tracking-wide">
                                            Password
                                        </label>
                                        <input value={password} onChange={(e) => setPassword(e.target.value)} className="w-full content-center text-base px-4 py-2 border  border-gray-300 rounded-lg focus:outline-none focus:border-green-400" type="password" placeholder="********" />
                                    </div>
                                    <div className="space-y-0">
                                        <label className="mb-5 text-sm font-medium text-gray-700 tracking-wide">
                                            Role
                                        </label>
                                        <div className="flex">
                                            <div className="flex pr-5">
                                                <input onChange={() => setRole("ROLE_MEMBERSHIP")} type="radio" name="radio-5" className="radio radio-success -mt-0" checked={role == "ROLE_MEMBERSHIP"} />
                                                <div className="pl-1">Member</div>
                                            </div>
                                            <div className="flex">
                                                <input onChange={() => setRole("ROLE_ADMIN")} type="radio" name="radio-5" className="radio radio-success -mt-0" checked={role == "ROLE_ADMIN"} />
                                                <div className="pl-1">Admin</div>
                                            </div>
                                        </div>
                                    </div>
                                    {role == "ROLE_ADMIN"
                                        && <div className="space-y-0">
                                            <label className="text-sm font-medium text-gray-700 tracking-wide">Position</label>
                                            <input value={position} onChange={(e) => setPosition(e.target.value)} className=" w-full text-base px-4 py-2 border  border-gray-300 rounded-lg focus:outline-none focus:border-green-400" type="text" placeholder="Cashier" />
                                        </div>}
                                    <div className="flex items-center text-xs">
                                        <div className="flex items-center">
                                            <p>Have an account?</p>
                                        </div>
                                        <div className="ml-2">
                                            <a onClick={() => setSignUpCard(false)} href="#" className="text-green-400 hover:text-green-500">
                                                Sign In
                                            </a>
                                        </div>
                                    </div>
                                    <div>
                                        <button onClick={signUp} type="submit" className={`${loading ? 'loading' : ''} btn btn-success w-full flex justify-center bg-green-400  hover:bg-green-500 text-gray-100 p-3  rounded-full tracking-wide font-semibold  shadow-lg cursor-pointer transition ease-in duration-500`}>
                                            Sign Up
                                        </button>
                                    </div>
                                </div>
                                <div className="pt-5 text-center text-gray-400 text-xs">
                                    <span>
                                        Copyright © 2021-2022
                                        <a href="https://codepen.io/uidesignhub" rel target="_blank" title="Ajimon" className="text-green hover:text-green-500 ">AJI</a></span>
                                </div>
                            </div>
                        </div>
                        : <div className="flex justify-center self-center  z-10">
                            <div className="p-12 bg-white mx-auto rounded-2xl w-100 ">
                                <div className="mb-4">
                                    <h3 className="font-semibold text-2xl text-gray-800">Sign In </h3>
                                    <p className="text-gray-500">Please sign in to your account.</p>
                                </div>
                                <div className="space-y-5">
                                    <div className="space-y-2">
                                        <label className="text-sm font-medium text-gray-700 tracking-wide">Username</label>
                                        <input className=" w-full text-base px-4 py-2 border  border-gray-300 rounded-lg focus:outline-none focus:border-green-400" value={username} onChange={(e) => setUsername(e.target.value)} type="text" placeholder="username" />
                                    </div>
                                    <div className="space-y-2">
                                        <label className="mb-5 text-sm font-medium text-gray-700 tracking-wide">
                                            Password
                                        </label>
                                        <input className="w-full content-center text-base px-4 py-2 border  border-gray-300 rounded-lg focus:outline-none focus:border-green-400" value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="********" />
                                    </div>
                                    <div className="flex items-center text-xs">
                                        <div className="flex items-center">
                                            <p>Don't have an account?</p>
                                        </div>
                                        <div className="ml-2">
                                            <a onClick={() => setSignUpCard(true)} href="#" className="text-green-400 hover:text-green-500">
                                                Sign Up
                                            </a>
                                        </div>
                                    </div>
                                    <div>
                                        <button onClick={signIn} type="submit" className={`${loading ? "loading" : ""} btn btn-success w-full flex justify-center bg-green-400  hover:bg-green-500 text-gray-100 p-3  rounded-full tracking-wide font-semibold  shadow-lg cursor-pointer transition ease-in duration-500`}>
                                            Sign in
                                        </button>
                                    </div>
                                </div>
                                <div className="pt-5 text-center text-gray-400 text-xs">
                                    <span>
                                        Copyright © 2021-2022
                                        <a href="https://codepen.io/uidesignhub" rel target="_blank" title="Ajimon" className="text-green hover:text-green-500 ">AJI</a></span>
                                </div>
                            </div>
                        </div>}
                </div>
            </div>
        </>
    );
};

export default Login;
