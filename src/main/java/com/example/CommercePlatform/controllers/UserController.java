package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.services.PersonService;
import com.example.CommercePlatform.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    private final PersonValidator personValidator;
    private final PersonService personService;

    @Autowired
    public UserController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    // Show
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", personService.getAll());
        return "/user/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", personService.getById(id));
        System.out.println(personService.getById(id));
        return "/user/show";
    }

    // Update
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", personService.getById(id));
//        model.addAttribute("roles", personService.getAllRoles());
        return "/user/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute("user") Person person,
                         @PathVariable("id") int id) {
        personService.changeUserRole(person, id);
        return "redirect:/user/{id}";
    }

    // Delete
    @GetMapping("/{id}/delete")
    public String deleteUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", personService.getById(id));
        return "/user/delete";
    }

    @PostMapping("/{id}/delete")
    public String delete(@ModelAttribute("user") Person person, @PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/user";
    }

}
