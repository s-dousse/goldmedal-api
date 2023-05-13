package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {
    public List<GoldMedal> findByCountrySortByYearAsc(String country);
    public List<GoldMedal> findByCountrySortByYearDesc(String country);
    public List<GoldMedal> findByCountrySortBySeasonAsc(String country);
    public List<GoldMedal> findByCountrySortBySeasonDesc(String country);
    public List<GoldMedal> findByCountrySortByCityAsc(String country);
    public List<GoldMedal> findByCountrySortByCityDesc(String country);
    public List<GoldMedal> findByCountrySortByNameAsc(String country);
    public List<GoldMedal> findByCountrySortByNameDesc(String country);
    public List<GoldMedal> findByCountrySortByEventAsc(String country);
    public List<GoldMedal> findByCountrySortByEventDesc(String country);
}
