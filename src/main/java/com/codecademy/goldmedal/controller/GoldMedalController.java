package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;
import com.codecademy.goldmedal.repositories.GoldMedalRepository;
import com.codecademy.goldmedal.repositories.CountryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    private GoldMedalRepository goldMedalRepository;
    private CountryRepository countryRepository;

    public GoldMedalController(GoldMedalRepository goldMedalRepository, CountryRepository countryRepository) {
        this.goldMedalRepository = goldMedalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountrySortByYearAsc(countryName) : this.goldMedalRepository.findByCountrySortByYearDesc(countryName);
                break;
            case "season":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountrySortBySeasonAsc(countryName) : this.goldMedalRepository.findByCountrySortBySeasonDesc(countryName);
                break;
            case "city":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountrySortByCityAsc(countryName) : this.goldMedalRepository.findByCountrySortByCityDesc(countryName);
                break;
            case "name":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountrySortByNameAsc(countryName) : this.goldMedalRepository.findByCountrySortByNameDesc(countryName);
                break;
            case "event":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountrySortByEventAsc(countryName) : this.goldMedalRepository.findByCountrySortByEventDesc(countryName);
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        Optional<Country> countryOptional = this.countryRepository.findByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        Country country = countryOptional.get();
        Integer goldMedalCount = this.goldMedalRepository.findByCountry(countryName).size();

        List<GoldMedal> summerWins = this.goldMedalRepository.findByCountryAndSeasonSortByYearAsc(countryName, "summer");
        Integer numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null; // TODO: find out more about null
        Integer totalSummerEvents = this.goldMedalRepository.findByEventsAndSeason("olympics", "summer").size();
        Float percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        Integer yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        List<GoldMedal> winterWins = this.goldMedalRepository.findByEventsAndSeason("olympics", "winter");
        Integer numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        Integer totalWinterEvents = this.goldMedalRepository.findByEventsAndSeasonSortByYearAsc("olympics", "winter").size();
        Float percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        Integer yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;
        Integer numberEventsWonByFemaleAthletes = this.goldMedalRepository.findByGender("female").size();
        Integer numberEventsWonByMaleAthletes = this.goldMedalRepository.findByGender("male").size();

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                countries = // TODO: list of countries sorted by name in the given order
                break;
            case "gdp":
                countries = // TODO: list of countries sorted by gdp in the given order
                break;
            case "population":
                countries = // TODO: list of countries sorted by population in the given order
                break;
            case "medals":
            default:
                countries = // TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = // TODO: get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
