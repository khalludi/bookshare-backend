package com.gmu.bookshare.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidDto {

    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Long id;

    @JsonProperty("createDate")
    private Date createDate;

    @JsonProperty("price")
    private double price;

    @JsonProperty("flag")
    private int flagged;

    @JsonProperty("userId")
    private Long userId;
}
