package com.question.question_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.question.question_service.service.QuestionService;
import java.util.*;
import com.question.question_service.model.Question;
import com.question.question_service.model.QuestionWrapper;
import com.question.question_service.model.Response;

@RestController
@RequestMapping("Question")
public class QuestionController {
	
	@Autowired
	QuestionService questionService;
	
	@GetMapping("allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
		return questionService.getAllQuestions();
	}
	
	@GetMapping("category/{category}")
	public ResponseEntity<List<Question>> getQuestionByCategory(@PathVariable String category){
		return questionService.getQuestionsByCategory(category);
	}
	
	@PostMapping("add")
	public ResponseEntity<String> addQuestion(@RequestBody Question question) {
		return questionService.addQuestion(question);
	}
	
	// now we have to work upon 3 more functions to make the service completely separate
	
	@GetMapping("generate")
	public ResponseEntity<List<String>> getQuestionsForQuiz(
	        @RequestParam String categoryName,
	        @RequestParam Integer numQuestions) {

	    return questionService.getQuestionsForQuiz(categoryName, numQuestions);
	} 
	
	@PostMapping("getQuestions")
	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<String> QuestionIds){
		return questionService.getQuestionsFromId(QuestionIds);
	}
	
	@PostMapping("getScore")
	public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses){
		return questionService.getScore(responses);
	}
}
