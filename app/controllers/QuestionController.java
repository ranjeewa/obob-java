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

import static play.libs.Json.toJson;


public class QuestionController extends Controller {

    private final QuestionRepository questionRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext ec;

    @Inject
    public QuestionController(QuestionRepository questionRepository, FormFactory formFactory, HttpExecutionContext ec) {
        this.questionRepository = questionRepository;
        this.formFactory = formFactory;
        this.ec = ec;
    }


    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> add() {
        Form<Question> questionForm = formFactory.form(Question.class);

        //validate here?
        Question question = questionForm.bindFromRequest().get();

        return questionRepository.add(question).thenApplyAsync(q -> ok(toJson(q)), ec.current());

    }

}
