package com.gmu.bookshare.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bid")
@Getter
@Setter
public class Bid {

    @Id
    @GeneratedValue
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

    @Column(name = "listingId")
    private Long listingId;
}
