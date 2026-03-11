import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import {toast } from "react-toastify";

export default function RegisterPage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    async function handleSubmit(e){
        e.preventDefault();

        try {
            const res = await fetch("http://localhost:8090/api/auth/register", {
                method:"POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({username, password}),
                credentials: "include",
            });

            const text = await res.text();
            if(!res.ok){
                toast.error(text || "Registration failed. Try again.");
                return;
            }

            toast.success("Account created successfully! Please log in");
            navigate("/login");
        }catch(error){
            console.error(error);
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
                    <h1 className="text-2xl font-bold mb-4">Register Account</h1>
                    <label className="block mb-2 font-medium">Username</label>
                    <input className="w-full border rounded px-3 py-2 mb-4" value={username} onChange={(e) => setUsername(e.target.value)} required/>
                    <label className="block mb-2 font-medium">Password</label>
                    <input className="w-full border rounded px-3 py-2 mb-4" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required/>
                    <button className="w-full bg-blue-600 text-white rounded py-2 hover:bg-blue-300" type="submit">Register</button>
                    <p className="text-center mt-4 text-sm">Already have an account?{" "}
                        <Link className="text-blue-600 underline" to="/login">
                            Login
                        </Link>
                    </p>
                </form>
            </div>
        </div>
    )
}