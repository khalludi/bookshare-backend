package com.gmu.bookshare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.model.ListingDto;
import com.gmu.bookshare.service.BidService;
import com.gmu.bookshare.service.ListingService;
import com.gmu.bookshare.service.ShareUserService;
import com.gmu.bookshare.web.BookshareApiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    public void givenListing_whenGetListings_thenReturnJsonArray()
            throws Exception {

        ListingEntity alex = new ListingEntity(123456, 3, 14.99,
                new Date(), 192838079872L, 2879878394L, "Title Calc 3");

        List<ListingEntity> allListingEntities = Arrays.asList(alex);

        given(listingService.getAll()).willReturn(allListingEntities);

        mvc.perform(get("/bs/api/listing/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is(alex.getIsbn())));
    }

    @Test
    public void givenListing_whenPutListings_thenReturnCorrectHttpStatus()
            throws Exception {

        ListingEntity listingInDB = new ListingEntity(123456, 3, 14.99,
                new Date(), 192838079872L, 2879878394L, "Title Calc 3");
        ListingEntity listingNotInDB = new ListingEntity(123456, 3, 14.99,
                new Date(), 192838079872L, 2879878394L, "Title Calc 3");

        ListingDto listingDtoInDB = modelMapper.map(listingInDB, ListingDto.class);
        listingDtoInDB.setCreateDateConverted(listingInDB.getCreateDate());
        ListingDto listingDtoNotInDB = modelMapper.map(listingNotInDB, ListingDto.class);
        listingDtoNotInDB.setCreateDateConverted(listingNotInDB.getCreateDate());

//        ListingDto listingDtoInDB = new ListingDto();
//        listingDtoInDB.setIsbn(123456);
//        listingDtoInDB.setCondition(3);
//        listingDtoInDB.setPrice(14.99);
//        listingDtoInDB.setCreateDateConverted(new Date());
//        listingDtoInDB.setTitle("Title Calc 3");
//        listingDtoInDB.setId(listingInDB.getId());
//
//        ListingDto listingDtoNotInDB = new ListingDto();
//        listingDtoInDB.setIsbn(123456);
//        listingDtoInDB.setCondition(3);
//        listingDtoInDB.setPrice(14.99);
//        listingDtoInDB.setCreateDateConverted(new Date());
//        listingDtoInDB.setTitle("Title Calc 3");
//        listingDtoInDB.setId(listingNotInDB.getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(listingDtoInDB);
        String requestJson2 = ow.writeValueAsString(listingDtoNotInDB);

        given(listingService.updateListing(listingInDB)).willReturn(listingInDB);
        given(listingService.updateListing(listingNotInDB)).willReturn(null);

        mvc.perform(put("/bs/api/listing/" + listingInDB.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());

        mvc.perform(put("/bs/api/listing/" + listingNotInDB.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
                .andExpect(status().isNotFound());
    }
}
