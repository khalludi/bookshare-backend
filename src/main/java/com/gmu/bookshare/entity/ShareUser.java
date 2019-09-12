package com.gmu.bookshare.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ShareUser")
public class ShareUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NonNull
    @Column(name = "email")
    private String email;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @OneToMany(mappedBy = "shareUser")
    private Set<ListingEntity> listingsOwned;

    @NonNull
    @OneToMany(mappedBy = "shareUserOwner")
    private Set<BidEntity> bidsOwned;

    public void addBid(BidEntity bid) {
        bidsOwned.add(bid);
        bid.setShareUserOwner(this);
    }

    public void removeBid(BidEntity bid) {
        bidsOwned.remove(bid);
        bid.setShareUserOwner(null);
    }

    public void addListing(ListingEntity listing) {
        listingsOwned.add(listing);
        listing.setShareUser(this);
    }

    public void removeListing(ListingEntity listing) {
        listingsOwned.remove(listing);
        listing.setShareUser(null);
    }
}
