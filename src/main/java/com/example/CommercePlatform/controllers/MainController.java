package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.security.PersonDetails;
import com.example.CommercePlatform.services.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/personal";
    }

    @GetMapping("/personal")
    public String personal(Model model) {
        // Получаем объект аутентификации: c помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации.
        // По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутентификации пользователя.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Преобразовываем объект аутентификации в специальный объект класса по работе с пользователями
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//        String role = personDetails.getPerson().getRole();
//        model.addAttribute("user", personService.getById(personDetails.getPerson().getId()));
        model.addAttribute("user", personDetails.getPerson());
//        model.addAttribute("user", personDetails.getPerson());
        return "personal";
    }

}
