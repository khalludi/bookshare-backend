/*
    CockroachDB Setup
*/

DROP TABLE IF EXISTS ShareUser;
CREATE TABLE ShareUser (
    id SERIAL PRIMARY KEY,
    email varchar(255),
    name varchar(255)
);

DROP TABLE IF EXISTS Bid;
CREATE TABLE Bid (
    id SERIAL PRIMARY KEY,
    createDate DATE,
    price DECIMAL(5,2),
    flagged INTEGER,
    userId bigint REFERENCES ShareUser(id)
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
    createDate DATE,
    bid_id bigint REFERENCES Bid(id),
    userId bigint REFERENCES ShareUser(id),
    title varchar(255)
);

CREATE INDEX ON Bid (id);
ALTER TABLE Bid ADD CONSTRAINT listingId FOREIGN KEY (id) REFERENCES Listing
