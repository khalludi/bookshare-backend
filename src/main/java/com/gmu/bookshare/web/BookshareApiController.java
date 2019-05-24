package com.gmu.bookshare.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmu.bookshare.entity.BidEntity;
import com.gmu.bookshare.entity.ImageEntity;
import com.gmu.bookshare.entity.ListingEntity;
import com.gmu.bookshare.entity.ShareUser;
import com.gmu.bookshare.model.BidDto;
import com.gmu.bookshare.model.ListingDto;
import com.gmu.bookshare.model.ShareUserDto;
import com.gmu.bookshare.service.BidService;
import com.gmu.bookshare.service.ListingService;
import com.gmu.bookshare.service.ShareUserService;
import com.gmu.bookshare.wrapper.DataWrapper;
import com.gmu.bookshare.wrapper.FormWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bs/api/")
public class BookshareApiController {

    private final ListingService listingService;
    private final BidService bidService;
    private final ShareUserService shareUserService;

    @Value("${secret.apikey}")
    private String API_KEY;

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

    @PostMapping(value = "/listing")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> newListing(@ModelAttribute FormWrapper model) throws Exception {

        // Convert data back to JSON
        ObjectMapper dataMapper = new ObjectMapper();
        DataWrapper dataWrapper = dataMapper.readValue(model.getData(), DataWrapper.class);

        // Convert DataWrapper to ListingDto
        ListingDto listingDto = new ListingDto();
        listingDto.setAccessCode(dataWrapper.getAccessCode());
        listingDto.setCondition(dataWrapper.getCondition());
        listingDto.setCourse(dataWrapper.getCourse());
        listingDto.setDescription(dataWrapper.getDescription());
        listingDto.setIsbn(Long.valueOf(dataWrapper.getIsbn()));

        // Get array of images
        List<byte[]> images = new ArrayList<>();
        if (model.getImages() != null) {
            for (MultipartFile image : model.getImages()) {
                // Set image if provided
                if (image != null && !image.isEmpty()) {
                    try {
                        images.add(image.getBytes());
                        Path path = Paths.get(image.getName());
                        Files.write(path, image.getBytes());
                    } catch (IOException e) {
                        System.err.println("ERROR: Could not get bytes off of image");
                    }
                }
            }
        }

        ShareUser user = shareUserService.getShareUser();
        ListingEntity listingEntity = convertToEntity(listingDto);

        user.addListing(listingEntity);

        images.forEach(x -> {
            ImageEntity image = new ImageEntity(x);
            listingEntity.addImages(image);
        });

        listingService.addListing(listingEntity);

        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);
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
        listingService.addBid(id, bid);
        shareUserService.getShareUser().addBid(bid);
        BidEntity bidCreated = bidService.addListing(bid);

        return convertBidToDto(bidCreated);
    }

    @GetMapping(value = "/user")
    ShareUserDto getUser() {
        ShareUser user = shareUserService.getShareUser();
        return convertShareUserToDto(user);
    }

    @GetMapping(value = "/user/id/{id}")
    ShareUserDto getById(@PathVariable Long id) {
        ShareUser user = shareUserService.getShareUserById(id);
        if (user == null) {
            return new ShareUserDto("", "");
        }
        return convertShareUserToDto(user);
    }

    @GetMapping(value = "/user/bid")
    List<BidDto> getBidsAssociatedWithShareUser() {
        ShareUser shareUser = shareUserService.getShareUser();
        return shareUser.getBidsOwned().stream()
                .map(this::convertBidToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/user/listing")
    List<ListingDto> getListingsAssociatedWithShareUser() {
        ShareUser shareUser = shareUserService.getShareUser();
        return shareUser.getListingsOwned().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void loginRedirect(HttpServletResponse httpServletResponse) {
        String redirectURL = "https://localhost:9090";
        httpServletResponse.setHeader("Location", redirectURL);
        httpServletResponse.setStatus(302);
    }

//    @GetMapping(name = "/login")
//    public String index(ModelMap modelMap) {
//        Authentication auth = SecurityContextHolder.getContext()
//                .getAuthentication();
//        if (auth != null
//                && auth.getPrincipal() != null
//                && auth.getPrincipal() instanceof UserDetails) {
//            modelMap.put("username", ((UserDetails) auth.getPrincipal()).getUsername());
//        }
//        return "secured/index";
//    }

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
        // Add creation date
        listingDto.setCreateDate(new Date());

        // Get title from REST call
        listingDto.setTitle(getTitleFromIsbn(listingDto.getIsbn()));

        return modelMapper.map(listingDto, ListingEntity.class);
    }

    private String getTitleFromIsbn(long isbn) {
        final String uri = "https://www.googleapis.com/books/v1/volumes?q="+isbn+"&key="+API_KEY;

        // Get titles from API using ISBN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        // Create an Jackson mapper
        ObjectMapper mapper = new ObjectMapper();

        // Try to map the JSON, return empty if failed.
        JsonNode root;
        try {
            root = mapper.readTree(response.getBody());
        } catch (IOException e) {
            return "";
        }

        // Get title from path
        String title = "";
        JsonNode itemsNode = root.path("items");
        for (JsonNode node : itemsNode) {
            JsonNode volumeInfo = node.path("volumeInfo");
            title = volumeInfo.path("title").asText();
            break;
        }

        return title;
    }

    private BidDto convertBidToDto(BidEntity bidEntity) {
        return modelMapper.map(bidEntity, BidDto.class);
    }

    private BidEntity convertBidToEntity(BidDto bidDto) {
        return modelMapper.map(bidDto, BidEntity.class);
    }

    private ShareUserDto convertShareUserToDto(ShareUser shareUser) {
        return modelMapper.map(shareUser, ShareUserDto.class);
    }

    private ShareUser convertShareUserToEntity(ShareUserDto shareUserDto) {
        return modelMapper.map(shareUserDto, ShareUser.class);
    }
}
