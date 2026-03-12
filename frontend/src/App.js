import './App.css';
import {Routes, Route} from "react-router-dom";
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import {ToastContainer} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import RegisterPage from './pages/RegisterPage';
import SearchHotelsPage from './pages/SearchHotelsPage';
import HotelDetailsPage from './pages/HomeDetailsPage';
import AddReviewPage from './pages/AddReviewPage';

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />}/>
        <Route path="/hotels" element={<SearchHotelsPage />} />
        <Route path ="/hotel/:hotelId" element={<HotelDetailsPage />} />
        <Route path="/add-review/:hotelId" element={<AddReviewPage />} />
      </Routes>
      <ToastContainer position="top-right" autoClose={3000} />
    </>
  );
}

export default App;
