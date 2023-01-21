package ru.maxima.springmvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.maxima.springmvc.dao.PeopleDAO;
import ru.maxima.springmvc.models.People;

import java.time.Period;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleDAO peopleDAO;

    public PeopleController(PeopleDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
    }

    // основная страница
    @GetMapping()
    public String index (Model model){
        //получение всех людей из нашей бд и пересылать их в отображение
        model.addAttribute("people", peopleDAO.getLstPeople());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String elem(@PathVariable("id") int id, Model model) {
        model.addAttribute("person",peopleDAO.getPeople(id));
        return "people/elem";
    }

    @GetMapping("/signin")
    public String addPerson(Model model) {
        model.addAttribute("person", new People());
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") People people) {
        peopleDAO.addPerson(people);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person",peopleDAO.getPeople(id));
        return "people/edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("person") People people) {
        peopleDAO.editPerson(people.getId(), people);
        return "redirect:/people";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        peopleDAO.deletePerson(id);
        return "redirect:/people";
    }
}
