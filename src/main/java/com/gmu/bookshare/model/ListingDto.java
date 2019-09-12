package com.gmu.bookshare.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmu.bookshare.entity.ListingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListingDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("course")
    private String course;

    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("condition")
    private int condition;

    @JsonProperty("accessCode")
    private int accessCode;

    @JsonProperty("price")
    private double price;

    @JsonProperty("image")
    private List<byte[]> image;

    @JsonProperty("description")
    private String description;

    @JsonProperty("createDate")
    private Date createDate;

    @JsonProperty("title")
    private String title;

    public ListingEntity toEntity() {
        ListingEntity listingEntity = new ListingEntity();

        listingEntity.setCourse(course);
        listingEntity.setIsbn(isbn);
        listingEntity.setCondition(condition);
        listingEntity.setAccessCode(accessCode);
        listingEntity.setPrice(price);
        listingEntity.setDescription(description);
        listingEntity.setCreateDate(new Date());
        listingEntity.setTitle(title);

        return listingEntity;
    }

    public boolean checkFields() {
        return isbn >= 100000000 && !(price <= 0) && title != null;
    }
}
