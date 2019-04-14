/*
    CockroachDB Setup
*/

DROP TABLE IF EXISTS ShareUser;
CREATE TABLE ShareUser (
    id SERIAL PRIMARY KEY,
    email varchar(255),
    name varchar(255)
);

DROP TABLE IF EXISTS Listing;
CREATE TABLE Listing (
    id SERIAL PRIMARY KEY,
    course varchar(100),
    isbn INTEGER,
    condition INTEGER,
    accessCode INTEGER,
    price DECIMAL(5,2),
    image bytea,
    description varchar(1000),
    createDate TIMESTAMPTZ,
    shareUserId bigint NOT NULL,
    CONSTRAINT user_fk FOREIGN KEY (shareUserId) REFERENCES ShareUser (id),
    title varchar(255)
);

DROP TABLE IF EXISTS Bid;
CREATE TABLE Bid (
    id SERIAL PRIMARY KEY,
    createDate DATE,
    price DECIMAL(5,2),
    flagged INTEGER,
    listingId bigint NOT NULL,
    CONSTRAINT listing_fk FOREIGN KEY (listingId) REFERENCES Listing (id),
    shareUserId bigint NOT NULL,
    CONSTRAINT shareUser_fk FOREIGN KEY (shareUserId) REFERENCES ShareUser (id)
);

--DROP TABLE IF EXISTS listing2bids;
--CREATE TABLE Listing2Bids (
--    listingId bigint NOT NULL,
--    bidId bigint NOT NULL,
--    CONSTRAINT ListingBid_pk PRIMARY KEY (listingId, bidId),
--    CONSTRAINT a_r_fk FOREIGN KEY (listingId) REFERENCES Listing(id),
--    CONSTRAINT b_r_fk FOREIGN KEY (bidId) REFERENCES Bid(id)
--);
