package com.campus.model;
import com.campus.interfaces.Reportable;

import java.util.ArrayList;

public class Library extends Facility implements Reportable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Book> books;
    private int booksIssued;
    private int employeeCount;
    private double salaryPerEmployee;

    public Library(){
        super();
        this.books = new ArrayList<>();
    }

    public Library(String entityID, String name, String location, boolean isBusy, double maintenanceCost, int usageFrequency, String operatingHours, boolean isOpen, ArrayList<Book> books, int employeeCount, double salaryPerEmployee) {
        super(entityID, name, location, isBusy, maintenanceCost, usageFrequency, operatingHours, isOpen);
        setBooks(books);
        setEmployeeCount(employeeCount);
        setSalaryPerEmployee(salaryPerEmployee);
    }

    public String addBook(Book book){
        if (book == null){
            return String.format("Book is null.%n");
        }

        if (books.contains(book)){
            return String.format("Book already exists.%n");
        }

        books.add(book);
        return String.format("Book Added Successfully.%n");
    }

    public String removeBook(Book book){
        if (book == null){
            return String.format("Book is null.%n");
        }

        if (books.contains(book)){
            books.remove(book);
            return String.format("Book Removed Successfully.%n");
        } else {
            return String.format("Book does not exist in the Library.%n");
        }
    }

    @Override
    public String generateReport(){
        return String.format("=====LIBRARY REPORT======%n" +
                " >Library Name: %s%n" +
                " >Library ID: %s%n" +
                " >Location: %s%n" +
                "----------------------------------%n" +
                " >Total Books in Library: %d%n" +
                " >Issued Books: %d%n" +
                " >Available Books: %d%n" +
                " >Total Employees In Library: %d%n" +
                " >Operating Hours: %s%n" +
                "----------------------------------%n" +
                "Operational Cost: %.2f%n" +
                "==================================%n",
                getName(), getEntityID(), getLocation(), books.size(), booksIssued,
                (books.size() - booksIssued), employeeCount, getOperatingHours(),
                calculateOperationalCost());
    }
    @Override
    public double calculateOperationalCost() {
        return ((getMaintenanceCost() * getUsageFrequency()) + getSalaryPerEmployee() * getEmployeeCount());
    }

    public String returnBook(Book book){
        if (book == null) {
            return String.format("Book cannot be null%n");
        }

        for (Book b : books){
            if (b.equals(book)){
                return String.format("Book already exists%n");
            }
        }
        books.add(book);
        if (booksIssued > 0) {
            booksIssued--;
        }
        return String.format("Book returned Successfully%n");
    }

    public String issueBook(Book book){
        if (book == null) {
            return String.format("Book cannot be null%n");
        }

        for (int i = 0; i < books.size(); i++){
            if (books.get(i).equals(book)){
                books.remove(i);
                booksIssued++;
                return String.format("Book Issued Successfully%n--- Details ---%n%s%n", book.toString());
            }
        }
        return String.format("Book Not Found%n");
    }

    public void setSalaryPerEmployee(double salaryPerEmployee){
        if (salaryPerEmployee > 0){
            this.salaryPerEmployee = salaryPerEmployee;
        } else {
            this.salaryPerEmployee = 0;
            System.out.println("Salary per Employee cannot be negative");
        }
    }

    public double getSalaryPerEmployee(){
        return  salaryPerEmployee;
    }

    public void setEmployeeCount(int employeeCount){
        if (employeeCount >= 0){
            this.employeeCount = employeeCount;
        } else  {
            System.out.println("Employee Count not valid");
        }
    }

    public int getEmployeeCount(){
        return employeeCount;
    }

    public void setBooks(ArrayList<Book> books){
        if (books != null){
            this.books = new ArrayList<>(books);
        } else {
            System.out.println("Books list cannot be null");
            this.books = new ArrayList<>();
        }
    }

    public ArrayList<Book> getBooks(){
        return new ArrayList<>(books);
    }

    public void displayAllBooks(){
        if (books == null || books.isEmpty()) {
            System.out.println("No books available");
            return;
        }

        System.out.println("--- All books in this library ---");
        for (Book b : books){
            System.out.println(b.toString());
        }
    }

    public String toString(){
        return super.toString() + String.format("Number of books: %d\nIssued Books: %d\n",books.size(), booksIssued);
    }
}