package com.campus.model;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String ISBN;
    private String author;
    private String genre;

    public Book() {
    }

    public Book(String title, String ISBN, String author, String genre) {
        setTitle(title);
        setISBN(ISBN);
        setAuthor(author);
        setGenre(genre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null ) {
            return false;
        }

        if (!(o instanceof Book)) {
            return false;
        }

        Book book = (Book) o;

        if (this.getISBN() == null || this.getTitle() == null || book.getISBN() == null || book.getTitle() == null) {
            return false;
        }

        return this.getISBN().equalsIgnoreCase(book.getISBN()) && this.getTitle().equalsIgnoreCase(book.getTitle());
    }


    public void setTitle(String title) {
        if(title != null && !title.isEmpty()){
            this.title = title;
        } else {
            System.out.println("Invalid Title");
        }
    }

    public void setISBN(String ISBN) {
        if(ISBN != null && !ISBN.isEmpty()){
            this.ISBN = ISBN;
        } else {
            System.out.println("Invalid ISBN");
        }
    }

    public void setAuthor(String author) {
        if(author != null && !author.isEmpty()){
            this.author = author;
        } else {
            System.out.println("Invalid Author");
        }
    }

    public void setGenre(String genre) {
        if(genre != null && !genre.isEmpty()){
            this.genre = genre;
        } else {
            System.out.println("Invalid Genre");
        }
    }

    public String getTitle() {
        return title;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String toString(){
        return String.format("Title: %s\nISBN: %s\nAuthor: %s\nGenre: %s\n", title,ISBN, author, genre);
    }
}
