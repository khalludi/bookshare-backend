package com.gmu.bookshare.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "Image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listingId")
    private ListingEntity listing;

    @NonNull
    @Column(name = "image")
    private byte[] image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageEntity)) return false;
        return id != null && id.equals(((ImageEntity) o).id);
    }

    @Override
    public int hashCode() {
        return image.length * image[image.length / 2];
    }

}
