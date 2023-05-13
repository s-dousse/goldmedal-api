package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    List<Country> findByName(String name);
}