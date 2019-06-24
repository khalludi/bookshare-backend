package com.gmu.bookshare.service;

import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.entity.ShareUser;
import com.gmu.bookshare.persistence.ListingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class ListingEntityServiceIntegrationTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @MockBean
        static ListingRepository listingRepository;

        @Bean
        public ListingService employeeService() {
            return new ListingService(listingRepository);
        }
    }

//    @MockBean
//    private ListingRepository listingRepository;

    @Autowired
    private ListingService listingService;

    @Before
    public void setUp() {
        ShareUser shareUser = new ShareUser("Bob Jones", "bobjones@gmail.com",
                new HashSet<>(), new HashSet<>());
        ListingEntity listingEntity1 = new ListingEntity(new Date(), "Title Calc 3");
        listingEntity1.setIsbn(1234567L);
        listingEntity1.setAccessCode(3);
        listingEntity1.setPrice(14.99);
        ListingEntity listingEntity2 = new ListingEntity(new Date(), "Title Calc 3");
        listingEntity2.setIsbn(1234568L);
        listingEntity2.setAccessCode(3);
        listingEntity2.setPrice(14.99);
        ListingEntity listingEntity3 = new ListingEntity(new Date(), "Title Calc 3");
        listingEntity3.setIsbn(1234569L);
        listingEntity3.setAccessCode(3);
        listingEntity3.setPrice(14.99);

        shareUser.addListing(listingEntity1);
        shareUser.addListing(listingEntity2);
        shareUser.addListing(listingEntity3);

        ArrayList<ListingEntity> l = new ArrayList<>();
        l.add(listingEntity1);

        ArrayList<ListingEntity> allListings = new ArrayList<>();
        allListings.add(listingEntity1);
        allListings.add(listingEntity2);
        allListings.add(listingEntity3);

        Mockito.when(EmployeeServiceImplTestContextConfiguration.listingRepository.findByIsbn(listingEntity1.getIsbn()))
                .thenReturn(l);

        Mockito.when(EmployeeServiceImplTestContextConfiguration.listingRepository.findAll()).thenReturn(allListings);
    }

    @Test
    public void whenValidIsbn_thenListingShouldBeFound() {
        int isbn = 1234567;
        ListingEntity found = listingService.getIsbn(isbn);

        assertThat(found.getIsbn()).isEqualTo(isbn);
    }

    @Test
    public void whenGetAll_thenAllListingsShouldBeFound() {
        List<ListingEntity> listings = listingService.getAll();

        assertThat(listings.size()).isEqualTo(3);
    }

    @Test
    public void whenDeleteByValidId_thenListingShouldBeRemoved() {
        listingService.deleteListing(9182381L);

        Mockito.verify(EmployeeServiceImplTestContextConfiguration.listingRepository, Mockito.times(1)).deleteById(9182381L);
    }

    @Test
    public void whenAddListingByEntity_thenListingShouldBeReturned() {
        ListingEntity listingEntity4 = new ListingEntity(new Date(), "Title Calc 3");
        listingEntity4.setIsbn(1234560);
        listingEntity4.setAccessCode(3);
        listingEntity4.setPrice(14.99);

        Mockito.when(EmployeeServiceImplTestContextConfiguration.listingRepository.save(listingEntity4))
                .thenReturn(listingEntity4);

        listingService.addListing(listingEntity4);
    }
}
