package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

//    @GetMapping()
//    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("admin", userService.getUserByName(userDetails.getUsername()));
//        model.addAttribute("newUser", new User());
//        model.addAttribute("rolesAdd", roleService.getAllRoles());
//        return "admin";
//    }
//
//    @PostMapping("/admin")
//    public String createUser(@ModelAttribute("user") User user) {
//        userService.add(user);
//        return "redirect:/admin";
//    }
//
//    @PostMapping(value = "/admin/{id}")
//    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {
//        userService.update(user, (long) id);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/admin/{id}/delete")
//    public String deleteUser(@PathVariable("id") int id) {
//        userService.delete((long) id);
//        return "redirect:/admin";
//    }

    @GetMapping()
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("user", userService.getUserByName(userDetails.getUsername()));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("listRoles", roleService.getAllRoles());
        return "admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable(value = "id", required = false) Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@PathVariable(value = "id", required = false) Long id, @ModelAttribute("user") User user,
                             @RequestParam(value = "nameRole", required = false) String nameRole) {
        if (nameRole != null) {
            user.setRoles(roleService.getByName(nameRole));
        }
        userService.update(user, id);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable(value = "id", required = false) Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("roleName", roleService.getAllRoles());
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "nameRole", required = false) String nameRole,
                             @RequestParam(value = "username", required = false) String username) {
        user.setUsername(username);
        user.setRoles(roleService.getByName(nameRole));
        userService.add(user);
        return "redirect:/admin";
    }
}