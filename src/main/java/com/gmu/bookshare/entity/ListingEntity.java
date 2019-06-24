package com.gmu.bookshare.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "Listing")
public class ListingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "course")
    private String course;

    @Column(name = "isbn")
    private long isbn;

    @Column(name = "condition")
    private int condition;

    @Column(name = "accessCode")
    private int accessCode;

    @Column(name = "price")
    private double price;

    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "createDate")
    private Date createDate;

    @NonNull
    @Column(name = "title")
    private String title;

    //    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {
//                    CascadeType.PERSIST,
//                    CascadeType.MERGE
//            })
//    @JoinTable(name = "listing2bids",
//            joinColumns = { @JoinColumn(name = "listingId") },
//            inverseJoinColumns = { @JoinColumn(name = "bidId") })
    @OneToMany(
            mappedBy = "listing",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<BidEntity> bids = new HashSet<>();

    @OneToMany(
            mappedBy = "image",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ImageEntity> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shareUserId")
    private ShareUser shareUser;


    public void addBid(BidEntity bid) {
        bids.add(bid);
        bid.setListing(this);
    }

    public void removeBid(BidEntity bid) {
        bids.remove(bid);
        bid.setListing(null);
    }

    public void addImages(ImageEntity image) {
        images.add(image);
        image.setListing(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListingEntity)) return false;
        return id != null && id.equals(((ListingEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 71;
    }
}
