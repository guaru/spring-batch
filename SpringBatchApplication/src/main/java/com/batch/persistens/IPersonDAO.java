package com.batch.persistens;

import com.batch.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface IPersonDAO extends CrudRepository<Person, Long> {

}
