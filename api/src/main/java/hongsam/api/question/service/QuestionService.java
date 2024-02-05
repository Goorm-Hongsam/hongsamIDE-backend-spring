package hongsam.api.question.service;

import hongsam.api.question.domain.QuestionBasic;
import hongsam.api.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionBasic addQuestion(QuestionBasic newQuestion) {
        return questionRepository.save(newQuestion);
    }

    public List<QuestionBasic> getAllQuestion() {
        return questionRepository.findAll();
    }

    public List<QuestionBasic> getPage(String buttonType, int level, int index, int size) {
        return questionRepository.findQuestion(buttonType, level, index, size);
    }

}
