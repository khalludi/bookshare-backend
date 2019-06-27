package com.gmu.bookshare;

import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.model.ListingDto;
import com.gmu.bookshare.service.BidService;
import com.gmu.bookshare.service.ListingService;
import com.gmu.bookshare.service.ShareUserService;
import com.gmu.bookshare.utils.JsonUtil;
import com.gmu.bookshare.web.BookshareApiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookshareApiController.class)
public class BookshareRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ListingService listingService;

    @MockBean
    private BidService bidService;

    @MockBean
    private ShareUserService shareUserService;

    @WithMockUser(value = "casuser")
    @Test
    public void givenListing_whenGetListings_thenReturnJsonArray()
            throws Exception {

        ListingEntity alex = new ListingEntity(new Date(), "Title Calc 3");
        alex.setIsbn(123456L);
        alex.setAccessCode(3);
        alex.setPrice(14.99);

        List<ListingEntity> allListingEntities = Collections.singletonList(alex);

        given(listingService.getAll()).willReturn(allListingEntities);

        mvc.perform(get("/bs/api/listing/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is((int) alex.getIsbn())));
    }

    @WithMockUser(value = "casuser")
    @Test
    public void givenListing_whenPutListing_thenReturnHttpOk()
            throws Exception {

        ListingEntity listingInDB = new ListingEntity(new Date(), "Title Calc 3");
        listingInDB.setIsbn(123456L);
        listingInDB.setAccessCode(3);
        listingInDB.setPrice(14.99);
        listingInDB.setId(123456L);

        ListingDto listingDtoInDB = modelMapper.map(listingInDB, ListingDto.class);

        given(listingService.updateListing(any(ListingEntity.class))).willReturn(listingInDB);

        String exampleJson = "{'items': ['volumeInfo': {'title': 'Physics'}]}";
        when(restTemplate.getForEntity("https://www.googleapis.com/books/v1/volumes?q=123456&key=api_key", String.class))
                .thenReturn(new ResponseEntity<>(exampleJson, HttpStatus.OK));

        mvc.perform(put("/bs/api/listing/" + listingDtoInDB.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValueAsString(listingDtoInDB)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "casuser")
    @Test
    public void givenListing_whenPutInvalidListing_thenReturnHttpNotFound() throws Exception {
        ListingEntity listingNotInDB = new ListingEntity(new Date(), "Title Calc 3");
        listingNotInDB.setIsbn(123456);
        listingNotInDB.setAccessCode(3);
        listingNotInDB.setPrice(14.99);
        listingNotInDB.setId(123457L);

        ListingDto listingDtoNotInDB = modelMapper.map(listingNotInDB, ListingDto.class);

        given(listingService.updateListing(any(ListingEntity.class))).willReturn(null);

        String exampleJson = "{'items': ['volumeInfo': {'title': 'Physics'}]}";
        when(restTemplate.getForEntity("https://www.googleapis.com/books/v1/volumes?q=123456&key=api_key", String.class))
                .thenReturn(new ResponseEntity<>(exampleJson, HttpStatus.OK));

        mvc.perform(put("/bs/api/listing/" + listingDtoNotInDB.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValueAsString(listingDtoNotInDB)))
                .andExpect(status().isNotFound());
    }


    @WithMockUser(value = "casuser")
    @Test
    public void givenListing_whenDeleteListing_thenReturnHttpOk() throws Exception {
        ListingEntity listingInDB = new ListingEntity(new Date(), "Title Calc 3");
        listingInDB.setIsbn(123456);
        listingInDB.setAccessCode(3);
        listingInDB.setPrice(14.99);
        listingInDB.setId(1324L);

        ListingDto listingDtoInDB = modelMapper.map(listingInDB, ListingDto.class);

        given(listingService.getById(listingInDB.getId())).willReturn(listingInDB);

        mvc.perform(delete("/bs/api/listing/" + listingInDB.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValueAsString(listingDtoInDB)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "casuser")
    @Test
    public void givenFormData_whenAddListing_thenReturnHttpOk() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("images", "image1.jpg", "image/jpg", "some_image_data".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("images", "image2.jpg", "image/jpg", "some_image_data2".getBytes());

        String exampleJson = "{'items': ['volumeInfo': {'title': 'Physics'}]}";
        when(restTemplate.getForEntity("https://www.googleapis.com/books/v1/volumes?q=9786543210&key=api_key", String.class))
                .thenReturn(new ResponseEntity<>(exampleJson, HttpStatus.OK));

        mvc.perform(multipart("/bs/api/listing")
                .file(firstFile)
                .file(secondFile)
                .param("data", "{\"isbn\": \"9786543210\", \"course\": \"TEST 390\", \"condition\": 0, " +
                        "\"accessCode\": 2, \"price\": 26, \"description\": \"Cool shades.\"}")
                .with(csrf()))
                .andExpect(status().is(201))
                .andExpect(content().string("Successfully uploaded!"));
    }
}
