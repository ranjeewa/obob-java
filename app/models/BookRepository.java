package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPABookRepository.class)
public interface BookRepository {

    CompletionStage<Book> add(Book book);

    CompletionStage<Stream<Book>> list();

    CompletionStage<Book> get(Long bookId);
}
