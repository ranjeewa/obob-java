package models;

import javax.persistence.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String question;

    public String answer;

    public Integer pageNumber;

    public Integer chapter;

    @Column(nullable = false)
    public Long bookId;
}
