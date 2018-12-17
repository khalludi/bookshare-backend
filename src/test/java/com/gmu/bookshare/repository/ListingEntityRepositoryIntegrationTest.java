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
}
