package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final PersonService personService;

    @Autowired
    public ProfileController(PersonService personService) {
        this.personService = personService;
    }

    // Show
    @GetMapping()
    public String profile(Model model) {
        model.addAttribute("user", personService.getPersonFromAuthentication());
        return "profile/index";
    }

    // Update
    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("user", personService.getPersonFromAuthentication());
        return "/profile/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("user") @Valid Person person,
                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/profile/edit";
        }
        personService.changeUserPassword(person);
        return "redirect:/profile";
    }

    // Delete
    @GetMapping("/delete")
    public String delete(Model model) {
        model.addAttribute("user", personService.getPersonFromAuthentication());
        return "/profile/delete";
    }

    @PostMapping("/delete")
    public String delete() {
        System.out.println("post request to delete controller");
        personService.delete(personService.getPersonFromAuthentication().getId());
        SecurityContextHolder.clearContext();
        return "redirect:/auth/registration";
    }

}
