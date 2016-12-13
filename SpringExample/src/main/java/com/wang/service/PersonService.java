package com.wang.service;

import com.wang.data.Person;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by admin on 2016/10/21.
 */
public class PersonService {

    private HashMap<String, Person> persons = new HashMap<>();

    public void addPerson(Person person){
        persons.put(person.getName(), person);
    }

    public Person getPerson(String name){
        return persons.get(name);
    }

    public List<Person> getAllPerson () {
        return persons.values().stream().collect(Collectors.toList());
    }
}

