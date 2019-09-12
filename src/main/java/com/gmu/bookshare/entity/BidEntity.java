package com.gmu.bookshare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Bid")
public class BidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "price")
    private double price;

    @Column(name = "flag")
    private int flagged;

    @Column(name = "userId")
    private Long userId;

//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {
//                    CascadeType.PERSIST,
//                    CascadeType.MERGE
//            },
//            mappedBy = "posts")
//    private Set<ListingEntity> posts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listingId")
    private ListingEntity listing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shareUserId")
    private ShareUser shareUserOwner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BidEntity)) return false;
        return id != null && id.equals(((BidEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
