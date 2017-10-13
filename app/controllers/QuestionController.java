package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Book;
import models.BookRepository;
import models.Question;
import models.QuestionRepository;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class QuestionController extends Controller {

    private final QuestionRepository questionRepository;
    private final BookRepository bookRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext ec;

    @Inject
    public QuestionController(QuestionRepository questionRepository, BookRepository bookRepository, FormFactory formFactory, HttpExecutionContext ec) {
        this.questionRepository = questionRepository;
        this.bookRepository = bookRepository;
        this.formFactory = formFactory;
        this.ec = ec;
    }


    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> add() {
        Form<Question> questionForm = formFactory.form(Question.class);

        Question question = questionForm.bindFromRequest().get();
        JsonNode node = request().body().asJson();
        Long bookId = node.findPath("bookId").asLong();
        return bookRepository.get(bookId).thenApplyAsync(book -> {
            question.bookId = book.id;
            questionRepository.add(question);
            return redirect(routes.BookController.getBooks());
        });
    }

}
