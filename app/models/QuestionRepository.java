package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPAQuestionRepository.class)
public interface QuestionRepository {

    CompletionStage<Question> add(Question question);

    CompletionStage<Stream<Question>> list();
}
