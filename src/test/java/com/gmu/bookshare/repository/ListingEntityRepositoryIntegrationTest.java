package com.gmu.bookshare.repository;

import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.entity.ShareUser;
import com.gmu.bookshare.persistence.ListingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
        testEntityManager.persistAndFlush(alex);

        // when
        ArrayList<ListingEntity> found = (ArrayList<ListingEntity>) listingRepository.findByIsbn(alex.getIsbn());

        // then
        assertThat(found.get(0).getIsbn())
                .isEqualTo(alex.getIsbn());
    }

    @Test
    public void whenFindById_thenReturnListing() {
        //given
        ShareUser shareUser = new ShareUser("Bobby Jones", "bobbyjones@yahoo.com",
                new HashSet<>(), new HashSet<>());
        ListingEntity book1 = new ListingEntity(new Date(), "Title Calc 3");
        book1.setIsbn(123456);
        book1.setAccessCode(3);
        book1.setPrice(14.99);
        ListingEntity book2 = new ListingEntity(new Date(), "Title Calc 3");
        book2.setIsbn(123456);
        book2.setAccessCode(3);
        book2.setPrice(14.99);
        ListingEntity book3 = new ListingEntity(new Date(), "Title Calc 3");
        book3.setIsbn(123456);
        book3.setAccessCode(3);
        book3.setPrice(14.99);

        shareUser.addListing(book1);
        shareUser.addListing(book2);
        shareUser.addListing(book3);

        testEntityManager.persist(book1);
        testEntityManager.persist(book2);
        testEntityManager.persist(book3);
        testEntityManager.persistAndFlush(shareUser);

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
        ShareUser shareUser = new ShareUser("John Doe", "jdoe@outlook.com",
                new HashSet<>(), new HashSet<>());
        ListingEntity book1 = new ListingEntity(new Date(), "Title Calc 3");
        book1.setIsbn(123456);
        book1.setAccessCode(3);
        book1.setPrice(14.99);

        shareUser.addListing(book1);

        testEntityManager.persistAndFlush(shareUser);

        // when
        Optional<ListingEntity> error = listingRepository.findById(82341792L);

        // then
        assertThat(error.isPresent()).isFalse();
    }
}
