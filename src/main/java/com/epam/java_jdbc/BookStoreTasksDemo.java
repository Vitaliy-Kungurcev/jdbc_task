package com.epam.java_jdbc;

import com.epam.java_jdbc.tasks.BookStoreTasks;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookStoreTasksDemo {
    public static void main(String[] args) throws IOException, SQLException {
        BookStoreTasks bookStoreTasks = new BookStoreTasks();

        bookStoreTasks.getAllAuthors();

        bookStoreTasks.getMostPopularAuthorBook("Asimov");
        bookStoreTasks.getMostPopularAuthorBook("Adams");
        bookStoreTasks.getMostPopularAuthorBook("Le Guin");

        bookStoreTasks.getIncomePerDay(LocalDate.of(2020, 12, 02));

        bookStoreTasks.increasePriceByAuthor("Asimov");

        bookStoreTasks.addNewBookSaleAtCustomDateTime("The End of Eternity",
                LocalDateTime.of(2021, 07, 14, 14, 30, 15));

        bookStoreTasks.addNewBookSaleAtCurrentDateTime(7);

        bookStoreTasks.getLastBookSale("The Two Towers");

        bookStoreTasks.close();
    }
}
