$ ->
  $.get "/books", (books) ->
    $.each books, (index, book) ->
      $("#books").append $("<li>").text book.name