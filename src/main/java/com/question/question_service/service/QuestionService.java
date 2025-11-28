package com.question.question_service.service;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.question.question_service.model.Question;
import com.question.question_service.repo.QuestionRepo;

@Service
public class QuestionService {
	
	@Autowired
	QuestionRepo questionRepo;
	
	  @Autowired
	    private MongoTemplate mongoTemplate;

	public ResponseEntity<List<Question>> getAllQuestions(){
		try {
			return new ResponseEntity<>(questionRepo.findAll(), HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}


	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
		try {
			return new ResponseEntity<>(questionRepo.findByCategoryIgnoreCase(category), HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		
	}


	public ResponseEntity<String> addQuestion(Question question) {
		questionRepo.save(question);
		return new ResponseEntity<>("success", HttpStatus.CREATED);
		
	}
	
	 public ResponseEntity<List<Question>> getRandomQuestionsByCategory(String category, int numQ) {
	        try {
	            MatchOperation matchStage =
	                Aggregation.match(Criteria.where("category").regex("^" + category + "$", "i"));

	            SampleOperation sampleStage = Aggregation.sample(numQ);

	            Aggregation aggregation = Aggregation.newAggregation(matchStage, sampleStage);

	            List<Question> randomQuestions =
	                mongoTemplate.aggregate(aggregation, "question", Question.class).getMappedResults();

	            return new ResponseEntity<>(randomQuestions, HttpStatus.OK);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	    }
	
}
