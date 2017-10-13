package models;


import javax.persistence.*;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    public String author;

    @OneToMany
    @JoinColumn(name = "bookId", referencedColumnName = "id", insertable = false, updatable = false)
    private List<Question> questions;

}
