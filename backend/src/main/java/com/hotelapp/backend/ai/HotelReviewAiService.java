package com.hotelapp.backend.ai;

import com.hotelapp.backend.entities.Reviews;
import com.hotelapp.backend.repositories.ReviewRepository;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelReviewAiService {
    private final ChatLanguageModel model;
    private final ReviewRepository reviewRepository;

    public HotelReviewAiService(ChatLanguageModel model,  ReviewRepository reviewRepository){
        this.model = model;
        this.reviewRepository = reviewRepository;
    }

    public String generateProsAndCons(String hotelId){
        List<Reviews> reviews = reviewRepository.findByHotelIdOrderByDateDesc(hotelId, org.springframework.data.domain.PageRequest.of(0, 50))
                .getContent();

        if(reviews.isEmpty()){
            return "No reviews found for this hotel.";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(Reviews review : reviews){
            stringBuilder.append("- ").append(review.getText()).append("\n");
        }

        String chatPrompt = "Summarize the following reviews into pros and cons, listing the top 5 pros and cons"
                + stringBuilder.toString();

        return model.generate(chatPrompt);
    }
}
