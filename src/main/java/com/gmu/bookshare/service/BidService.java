package com.gmu.bookshare.service;

import com.gmu.bookshare.entity.BidEntity;
import com.gmu.bookshare.persistence.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    private final BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public BidEntity addListing(BidEntity bidEntity) {
        return bidRepository.save(bidEntity);
    }
}
