package com.gmu.bookshare.web;

import com.gmu.bookshare.entity.BidEntity;
import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.model.BidDto;
import com.gmu.bookshare.model.ListingDto;
import com.gmu.bookshare.service.BidService;
import com.gmu.bookshare.service.ListingService;
import com.gmu.bookshare.service.ShareUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public BookshareApiController(ListingService listingService, BidService bidService,
                                  ShareUserService shareUserService) {
        this.listingService = listingService;
        this.bidService = bidService;
        this.shareUserService = shareUserService;
    }

    @GetMapping(value = "/listing", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getListings() {
        List<ListingEntity> listings = listingService.getAll();
        return listings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/listing", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/listing/{id}/bid")
    List<BidDto> getBidsAssociatedWithListing(@PathVariable Long id) {
        ListingEntity listingEntity = listingService.getById(id);
        return listingEntity.getBids().stream()
                .map(this::convertBidToDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/listing/{id}/bid")
    @ResponseStatus(HttpStatus.CREATED)
    public BidDto addBid(@PathVariable Long id, @RequestBody BidDto bidDto) {
        BidEntity bid = convertBidToEntity(bidDto);
        BidEntity bidCreated = bidService.addListing(bid);
        listingService.addBid(id, bidCreated);
        return convertBidToDto(bidCreated);
    }

    @GetMapping(name = "/login")
    public String index(ModelMap modelMap) {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth != null
                && auth.getPrincipal() != null
                && auth.getPrincipal() instanceof UserDetails) {
            modelMap.put("username", ((UserDetails) auth.getPrincipal()).getUsername());
        }
        return "secured/index";
    }

    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response,
            SecurityContextLogoutHandler logoutHandler) {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        logoutHandler.logout(request, response, auth);
        new CookieClearingLogoutHandler(
                AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
                .logout(request, response, auth);
        return "auth/logout";
    }

    private ListingDto convertToDto(ListingEntity listingEntity) {
        return modelMapper.map(listingEntity, ListingDto.class);
    }

    private ListingEntity convertToEntity(ListingDto listingDto) {
        return modelMapper.map(listingDto, ListingEntity.class);
    }

    private BidDto convertBidToDto(BidEntity bidEntity) {
        return modelMapper.map(bidEntity, BidDto.class);
    }

    private BidEntity convertBidToEntity(BidDto bidDto) {
        return modelMapper.map(bidDto, BidEntity.class);
    }
}
