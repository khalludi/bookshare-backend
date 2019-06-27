package com.gmu.bookshare.service;

import com.gmu.bookshare.entity.BidEntity;
import com.gmu.bookshare.entity.ImageEntity;
import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.entity.ShareUser;
import com.gmu.bookshare.error.ListingNotFoundException;
import com.gmu.bookshare.model.ListingDto;
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

    public ListingEntity addListing(ListingDto listingDto, List<byte[]> images, ShareUser user) {

        ListingEntity listingEntity = listingDto.toEntity();

        user.addListing(listingEntity);

        if (images != null) {
            images.forEach(x -> {
                ImageEntity image = new ImageEntity(x);
                listingEntity.addImages(image);
            });
        }

        return listingRepository.save(listingEntity);
    }


    public ListingEntity getById(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
    }

    ListingEntity getIsbn(long isbn) {
        return listingRepository.findByIsbn(isbn).get(0);
    }

    public ListingEntity updateListing(ListingEntity listingEntity) {
        if (listingRepository.findById(listingEntity.getId()).isPresent())
            listingRepository.save(listingEntity);
        else
            return null;
        return listingEntity;
    }

    public void deleteListing(Long id) {
        listingRepository.deleteById(id);
    }

    public void addBid(Long id, BidEntity bid) {
        getById(id).addBid(bid);
    }
}
