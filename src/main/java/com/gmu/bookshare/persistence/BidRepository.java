package com.gmu.bookshare.persistence;

import com.gmu.bookshare.entity.Bid;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BidRepository extends CrudRepository<Bid, Long> {

    List<Bid> findByUserId(Long l);
}
