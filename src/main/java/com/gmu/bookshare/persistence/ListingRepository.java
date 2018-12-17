package com.gmu.bookshare.persistence;

import com.gmu.bookshare.entity.ListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingRepository extends JpaRepository<ListingEntity, Long> {

    List<ListingEntity> findByTitle(String s);

    List<ListingEntity> findByIsbn(int isbn);
}
