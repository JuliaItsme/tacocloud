package ru.goryacheva.tacocloud.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.goryacheva.tacocloud.service.OrderAdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final OrderAdminService orderAdminService;

    @Autowired
    public AdminController(OrderAdminService orderAdminService) {
        this.orderAdminService = orderAdminService;
    }

    @GetMapping
    public String showAminPage(){
        return "admin";
    }

    @PostMapping("/deleteOrders")
    public String deleteAllOrders(){
        orderAdminService.deleteAllOrders();
        return "redirect:/admin";
    }
}
