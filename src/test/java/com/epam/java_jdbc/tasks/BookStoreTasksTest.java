package com.epam.java_jdbc.tasks;

import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class BookStoreTasksTest {

    BookStoreTasks bookStoreTasks = new BookStoreTasks();

    public BookStoreTasksTest() throws IOException {
    }

    @Test
    public void getAllAuthors() throws SQLException, IOException {
        List<String> authors = List.of("D.N.Adams", "I.Asimov", "R.D.Bradbury", "G.Orwell", "J.R.R.Tolkien");
        assertEquals(bookStoreTasks.getAllAuthors(), authors);
    }

    @Test
    public void getMostPopularAuthorBook() throws IOException {
        String popularBookAsimov = "I, Robot";
        String popularBookBradbury = "Fahrenheit 451";
        String popularBookTolkien = "The Two Towers";

        assertEquals(bookStoreTasks.getMostPopularAuthorBook("Asimov"), popularBookAsimov);
        assertEquals(bookStoreTasks.getMostPopularAuthorBook("Bradbury"), popularBookBradbury);
        assertEquals(bookStoreTasks.getMostPopularAuthorBook("Tolkien"), popularBookTolkien);
        assertNull(bookStoreTasks.getMostPopularAuthorBook("Adams"));
    }


    @Test
    public void getIncomePerDay() throws SQLException, IOException {
        int incomePerDay2020_12_02 = 2310;

        assertEquals(bookStoreTasks.getIncomePerDay(LocalDate.of(2020, 12, 02)), incomePerDay2020_12_02);
        assertEquals(bookStoreTasks.getIncomePerDay(LocalDate.of(2000, 01, 01)),0);
    }

    @Test
    public void increasePriceByAuthor() {
        int countBookByAsimov=3;
        int countBookByBradbury=2;

        assertEquals( bookStoreTasks.increasePriceByAuthor("Asimov"),countBookByAsimov);
        assertEquals( bookStoreTasks.increasePriceByAuthor("Bradbury"),countBookByBradbury);
    }


    @Test
    public void addNewBookSaleAtCurrentDateTime() throws SQLException {
        int priceBookId7 = 150;

        assertEquals(bookStoreTasks.addNewBookSaleAtCurrentDateTime(7),150);
    }

    @Test
    public void getLastBookSale() throws IOException {
        LocalDateTime localDateTimeLastSaleTheTwoTowers=
                LocalDateTime.of(2020,12,07 ,17,06,25);

        assertEquals(bookStoreTasks.getLastBookSale("The Two Towers"),localDateTimeLastSaleTheTwoTowers);
    }
}