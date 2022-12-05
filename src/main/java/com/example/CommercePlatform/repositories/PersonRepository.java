package com.example.CommercePlatform.repositories;

import com.example.CommercePlatform.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLogin(String login);
    Optional<Person> findById(int id);

}
