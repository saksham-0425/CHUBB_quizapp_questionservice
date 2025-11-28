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
import com.question.question_service.model.QuestionWrapper;
import com.question.question_service.model.Response;
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


	 public ResponseEntity<List<String>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
		    ResponseEntity<List<Question>> response = getRandomQuestionsByCategory(categoryName, numQuestions);
		    List<Question> questions = response.getBody();

		    List<String> questionIds = new ArrayList<>();

		    for (Question q : questions) {
		        questionIds.add(q.getId().toString());  
		    }

		    return new ResponseEntity<>(questionIds, HttpStatus.OK);
		}


	 public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<String> questionIds) {
		List<QuestionWrapper> wrappers=new ArrayList<>();
		
		List<Question> questions = new ArrayList<>();
		for(String id:questionIds) {
			questions.add(questionRepo.findById(id).get());
		}
		
		for(Question question : questions) {
			QuestionWrapper wrapper = new QuestionWrapper();
			wrapper.setId(question.getId());
			wrapper.setTitle(question.getTitle());
			wrapper.setOptions(question.getOptions());
			wrappers.add(wrapper);
		}
		
		return new ResponseEntity<>(wrappers, HttpStatus.OK);
	 }


	 public ResponseEntity<Integer> getScore(List<Response> responses) {
			int right=0;
			
			for(Response response: responses) {
				Question question = questionRepo.findById(response.getId()).get();
				if(response.getResponse().equals(question.getRight_answer())) {
					right++;
				}
				
			}
			return new ResponseEntity<>(right, HttpStatus.OK);
	 }
     
	
}
