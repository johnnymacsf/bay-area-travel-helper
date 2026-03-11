import { useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { toast } from "react-toastify";

export default function AddReviewPage(){
    const { hotelId } = useParams();
    const navigate =  useNavigate();

    const [reviewTitle, setReviewTitle] = useState("");
    const [reviewText, setReviewText] = useState("");
    const [rating, setRating] = useState(5);
    const [loading, setLoading] = useState(false);

    async function handleSubmit(e) {
        e.preventDefault();
        setLoading(true);

        try{
            const res = await fetch("http://localhost:8090/api/reviews/add", {
                method: "POST",
                credentials: "include",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    hotelId,
                    title: reviewTitle,
                    text: reviewText,
                    rating
                })
            });

            if(!res.ok){
                const message = await res.text();
                toast.error(message || "Failed to add review. Please try again");
                setLoading(false);
                return;
            }
            const data = await res.json();
            toast.success("Successfully added review!");
            navigate(`/hotel/${data.hotelId}`);
        }catch(error){
            console.error(error);
            toast.error("Uh oh! Something went wrong!");
        }finally{
            setLoading(false);
        }
    }
    return (
        <div className="min-h-screen bg-gray-50 p-6">
            <div className="max-w-xl mx-auto bg-white shadow rounded p-6">
                <h1 className="text-2xl font-bold text-center mb-6">Add Your Review</h1>
                <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
                    <div>
                        <label className="block font-semibold mb-1">Review Title</label>
                        <input type="text" className="w-full border rounded px-3 py-2" value={reviewTitle} onChange={(e) => setReviewTitle(e.target.value)} required/>
                    </div>
                    <div>
                        <label className="block font-semibold mb-1">Review Text</label>
                        <textarea rows="5" className="w-full border rounded px-3 py-2" value={reviewText} onChange={(e) => setReviewText(e.target.value)} required/>
                    </div>
                    <div>
                        <label className="block font-semibold mb-1">Rating</label>
                        <input type="number" min="1" max="5" className="w-full border rounded px-3 py-2" value={rating} onChange={(e) => setRating(Number(e.target.value))} required/>
                    </div>
                    <button type="submit" className="bg-blue-600 text-white py-2 rounded hover:bg-blue-300" disabled={loading}>
                        {loading ? "Submitting..." : "Submit"}
                    </button>
                </form>
                <div className="text-center mt-4">
                    <Link to={`/hotel/${hotelId}`} className="text-blue-600 underline">
                        Back to hotel
                    </Link>
                </div>
            </div>
        </div>
    )
}