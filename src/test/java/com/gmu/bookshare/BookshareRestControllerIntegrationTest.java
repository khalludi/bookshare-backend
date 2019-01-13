package com.gmu.bookshare;

import com.gmu.bookshare.config.ApiSecurityConfiguration;
import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.model.ListingDto;
import com.gmu.bookshare.service.BidService;
import com.gmu.bookshare.service.ListingService;
import com.gmu.bookshare.service.ShareUserService;
import com.gmu.bookshare.utils.JsonUtil;
import com.gmu.bookshare.web.BookshareApiController;
import com.kakawait.spring.boot.security.cas.autoconfigure.CasSecurityProperties;
import com.kakawait.spring.security.cas.client.ticket.ProxyTicketProvider;
import com.kakawait.spring.security.cas.client.validation.AssertionProvider;
import org.jasig.cas.client.validation.TicketValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookshareApiController.class)
public class BookshareRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private ListingService listingService;

    @MockBean
    private BidService bidService;

    @MockBean
    private ShareUserService shareUserService;

    @MockBean
    private ProxyTicketProvider proxyTicketProvider;

    @MockBean
    private AssertionProvider assertionProvider;

    @MockBean
    private CasSecurityProperties casSecurityProperties;

    @MockBean
    private CasAuthenticationEntryPoint casAuthenticationEntryPoint;

    @MockBean
    private TicketValidator ticketValidator;

    @MockBean
    private ServiceProperties serviceProperties;

//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mvc;
//
//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }

    @WithMockUser(value = "spring")
    @Test
    public void givenListing_whenGetListings_thenReturnJsonArray()
            throws Exception {

        ListingEntity alex = new ListingEntity(123456, 3, 14.99,
                new Date(), "Title Calc 3");

        List<ListingEntity> allListingEntities = Collections.singletonList(alex);

        given(listingService.getAll()).willReturn(allListingEntities);

        mvc.perform(get("/bs/api/listing/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is(alex.getIsbn())));
    }

    @WithMockUser(value = "spring")
    @Test
    public void givenListing_whenPutListing_thenReturnHttpOk()
            throws Exception {

        ListingEntity listingInDB = new ListingEntity(123456, 3, 14.99,
                new Date(), "Title Calc 3");
        ListingEntity listingNotInDB = new ListingEntity(123456, 3, 14.99,
                new Date(), "Title Calc 3");
        listingInDB.setId(123456L);
        listingNotInDB.setId(123457L);

        ListingDto listingDtoInDB = modelMapper.map(listingInDB, ListingDto.class);
        ListingDto listingDtoNotInDB = modelMapper.map(listingNotInDB, ListingDto.class);

        given(listingService.updateListing(any(ListingEntity.class))).willReturn(listingInDB);

        mvc.perform(put("/bs/api/listing/" + listingDtoInDB.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValueAsString(listingDtoInDB)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void givenListing_whenPutInvalidListing_thenReturnHttpNotFound() throws Exception {
        ListingEntity listingNotInDB = new ListingEntity(123456, 3, 14.99,
                new Date(), "Title Calc 3");
        listingNotInDB.setId(123457L);

        ListingDto listingDtoNotInDB = modelMapper.map(listingNotInDB, ListingDto.class);

        given(listingService.updateListing(any(ListingEntity.class))).willReturn(null);

        mvc.perform(put("/bs/api/listing/" + listingDtoNotInDB.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValueAsString(listingDtoNotInDB)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "spring")
    @Test
    public void givenListing_whenDeleteListing_thenReturnHttpOk() throws Exception {
        ListingEntity listingInDB = new ListingEntity(123456, 3, 14.99,
                new Date(), "Title Calc 3");
        listingInDB.setId(1324L);

        ListingDto listingDtoInDB = modelMapper.map(listingInDB, ListingDto.class);

        given(listingService.getById(listingInDB.getId())).willReturn(listingInDB);

        mvc.perform(delete("/bs/api/listing/" + listingInDB.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValueAsString(listingDtoInDB)))
                .andExpect(status().isOk());
    }
}
