package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.entity.Review;
import com.globalagriculture.backend.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/reviews.js -> read-only, GET /reviews only
@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }
}
