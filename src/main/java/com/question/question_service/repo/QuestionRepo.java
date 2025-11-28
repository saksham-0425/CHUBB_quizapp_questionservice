package com.question.question_service.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.question.question_service.model.Question;
import java.util.*;

@Repository
public interface QuestionRepo extends MongoRepository<Question, String> {
       List<Question> findByCategoryIgnoreCase(String category);

	   List<Question> findRandomQuestionsByCategory(String category, int numQ);
}
