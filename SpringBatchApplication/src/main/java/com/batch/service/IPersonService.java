package com.batch.service;

import com.batch.entities.Person;

import java.util.List;

public interface IPersonService {
    public void saveAll(List<Person> personList);
}
