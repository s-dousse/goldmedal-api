package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {
    public List<GoldMedal> getByCountryOrderByYearAsc(String country);
    public List<GoldMedal> getByCountryOrderByYearDesc(String country);
    public List<GoldMedal> getByCountryOrderBySeasonAsc(String country);
    public List<GoldMedal> getByCountryOrderBySeasonDesc(String country);
    public List<GoldMedal> getByCountryOrderByCityAsc(String country);
    public List<GoldMedal> getByCountryOrderByCityDesc(String country);
    public List<GoldMedal> getByCountryOrderByNameAsc(String country);
    public List<GoldMedal> getByCountryOrderByNameDesc(String country);
    public List<GoldMedal> getByCountryOrderByEventAsc(String country);
    public List<GoldMedal> getByCountryOrderByEventDesc(String country);
    public List<GoldMedal> findByCountry(String country);
    public List<GoldMedal> getByCountryAndSeasonOrderByYearAsc(String country, String season);
    public List<GoldMedal> findByEventAndSeason(String event, String season);
    public List<GoldMedal> getByEventAndSeasonOrderByYearAsc(String event, String season);
    public List<GoldMedal> findByGender(String gender);
}
