package hongsam.api.question.controller;

import hongsam.api.question.domain.QuestionBasic;
import hongsam.api.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/question")
    public QuestionBasic addQuestion(@RequestBody QuestionBasic newQuestion) {
        return questionService.addQuestion(newQuestion);
    }

    @GetMapping("/question")
    public List<QuestionBasic> getQuestionsOfPage(@RequestParam String button, @RequestParam int level,
                                                  @RequestParam int index, @RequestParam int size) {
        return questionService.getPage(button, level, index, size);
    }
}