package ge.tsu.Quiz.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@Entity
@Data
@Getter
@Setter
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int questionCount;

    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}