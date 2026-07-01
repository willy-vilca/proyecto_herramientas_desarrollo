package SistemaContador.controller;

import SistemaContador.model.Client;
import SistemaContador.model.User;
import SistemaContador.service.ClientService;
import SistemaContador.util.UiFeedbackUtil;

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

        UiFeedbackUtil.moveFeedbackFromSession(
                session,
                model
        );

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
                    "clients",
                    clientService.getClientsByUser(
                            user.getUserId()
                    )
            );

            UiFeedbackUtil.addFeedback(
                    model,
                    "danger",
                    "El número de teléfono debe tener exactamente 9 dígitos"
            );

            return "clients";
        }

        // VALIDACIÓN DNI

        if("DNI".equals(client.getDocumentType()) &&
                !client.getDocumentNumber().matches("\\d{8}")) {

            model.addAttribute(
                    "clients",
                    clientService.getClientsByUser(
                            user.getUserId()
                    )
            );

            UiFeedbackUtil.addFeedback(
                    model,
                    "danger",
                    "El número del DNI debe tener exactamente 8 dígitos"
            );

            return "clients";
        }

        // VALIDACIÓN RUC

        if("RUC".equals(client.getDocumentType()) &&
                !client.getDocumentNumber().matches("\\d{11}")) {

            model.addAttribute(
                    "clients",
                    clientService.getClientsByUser(
                            user.getUserId()
                    )
            );

            UiFeedbackUtil.addFeedback(
                    model,
                    "danger",
                    "El número del RUC debe tener exactamente 11 dígitos"
            );

            return "clients";
        }

        if("update".equals(action)) {

            client.setStatus(true);

            clientService.update(client);

            UiFeedbackUtil.queueFeedback(
                    session,
                    "success",
                    "Cliente actualizado correctamente"
            );

        } else {

            client.setStatus(true);

            clientService.save(client);

            UiFeedbackUtil.queueFeedback(
                    session,
                    "success",
                    "Cliente registrado correctamente"
            );
        }

        return "redirect:/clients";
    }

    @GetMapping("/deleteClient")
    public String deleteClient(
            @RequestParam Integer id,
            HttpSession session) {

        clientService.delete(id);

        UiFeedbackUtil.queueFeedback(
                session,
                "success",
                "Cliente eliminado correctamente"
        );

        return "redirect:/clients";
    }
}