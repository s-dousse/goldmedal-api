package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Column;

public interface GoldmedalRepository extends CrudRepository<GoldMedal, Integer> {

}
