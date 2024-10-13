package com.batch.service;

import com.batch.entities.Person;
import com.batch.persistens.IPersonDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class PersonServiceImpl implements  IPersonService {

    private final IPersonDAO personDAO;

    @Override
    @Transactional
    public void saveAll(List<Person> personList) {
        personDAO.saveAll(personList);
    }
}
