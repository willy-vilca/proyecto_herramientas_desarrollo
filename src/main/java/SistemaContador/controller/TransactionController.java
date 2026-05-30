package SistemaContador.controller;

import SistemaContador.model.*;
import SistemaContador.service.*;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor

@RequestMapping("/movements")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final ClientService clientService;

    @GetMapping
    public String movements(
            @RequestParam(required = false)
            Integer clientId,
            HttpSession session,
            Model model
    ) {

        User user =
                (User) session.getAttribute("user");

        if (user == null) {

            return "redirect:/login";
        }

        var clients =
                clientService.getClientsByUser(
                        user.getUserId()
                );

        if (clientId == null &&
                !clients.isEmpty()) {

            clientId =
                    clients.get(0).getClientId();
        }

        double[] summary =
                transactionService
                        .getSummaryByClient(
                                clientId
                        );

        model.addAttribute(
                "clients",
                clients
        );

        model.addAttribute(
                "movements",
                transactionService.getByClient(clientId)
        );

        model.addAttribute(
                "categories",
                categoryService.getCategoriesByUser(
                        user.getUserId()
                )
        );

        model.addAttribute(
                "selectedClient",
                clientId
        );

        model.addAttribute(
                "ingresos",
                summary[0]
        );

        model.addAttribute(
                "gastos",
                summary[1]
        );

        model.addAttribute(
                "balance",
                summary[2]
        );

        return "movements";
    }

    @PostMapping
    public String saveMovement(
            @RequestParam(required = false)
            String action,
            Transaction transaction
    ) {

        if ("update".equals(action)) {

            transactionService.update(transaction);

        } else {

            transactionService.save(transaction);
        }

        return "redirect:/movements?clientId="
                + transaction.getClientId();
    }

    @GetMapping("/delete")
    public String deleteMovement(
            @RequestParam Integer id,
            @RequestParam Integer clientId
    ) {

        transactionService.delete(id);

        return "redirect:/movements?clientId="
                + clientId;
    }
}