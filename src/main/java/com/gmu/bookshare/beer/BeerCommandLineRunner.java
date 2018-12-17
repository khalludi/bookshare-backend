package com.gmu.bookshare.beer;

import org.springframework.stereotype.Component;

@Component
public class BeerCommandLineRunner {//implements CommandLineRunner {

//    private final BeerRepository repository;
//
//    public BeerCommandLineRunner(BeerRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public void run(String... strings) throws Exception {
//        // Top beers from https://www.beeradvocate.com/lists/us, November 2018
//        Stream.of("Kentucky Brunch Brand Stout", "Marshmallow Handjee", "Barrel-Aged Abraxas",
//                "Hunahpu's Imperial Stout", "King Julius", "Heady Topper",
//                "Budweiser", "Coors Light", "PBR").forEach(name ->
//                repository.save(new Beer(name))
//        );
//        repository.findAll().forEach(System.out::println);
//    }
}