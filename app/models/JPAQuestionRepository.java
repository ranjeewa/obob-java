package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class JPAQuestionRepository implements QuestionRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAQuestionRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Question> add(Question question) {
        return supplyAsync(() -> wrap(em -> insert(em, question)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Question>> list() {
        return null;
    }

    @Override
    public CompletionStage<Question> get(Long id) {
        return supplyAsync(() -> wrap(em -> get(em, id)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Question>> getForBooks(Long[] bookIds) {
        return supplyAsync(() -> wrap(em -> getForBooks(em, bookIds)), executionContext);
    }


    private Question insert(EntityManager em, Question question) {
        em.persist(question);
        return question;
    }

    private Stream<Question> getForBooks(EntityManager em, Long[] bookIds) {
        return em.createQuery("select q from Question q where q.bookId in :bookIds", Question.class)
                .setParameter("bookIds", Arrays.asList(bookIds))
                .getResultList()
                .stream();
    }

    private Question get(EntityManager em, Long id) {
        return em.createQuery("select q from Question q where q.id = :id", Question.class)
                .setParameter("id", id).getSingleResult();
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }
}
