package com.gmu.bookshare.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "listing")
public class ListingEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "course")
    private String course;

    @NonNull
    @Column(name = "isbn")
    private int isbn;

    @NonNull
    @Column(name = "condition")
    private int condition;

    @Column(name = "accessCode")
    private boolean accessCode;

    @NonNull
    @Column(name = "price")
    private double price;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "bidList")
    @OneToMany
    @JoinTable(name = "Listing2Bids",
            joinColumns = {@JoinColumn(name = "Listing_FK")},
            inverseJoinColumns = {@JoinColumn(name = "Bids_FK")})
    private List<Bid> bidList;

    @NonNull
    @Column(name = "owner")
    private Long owner;

    @NonNull
    @Column(name = "title")
    private String title;
}
