package com.gmu.bookshare.web;

import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.model.ListingDto;
import com.gmu.bookshare.service.BidService;
import com.gmu.bookshare.service.ListingService;
import com.gmu.bookshare.service.ShareUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bs/api/")
public class BookshareApiController {

    private final ListingService listingService;
    private final BidService bidService;
    private final ShareUserService shareUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public BookshareApiController(ListingService listingService, BidService bidService, ShareUserService shareUserService) {
        this.listingService = listingService;
        this.bidService = bidService;
        this.shareUserService = shareUserService;
    }

    @GetMapping(value = "/listing/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getListings() {
        List<ListingEntity> listings = listingService.getAll();
        return listings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/listing/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ListingDto newListing(@RequestBody ListingDto listingDto) throws ParseException {
        ListingEntity post = convertToEntity(listingDto);
        ListingEntity postCreated = listingService.addListing(post);
        return convertToDto(postCreated);
    }

    @GetMapping(value = "/listing/{id}")
    ListingDto getOne(@PathVariable Long id) {
        return convertToDto(listingService.getById(id));
    }

    @PutMapping(value = "/listing/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateListing(@RequestBody ListingDto listingDto) throws ParseException {
        ListingEntity listingEntity = convertToEntity(listingDto);
        listingService.updateListing(listingEntity);
    }

    private ListingDto convertToDto(ListingEntity listingEntity) {
        ListingDto listingDto = modelMapper.map(listingEntity, ListingDto.class);
        listingDto.setCreateDate(listingEntity.getCreateDate());
        return listingDto;
    }

    private ListingEntity convertToEntity(ListingDto listingDto) throws ParseException {
        ListingEntity post = modelMapper.map(listingDto, ListingEntity.class);
        post.setCreateDate(listingDto.getCreateDateConverted());
        return post;
    }
}
