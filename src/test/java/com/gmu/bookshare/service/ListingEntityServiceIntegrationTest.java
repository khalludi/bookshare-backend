package com.gmu.bookshare.service;

import com.gmu.bookshare.entity.ListingEntity;
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
        ListingEntity listingEntity1 = new ListingEntity(123456, 3, 14.99,
                new Date(), 192838079872L, 2879878394L, "Title Calc 3");

        ArrayList<ListingEntity> l = new ArrayList<>();
        l.add(listingEntity1);

        Mockito.when(EmployeeServiceImplTestContextConfiguration.listingRepository.findByIsbn(listingEntity1.getIsbn()))
                .thenReturn(l);
    }

    @Test
    public void whenValidIsbn_thenListingShouldBeFound() {
        int isbn = 123456;
        ListingEntity found = listingService.getIsbn(isbn);

        assertThat(found.getIsbn()).isEqualTo(isbn);
    }
}
