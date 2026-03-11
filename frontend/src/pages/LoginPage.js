import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import {toast } from "react-toastify";

export default function LoginPage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const[password, setPassword] = useState("");

    useEffect(() => {
        async function checkSession() {
            try {
                const res = await fetch("http://localhost:8090/api/auth/session", {
                    credentials: "include",
                });
                if(res.ok){
                    navigate("/");
                    toast.info("you are already logged in!");
                }
            }catch(error){
                console.error(error);
            }
        }
        checkSession();
    }, [navigate]);

    async function handleSubmit(e) {
        e.preventDefault();

        try {
            const res = await fetch("http://localhost:8090/api/auth/login", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                credentials: "include",
                body: JSON.stringify({username, password}),
            });

            const text = await res.text();
            if(!res.ok){
                toast.error("Unable to login. Please try again!");
                return;
            }

            navigate("/");
            toast.success("Successfully logged in!");
        }catch(err){
            console.error(err);
            toast.error("Uh oh! Something went wrong!");
        }
    }

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="bg-blue-700 text-white py-6 text-center shadow">
                <h1 className="text-3xl font-bold">TravelHelper</h1>
            </div>
            <div className="flex items-center justify-center mt-4">
                <form onSubmit={handleSubmit} className="w-full max-w-sm bg-white p-6 rounded-lg shadow">
                    <h1 className="text-2xl font-bold mb-4">Login</h1>
                    <label className="block mb-2 font-medium">Username</label>
                    <input className="w-full border rounded px-3 py-2 mb-4" value={username} onChange={(e) => setUsername(e.target.value)}/>
                    <label className="block mb-2 font-medium">Password</label>
                    <input className="w-full border rounded px-3 py-2 mb-4" type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                    <button type="submit" className="w-full bg-blue-600 text-white rounded py-2 hover:bg-blue-300">Login</button>
                    <p className="text-center mt-4 text-sm">
                        Don't have an account?{" "}
                        <Link className="text-blue-600 underline" to="/register">
                            Register
                        </Link>
                    </p>
                </form>
            </div>
        </div>
    );
}