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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ListingDto newListing(@RequestBody ListingDto listingDto) {
        ListingEntity post = convertToEntity(listingDto);
        ListingEntity postCreated = listingService.addListing(post);
        return convertToDto(postCreated);
    }

    @GetMapping(value = "/listing/{id}")
    ListingDto getOne(@PathVariable Long id) {
        return convertToDto(listingService.getById(id));
    }

    @PutMapping(value = "/listing/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ListingDto> updateListing(@RequestBody ListingDto listingDto) {
        ListingEntity listingEntity = convertToEntity(listingDto);
        ListingEntity ret = listingService.updateListing(listingEntity);
        if (ret == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(convertToDto(ret), HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/listing/{id}")
    void deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
    }

    private ListingDto convertToDto(ListingEntity listingEntity) {
        return modelMapper.map(listingEntity, ListingDto.class);
    }

    private ListingEntity convertToEntity(ListingDto listingDto) {
        return modelMapper.map(listingDto, ListingEntity.class);
    }
}
