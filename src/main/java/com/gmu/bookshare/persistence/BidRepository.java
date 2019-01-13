package com.gmu.bookshare.persistence;

import com.gmu.bookshare.entity.BidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<BidEntity, Long> {

    List<BidEntity> findByUserId(Long l);
}
