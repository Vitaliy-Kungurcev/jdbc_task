package com.epam.java_jdbc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_sale")
public class Sale {

    @Id
    @Column(name ="sale_id")
    private int saleId;

    @Column(name ="book_id")
    private int bookId;

    @Column(name ="date_time")
    private LocalDateTime dateTime;

    public Sale() {
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", bookId=" + bookId +
                ", dateTime=" + dateTime +
                '}';
    }
}
