package com.gmu.bookshare.error;

public class ListingNotFoundException extends RuntimeException {

    public ListingNotFoundException(Long id) {
        super("Could not find listing " + id);
    }
}
