package com.gmu.bookshare.repository;

import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.persistence.ListingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ListingEntityRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ListingRepository listingRepository;

    @Test
    public void whenFindByIsbn_thenReturnListing() {
        // given
        ListingEntity alex = new ListingEntity();
        testEntityManager.persist(alex);
        testEntityManager.flush();

        // when
        ArrayList<ListingEntity> found = (ArrayList<ListingEntity>) listingRepository.findByIsbn(alex.getIsbn());

        // then
        assertThat(found.get(0).getIsbn())
                .isEqualTo(alex.getIsbn());
    }

    @Test
    public void whenFindById_thenReturnListing() {
        //given
        ListingEntity book1 = new ListingEntity(123456, 3, 14.99,
                new Date(), 192838079872L, 2879878394L, "Title Calc 3");
        ListingEntity book2 = new ListingEntity(123456, 3, 14.99,
                new Date(), 192838079872L, 2879878394L, "Title Calc 3");
        ListingEntity book3 = new ListingEntity(123456, 3, 14.99,
                new Date(), 192838079872L, 2879878394L, "Title Calc 3");
        testEntityManager.persist(book1);
        testEntityManager.persist(book2);
        testEntityManager.persist(book3);

        // when
        Optional<ListingEntity> found2 = listingRepository.findById(book2.getId());
        Optional<ListingEntity> found3 = listingRepository.findById(book3.getId());
        Optional<ListingEntity> found1 = listingRepository.findById(book1.getId());

        // then
        assertThat(found2.isPresent()).isTrue();
        assertThat(found2.get().getId()).isEqualTo(book2.getId());

        assertThat(found3.isPresent()).isTrue();
        assertThat(found3.get().getId()).isEqualTo(book3.getId());

        assertThat(found1.isPresent()).isTrue();
        assertThat(found1.get().getId()).isEqualTo(book1.getId());
    }

    @Test
    public void whenFindByInvalidId_thenReturnEmpty() {
        //given
        ListingEntity book1 = new ListingEntity(123456, 3, 14.99,
                new Date(), 192838079872L, 2879878394L, "Title Calc 3");
        testEntityManager.persistAndFlush(book1);

        // when
        Optional<ListingEntity> error = listingRepository.findById(82341792L);

        // then
        assertThat(error.isPresent()).isFalse();
    }
}
