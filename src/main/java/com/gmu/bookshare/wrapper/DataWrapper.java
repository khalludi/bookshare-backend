package com.gmu.bookshare.wrapper;

import com.gmu.bookshare.model.ListingDto;
import lombok.Getter;

@Getter
public class DataWrapper {

    private String isbn;

    private String course;

    private int condition;

    private int accessCode;

    private int price;

    private String description;

    public ListingDto toDto() {
        ListingDto listingDto = new ListingDto();
        listingDto.setAccessCode(accessCode);
        listingDto.setCondition(condition);
        listingDto.setCourse(course);
        listingDto.setDescription(description);
        listingDto.setIsbn(Long.valueOf(isbn));
        listingDto.setPrice(price);
        return listingDto;
    }
}
