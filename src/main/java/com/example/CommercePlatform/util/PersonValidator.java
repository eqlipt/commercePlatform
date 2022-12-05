package com.example.CommercePlatform.util;

import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.services.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    // В данно методе указываем для какой модели предназначен данный валидатор
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("Validator working");
        Person person = (Person) target;
        // Если метод по поиску пользователя по логину не равен 0 тогда такой логин уже занят
        if(personService.getByLogin(person) != null){
            System.out.println("inside personService.getByLogin(person)");
            errors.rejectValue("login", "", "Логин занят");
        }
    }
}
