package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    List<Country> getByName(String name);
    List<Country> findAllOrderByNameDesc();
    List<Country> findAllOrderByGdpAsc();
    List<Country> findAllOrderByGdpDesc();
    List<Country> findAllOrderByPopulationAsc();
    List<Country> findAllOrderByPopulationDesc();
    List<Country> findAllOrderByNameAsc();
}
