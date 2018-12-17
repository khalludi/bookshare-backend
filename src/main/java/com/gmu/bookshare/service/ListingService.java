package com.gmu.bookshare.service;

import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.error.ListingNotFoundException;
import com.gmu.bookshare.persistence.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    @Autowired
    ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<ListingEntity> getAll() {
        return listingRepository.findAll();
    }

    public ListingEntity addListing(ListingEntity newListingEntity) {
        return listingRepository.save(newListingEntity);
    }


    public ListingEntity getById(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
    }

    ListingEntity getIsbn(int isbn) {
        return listingRepository.findByIsbn(isbn).get(0);
    }

    public void updateListing(ListingEntity listingEntity) {
        listingRepository.save(listingEntity);
    }

    public void deleteListing(Long id) {
        listingRepository.deleteById(id);
    }
}
