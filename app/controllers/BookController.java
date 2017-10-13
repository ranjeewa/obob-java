package controllers;

import models.BookRepository;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

public class BookController extends Controller {

    private final BookRepository bookRepository;
    private final HttpExecutionContext ec;

    @Inject
    public BookController(BookRepository bookRepository, HttpExecutionContext ec) {
        this.bookRepository = bookRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.books.render());
    }


    public CompletionStage<Result> getBooks() {
        return bookRepository.list().thenApplyAsync(bookStream -> {
            return ok(toJson(bookStream.collect(Collectors.toList())));
        }, ec.current());
    }
}
