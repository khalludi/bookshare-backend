package com.gmu.bookshare.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Getter
@Setter
public class ListingDto {

    private final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Long id;

    private String course;

    private int isbn;

    private int condition;

    private boolean accessCode;

    private double price;

    private byte[] image;

    private String description;

    private String createDate;

    private String title;

    public Date getCreateDateConverted() throws ParseException {
        return dateFormat.parse(this.createDate);
    }

    public void setCreateDate(Date date) {
        this.createDate = dateFormat.format(date);
    }
}
