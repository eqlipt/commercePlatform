package com.example.CommercePlatform.services;

import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.models.UserRole;
import com.example.CommercePlatform.repositories.PersonRepository;
import com.example.CommercePlatform.repositories.UserRoleRepository;
import com.example.CommercePlatform.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }
    public List<UserRole> getAllRoles() {
        return  userRoleRepository.findAll();
    }

    public Object getById(int id) {
        Optional<Person> person_db = personRepository.findById(id);
        return person_db.orElse(null);
    }

    public Person getPersonFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = (Person) this.getById(personDetails.getPerson().getId());
        return person;
    }
    public Person getByLogin(Person person){
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }

    @Transactional
    public void changeUserRole(Person person, int id) {
        Person person_db = (Person) this.getById(id);
        person_db.setRole(person.getRole());
        personRepository.save(person_db);
    }

    @Transactional
    public void changeUserPassword(Person person) {
        Person person_db = (Person) this.getById(person.getId());
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person_db.setPassword(encodedPassword);
        personRepository.save(person_db);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public void register(Person person){
        System.out.println("Register service working");
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        person.setRole("ROLE_CUSTOMER");
        personRepository.save(person);
    }
}
