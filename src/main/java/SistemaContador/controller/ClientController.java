package SistemaContador.controller;

import SistemaContador.model.Client;
import SistemaContador.model.User;
import SistemaContador.service.ClientService;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/clients")
    public String clients(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("user");

        if(user == null){
            return "redirect:/login";
        }

        model.addAttribute(
                "clients",
                clientService.getClientsByUser(
                        user.getUserId()
                )
        );

        return "clients";
    }

    @PostMapping("/clients")
    public String saveClient(
            Client client,
            @RequestParam(required = false)
            String action,
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("user");

        if(user == null){
            return "redirect:/login";
        }

        client.setUserId(user.getUserId());

        // VALIDACIÓN TELÉFONO

        if(client.getPhone() != null &&
                !client.getPhone().matches("\\d{9}")) {

            model.addAttribute(
                    "error",
                    "El número de teléfono debe tener exactamente 9 dígitos"
            );

            model.addAttribute(
                    "clients",
                    clientService.getClientsByUser(
                            user.getUserId()
                    )
            );

            return "clients";
        }

        // VALIDACIÓN DNI

        if("DNI".equals(client.getDocumentType()) &&
                !client.getDocumentNumber().matches("\\d{8}")) {

            model.addAttribute(
                    "error",
                    "El número del DNI debe tener exactamente 8 dígitos"
            );

            model.addAttribute(
                    "clients",
                    clientService.getClientsByUser(
                            user.getUserId()
                    )
            );

            return "clients";
        }

        // VALIDACIÓN RUC

        if("RUC".equals(client.getDocumentType()) &&
                !client.getDocumentNumber().matches("\\d{11}")) {

            model.addAttribute(
                    "error",
                    "El número del RUC debe tener exactamente 11 dígitos"
            );

            model.addAttribute(
                    "clients",
                    clientService.getClientsByUser(
                            user.getUserId()
                    )
            );

            return "clients";
        }

        if("update".equals(action)) {

            client.setStatus(true);

            clientService.update(client);

        } else {

            client.setStatus(true);

            clientService.save(client);
        }

        return "redirect:/clients";
    }

    @GetMapping("/deleteClient")
    public String deleteClient(
            @RequestParam Integer id) {

        clientService.delete(id);

        return "redirect:/clients";
    }
}