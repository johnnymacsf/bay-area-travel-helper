import { useState, useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

export default function HotelDetailsPage() {
  const { hotelId } = useParams();
  const navigate = useNavigate();

  const [hotel, setHotel] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [page, setPage] = useState(1);
  const [hasNextPage, setHasNextPage] = useState(false);
  const [averageRating, setAverageRating] = useState(0);
  const [loading, setLoading] = useState(false);
  const [expediaLink, setExpediaLink] = useState("");

  const [aiProsCons, setAiProsCons] = useState("");
  const [aiLoading, setAiLoading] = useState(false);
  const [accordionOpen, setAccordionOpen] = useState(false);

  useEffect(() => {
    async function fetchHotelDetails() {
      setLoading(true);
      try {
        const res = await fetch(
          `${process.env.REACT_APP_BACKEND_URL}/api/hotels/${hotelId}?page=${page}&pageSize=5`,
          { credentials: "include" }
        );

        if (res.status === 401) {
          toast.info("Please log in to view hotel details");
          navigate("/login");
          return;
        }

        if (!res.ok) {
          toast.error("Failed to load hotel details.");
          return;
        }

        const data = await res.json();
        setHotel(data.hotel);
        setReviews(data.reviews || []);
        setAverageRating(data.averageRating ?? 0);
        setHasNextPage(data.hasNextPage);
        setExpediaLink(data.expediaLink || "");
      } catch (error) {
        console.error(error);
        toast.error("Uh oh! Something went wrong!");
      } finally {
        setLoading(false);
      }
    }

    fetchHotelDetails();
  }, [hotelId, page, navigate]);

  async function fetchProsCons() {
    setAiLoading(true);
    try {
      const res = await fetch(
        `${process.env.REACT_APP_BACKEND_URL}/api/ai/hotel/${hotelId}/pro-cons`,
        { credentials: "include" }
      );

      if (!res.ok) {
        const msg = await res.text();
        toast.error(msg || "Failed to fetch AI pros & cons");
        return;
      }

      const data = await res.json();
      setAiProsCons(data.prosCons);
      setAccordionOpen(true); // automatically expand
    } catch (error) {
      console.error(error);
      toast.error("Something went wrong fetching AI pros & cons");
    } finally {
      setAiLoading(false);
    }
  }

  if (loading) {
    return <p className="text-center mt-10">Loading hotel details...</p>;
  }

  if (!hotel) {
    return (
      <div className="text-center mt-10">
        <h2 className="text-red-600">Hotel not found</h2>
        <Link
          to="/hotels"
          className="bg-blue-600 text-white px-4 py-2 rounded mt-4 inline-block"
        >
          Search Again
        </Link>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="flex gap-4 justify-center mb-6">
        <Link to="/" className="bg-blue-600 text-white px-4 py-2 rounded">
          Home
        </Link>
        <Link to="/hotels" className="bg-blue-600 text-white px-4 py-2 rounded">
          Search Hotels
        </Link>
      </div>

      {/* Hotel Info */}
      <div className="text-center mb-6">
        <h1 className="text-3xl font-bold">{hotel.name}</h1>
        <h3>
          {hotel.address}, {hotel.city}, {hotel.state}
        </h3>
        <h3>Average Rating: {Number(averageRating).toFixed(1)} ⭐</h3>
        <h3>
          View on Expedia:{" "}
          {expediaLink ? (
            <a
              href={expediaLink}
              target="_blank"
              rel="noreferrer"
              className="text-blue-600 underline"
            >
              here
            </a>
          ) : (
            <span className="text-gray-500">(link unavailable)</span>
          )}
        </h3>
      </div>

      {/* Action Buttons */}
      <div className="flex justify-center gap-4 mb-6">
        <Link
          to={`/add-review/${hotelId}`}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-500"
        >
          Add Review
        </Link>

        <button
          onClick={fetchProsCons}
          disabled={aiLoading}
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-500 disabled:opacity-50"
        >
          {aiLoading ? "Generating..." : "Generate AI Pros & Cons"}
        </button>
      </div>

      {/* AI Pros & Cons Accordion */}
      {aiProsCons && (
        <div className="max-w-3xl mx-auto mb-6 border rounded shadow bg-white">
          <button
            onClick={() => setAccordionOpen(!accordionOpen)}
            className="w-full text-left px-4 py-2 font-semibold bg-gray-100 hover:bg-gray-200 rounded-t"
          >
            PROS & CONS {accordionOpen ? "▲" : "▼"}
          </button>
          {accordionOpen && (
            <div className="px-4 py-4 whitespace-pre-line">{aiProsCons}</div>
          )}
        </div>
      )}

      {/* Reviews Section */}
      <div className="max-w-3xl mx-auto flex flex-col gap-4">
        {reviews.length === 0 ? (
          <p className="text-center">
            No reviews yet. Consider adding one about your stay!
          </p>
        ) : (
          reviews.map((review) => (
            <div
              key={review.reviewId}
              className="border p-4 rounded shadow bg-white"
            >
              <h3 className="text-xl font-semibold text-center">
                {review.reviewTitle || review.title}
              </h3>
              <p className="text-center">{review.reviewText || review.text}</p>
              <p className="text-center">{review.rating} ⭐</p>
              <p className="text-center text-sm">By: {review.userNickname}</p>
              <p className="text-center text-sm">{review.date}</p>
            </div>
          ))
        )}

        {/* Pagination */}
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
  );
}