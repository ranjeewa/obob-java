package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
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

    private Question insert(EntityManager em, Question question) {
        em.persist(question);
        return question;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }
}
