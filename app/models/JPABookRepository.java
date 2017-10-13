package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class JPABookRepository implements BookRepository{

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPABookRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }


    @Override
    public CompletionStage<Book> add(Book book) {
        return supplyAsync(() -> wrap(em -> insert(em, book)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Book>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }


    public CompletionStage<Book> get(Long id) {
        return supplyAsync(() -> wrap(em-> get(em, id)), executionContext);
    }

    private Book insert(EntityManager em, Book book) {
        em.persist(book);
        return book;
    }

    private Stream<Book> list(EntityManager em) {
        List<Book> books = em.createQuery("select b from Book b", Book.class).getResultList();
        return books.stream();
    }

    private Book get(EntityManager em, Long id) {
        return em.createQuery("select b from Book b where b.id = :id", Book.class)
                .setParameter("id", id).getSingleResult();
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

}
