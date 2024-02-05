package hongsam.api.question.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class QuestionBasic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    private String title;
    private String level;

    public QuestionBasic() {
    }
}