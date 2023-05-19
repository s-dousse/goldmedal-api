package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repository.CountryRepository;
import com.codecademy.goldmedal.repository.GoldMedalRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
                medalsList = ascendingOrder ? this.goldMedalRepository.getByCountryOrderByYearAsc(countryName) : this.goldMedalRepository.getByCountryOrderByYearDesc(countryName);
                break;
            case "season":
                medalsList = ascendingOrder ? this.goldMedalRepository.getByCountryOrderBySeasonAsc(countryName) : this.goldMedalRepository.getByCountryOrderBySeasonDesc(countryName);
                break;
            case "city":
                medalsList = ascendingOrder ? this.goldMedalRepository.getByCountryOrderByCityAsc(countryName) : this.goldMedalRepository.getByCountryOrderByCityDesc(countryName);
                break;
            case "name":
                medalsList = ascendingOrder ? this.goldMedalRepository.getByCountryOrderByNameAsc(countryName) : this.goldMedalRepository.getByCountryOrderByNameDesc(countryName);
                break;
            case "event":
                medalsList = ascendingOrder ? this.goldMedalRepository.getByCountryOrderByEventAsc(countryName) : this.goldMedalRepository.getByCountryOrderByEventDesc(countryName);
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        var countryOptional = countryRepository.getByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get(0);
        var goldMedalCount = this.goldMedalRepository.findByCountry(countryName).size();

        var summerWins = this.goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "summer");
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null; // TODO: find out more about null
        var totalSummerEvents = this.goldMedalRepository.findByEventAndSeason("olympics", "summer").size();
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        var winterWins = this.goldMedalRepository.findByEventAndSeason("olympics", "winter");
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        var totalWinterEvents = this.goldMedalRepository.getByEventAndSeasonOrderByYearAsc("olympics", "winter").size();
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;
        var numberEventsWonByFemaleAthletes = this.goldMedalRepository.findByGender("female").size();
        var numberEventsWonByMaleAthletes = this.goldMedalRepository.findByGender("male").size();

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
                countries = ascendingOrder ? this.countryRepository.getAllByOrderByNameAsc() : this.countryRepository.getAllByOrderByNameDesc();
                break;
            case "gdp":
                countries = ascendingOrder ? this.countryRepository.getAllByOrderByGdpAsc() : this.countryRepository.getAllByOrderByGdpDesc();
                break;
            case "population":
                countries = ascendingOrder ? this.countryRepository.getAllByOrderByPopulationAsc() : this.countryRepository.getAllByOrderByPopulationDesc();
                break;
            case "medals":
            default:
                countries = this.countryRepository.getAllByOrderByNameAsc();
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
            var goldMedalCount = this.goldMedalRepository.findByCountry(country.getName()).size();
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
