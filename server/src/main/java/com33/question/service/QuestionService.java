package com33.question.service;

import com33.exception.BusinessLogicException;
import com33.exception.ExceptionCode;
import com33.question.entity.Question;
import com33.question.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    public Question creatQuestion(Question question){
        return questionRepository.save(question);
    }
    public Question findQuestion(long question_Id) {
        return findVerifiedQuestionByQuery(question_Id);
    }
    public Question updateQuestion(Question question) {
        Question findQuestion = findVerifiedQuestion(question.getQuestion_id());
        findQuestion.setModifiedAt(LocalDateTime.now());
        Optional.ofNullable(question.getTitle())
                .ifPresent(title -> findQuestion.setTitle(title));
        Optional.ofNullable(question.getContent())
                .ifPresent(content -> findQuestion.setContent(content));
        return questionRepository.save(findQuestion);
    }

//    public Page<Question> findQuestions(int page, int size) {
//        return questionRepository.findAll(PageRequest.of(page, size,
//                Sort.by("question_Id").descending()));
//    }

    public void deleteQuestion(long questionId) {
        Question question = findVerifiedQuestion(questionId);
        questionRepository.delete(question);
    }

    public Question findVerifiedQuestion(long question_Id) {
        Optional<Question> optionalQuestion = questionRepository.findById(question_Id);
        Question findQuestion =
                optionalQuestion.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));

        return findQuestion;
    }


    private Question findVerifiedQuestionByQuery(long question_Id) {
        Optional<Question> optionalQuestion = questionRepository.findByQuestion_id(question_Id);
        Question findQuestion =
                optionalQuestion.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));

        return findQuestion;

    }

    public List<Question> findQuestions(){
        return questionRepository.findAll();
    }
}
