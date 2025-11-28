package com.question.question_service.model;

import org.springframework.data.annotation.Id;

import jakarta.annotation.Generated;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection="question")

public class Question {
	@Id
    private String id;
    private String title;
    private String category;
    private String difficultylevel;
    private Options options;
    private String right_answer;
    
    
    
}
