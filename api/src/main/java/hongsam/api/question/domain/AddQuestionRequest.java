package hongsam.api.question.domain;

import lombok.Data;

@Data
public class AddQuestionRequest {
    private String title;
    private String level;
}