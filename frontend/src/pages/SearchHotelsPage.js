import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

export default function SearchHotelsPage () {
    const [keyword, setKeyword] = useState("");
    const [hotels, setHotels] = useState({});
    const [page, setPage] = useState(1);
    const [hasNextPage, setHasNextPage] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");
    const navigate = useNavigate();
    
    useEffect(() => {
        async function fetchHotels() {
            try {
                const sessionRes = await fetch("http://localhost:8090/api/auth/session", {
                    credentials: "include",
                });
                if (!sessionRes.ok) {
                    // redirect to login if no session
                    navigate("/login");
                    toast.info("Please log in to search hotels");
                    return;
                }
    
                const res = await fetch(`http://localhost:8090/api/hotels?keyword=${encodeURIComponent(keyword)}&page=${page}`, {
                    credentials: "include"
                });
    
                if (!res.ok) {
                    toast.error("Failed to load hotels.");
                    return;
                }
    
                const data = await res.json();
                setHotels(data.hotelNames);
                setHasNextPage(data.hasNextPage);
            } catch (error) {
                console.error(error);
                toast.error("Uh oh! Something went wrong!");
            }
        }
    
        fetchHotels();
    }, [keyword, page, navigate]);
    

    const handleSearch = (e) => {
        e.preventDefault();
        setPage(1);
        setKeyword(searchTerm.trim());
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="bg-blue-700 text-white py-6 text-center shadow cursor-pointer" onClick={() => navigate("/")}>
                <h1 className="text-3xl font-bold">TravelHelper</h1>
            </div>
            <h1 className="text-3xl font-bold text-center mt-6 mb-6">Search Hotels</h1>
            <form className="flex justify-center mb-6 gap-2" onSubmit={handleSearch}>
                <input type="text" placeholder="Search by hotel name" onChange={(e) => setSearchTerm(e.target.value)} value={searchTerm}/>
                <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-300">Search</button>
            </form>

            <div className="max-w-3xl mx-auto flex flex-col gap-3">
                {Object.entries(hotels).length === 0 && <p className="text-center">No hotels found.</p>}

                {Object.entries(hotels).map(([hotelId, name]) => (
                    <Link
                        key={hotelId}
                        to={`/hotel/${hotelId}`}
                        className="p-3 bg-white shadow rounded hover:bg-blue-50"
                    >
                        {name}
                    </Link>
                ))}
                <div className="flex justify-between mt-4">
                    <button
                        onClick={() => setPage(Math.max(page - 1, 1))}
                        disabled={page === 1}
                        className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400 disabled:opacity-50"
                    >
                        Previous
                    </button>

                    <span className="px-4 py-2">Page {page}</span>

                    <button
                        onClick={() => setPage(page + 1)}
                        disabled={!hasNextPage}
                        className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400 disabled:opacity-50"
                    >
                        Next
                    </button>
                </div>
            </div>
        </div>
    )
}