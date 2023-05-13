package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Column;

public interface GoldmedalRepository extends CrudRepository<GoldMedal, Integer> {
    public List<GoldMedal> findByCountrySortByYearAsc(String country);
    public List<GoldMedal> findByCountrySortBySeasonAsc(String country);
    public List<GoldMedal> findByCountrySortByCityAsc(String country);
    public List<GoldMedal> findByCountrySortByNameAsc(String country);
    public List<GoldMedal> findByCountrySortByEventAsc(String country);
}
