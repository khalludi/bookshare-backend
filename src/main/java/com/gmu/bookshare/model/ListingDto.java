package com.gmu.bookshare.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListingDto {

//    @Getter(AccessLevel.NONE)
//    @Setter(AccessLevel.NONE)
//    private final SimpleDateFormat dateFormat
//            = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @JsonProperty("id")
    private Long id;

    @JsonProperty("course")
    private String course;

    @JsonProperty("isbn")
    private int isbn;

    @JsonProperty("condition")
    private int condition;

    @JsonProperty("accessCode")
    private boolean accessCode;

    @JsonProperty("price")
    private double price;

    @JsonProperty("image")
    private byte[] image;

    @JsonProperty("description")
    private String description;

    @JsonProperty("createDate")
    private Date createDate;

    @JsonProperty("title")
    private String title;

//    public Date getCreateDateConverted() throws ParseException {
//        return dateFormat.parse(this.createDate);
//    }
//
//    public void setCreateDateConverted(Date date) {
//        this.createDate = dateFormat.format(date);
//    }
}
