package com.epam.java_jdbc.tasks;

import com.epam.java_jdbc.configuration.ConnectionManager;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookStoreTasks {

    ConnectionManager connectionManager = new ConnectionManager();

    public BookStoreTasks() throws IOException {
    }

    //    Вернуть список всех авторов, представленных в книжном магазине, в формате инициалов имен и
    //     фамилии, например `J. R. R. Tolkien`. Сортировать по фамилии в алфавитном порядке.
    public List<String> getAllAuthors() {
        List<String> allAuthors = new ArrayList<>();
        String query = "SELECT CONCAT (" +
                "COALESCE(first_name,''),' ', COALESCE(middle_name,''),' ',COALESCE(last_name,'')) " +
                "FROM author ORDER BY last_name";

        Statement statement = null;
        try {
            statement = connectionManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String resultSetString = resultSet.getString(1);
                String[] strings = resultSetString.split(" +");
                StringBuilder nameAuthor = new StringBuilder();
                for (int i = 0; i < strings.length + 1; i++) {
                    if (i == strings.length - 1) {
                        nameAuthor.append(strings[i]);
                        break;
                    }
                    nameAuthor.append(strings[i].charAt(0));
                    nameAuthor.append(".");
                }
                allAuthors.add(nameAuthor.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAuthors;
    }

    // Найти самую популярную книгу автора по его фамилии. Предусмотреть исключение, если такой автор не
    // найден. В сообщении исключения должна быть фамилия автора. Если книг этого автора нет или они не
    // продавались, вернуть `null`. Выполнить этот метод для `Asimov`, `Adams` и `Le Guin`.
    public String getMostPopularAuthorBook(String lastName) {
        String bookName;
        String queryCheck = "SELECT first_name FROM author WHERE last_name=?";

        String querySelect = "SELECT book_name " +
                "FROM author NATURAL JOIN book NATURAL JOIN book_sale " +
                "WHERE last_name=? " +
                "GROUP BY book_id ORDER BY COUNT(book_id) DESC ";
        try {
            PreparedStatement statement = connectionManager.getConnection().prepareStatement(queryCheck);
            statement.setString(1, lastName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                statement = connectionManager.getConnection().prepareStatement(querySelect);
                statement.setString(1, lastName);
                ResultSet resultSet2 = statement.executeQuery();
                if (resultSet2.next()) {
                    bookName = resultSet2.getString(1);
                } else return null;
            } else throw new SQLException();
        } catch (SQLException e) {
            return "The request with the author's name: " + lastName + " threw an error: " + e;
        }
        return bookName;
    }

    // Подсчитать выручку магазина за конкретную дату (класс даты-параметра - LocalDate). Подсчитать
    // выручку магазина за 2020-12-02.
    public int getIncomePerDay(LocalDate date) throws SQLException {
        int incomePerDay = 0;
        String query = "SELECT SUM(price) FROM  book NATURAL JOIN book_sale WHERE date_time BETWEEN ? AND ? ";

        PreparedStatement statement = connectionManager.getConnection().prepareStatement(query);
        statement.setString(1, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        statement.setString(2, date.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        incomePerDay = resultSet.getInt(1);

        return incomePerDay;
    }


    // Увеличить стоимость книг автора на 10%, вернуть количество таких книг. Имя автора должно быть
    // параметром этого метода.
    public int increasePriceByAuthor(String lastName) {
        int countBookByAuthor = 0;
        String queryUpdate = "UPDATE book_store.book natural join book_store.author " +
                "SET book_store.book.price = book_store.book.price * 1.1 where last_name=? ";
        String querySelect = "select count(last_name) from book_store.author natural join book_store.book  where last_name=?";

        PreparedStatement statement = null;
        try {
            statement = connectionManager.getConnection().prepareStatement(querySelect);
            statement.setString(1, lastName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            countBookByAuthor = resultSet.getInt(1);
            if (countBookByAuthor == 0) {
                throw new SQLException();
            }
            statement = connectionManager.getConnection().prepareStatement(queryUpdate);
            statement.setString(1, lastName);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("The author with the surname " + lastName + " is not in the database");
        }
        return countBookByAuthor;
    }


    // Добавить запись о новой проданной книге по ее имени на конкретную дату и время (класс параметра -
    // LocalDateTime). Предусмотреть исключение при вводе несуществующего имени.
    public void addNewBookSaleAtCustomDateTime(String bookName, LocalDateTime localDateTime) {
        String querySelect = "SELECT book_id from book where book_name=?";
        String queryInsert = "INSERT INTO book_sale(book_id, date_time) VALUES (?,?)";
        int idBook;

        PreparedStatement statement = null;
        try {
            statement = connectionManager.getConnection().prepareStatement(querySelect);
            statement.setString(1, bookName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idBook = resultSet.getInt(1);
                statement = connectionManager.getConnection().prepareStatement(queryInsert);
                statement.setInt(1, idBook);
                statement.setString(2, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                statement.executeUpdate();
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("The book with the name " + bookName + " is not in the database");
        }
    }

    // task 6
    public int addNewBookSaleAtCurrentDateTime(int bookId) throws SQLException {
        int price = 0;

        CallableStatement statement = null;
        try {
            statement = connectionManager.getConnection().prepareCall("{Call add_book_sale(?,?)}");
            statement.setInt(1, bookId);
            statement.registerOutParameter(2, Types.INTEGER);
            statement.execute();
            price = statement.getInt("price");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("The book with id " + bookId + " is not in the database");
        }
        return price;
    }

    // task 7
    public LocalDateTime getLastBookSale(String bookName) throws IOException {
        String querySelect = "select max(date_time) from book_sale natural join book where book_name=?";
        LocalDateTime localDateTimeLastSale = null;

        PreparedStatement statement = null;
        try {
            statement = statement = connectionManager.getConnection().prepareStatement(querySelect);
            statement.setString(1, bookName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String datetimeString = resultSet.getString(1);
            if (datetimeString == null) {
                throw new SQLException();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(datetimeString);
            sb.replace(10, 11, "T");

            localDateTimeLastSale = LocalDateTime.parse(sb.toString());

        } catch (SQLException e) {
            System.out.println("The book with the name " + bookName + " is not in the database");
        }
        return localDateTimeLastSale;
    }

    public void close() throws SQLException {
        connectionManager.closeConnection();
    }
}
