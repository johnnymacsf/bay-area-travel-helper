import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { useEffect, useState } from "react";

export default function HomePage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [lastLogin, setLastLogin] = useState("");

    useEffect(() => {
        async function fetchSession() {
            try{
                const res = await fetch("http://localhost:8090/api/auth/session", {
                    credentials: "include"
                });

                if(!res.ok){
                    navigate("/login");
                    toast.info("Please log in to your account to continue");
                    return;
                }

                const data = await res.json();
                setUsername(data.username);
                setLastLogin(data.lastLogin || "Welcome! This is your first time logging in!");
            }catch(error){
                console.error(error);
                toast.error("Uh oh! Something went wrong!");
            }
        }
        fetchSession();
    }, [navigate]);

    return(
        <div className="min-h-screen bg-gray-50">
            <div className="bg-blue-700 text-white py-6 text-center shadow">
                <h1 className="text-3xl font-bold">TravelHelper</h1>
            </div>
            <div className="max-w-3xl mx-auto mt-10 flex flex-col items-center gap-6">
                <div className="flex gap-4">
                <Link
                    to="/hotels"
                    className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded"
                >
                    Search Hotels
                </Link>
                <button
                        onClick={async () => {
                            try {
                                const res = await fetch("http://localhost:8090/api/auth/logout", {
                                    method: "POST",
                                    credentials: "include",
                                });
                                if (res.ok) {
                                    toast.success("Successfully logged out!");
                                    navigate("/login");
                                }
                            } catch (err) {
                                console.error(err);
                                toast.error("Logout failed!");
                            }
                        }}
                        className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded"
                    >
                        Logout
                    </button>
                </div>
                <h2 className="text-2xl font-semibold text-blue-700">
                Welcome, {username}!
                </h2>
                <h3 className="text-lg text-gray-700">{lastLogin.includes("Welcome") ? lastLogin : `You last logged in on: ${new Date(lastLogin).toLocaleString("en-US", {
                    year: "numeric", month: "long", day: "numeric", hour: "numeric", minute:"2-digit"
                })}`}</h3>
            </div>
        </div>
    );
}