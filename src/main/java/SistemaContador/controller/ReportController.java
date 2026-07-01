package SistemaContador.controller;

import SistemaContador.model.User;
import SistemaContador.service.ClientService;
import SistemaContador.service.ReportService;
import SistemaContador.util.UiFeedbackUtil;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor

@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final ClientService clientService;

    @GetMapping
    public String reports(

            @RequestParam(required = false)
            Integer clientId,

            @RequestParam(required = false)
            LocalDate startDate,

            @RequestParam(required = false)
            LocalDate endDate,

            HttpSession session,
            Model model
    ) {

        User user =
                (User) session.getAttribute("user");

        if(user == null){

            return "redirect:/login";
        }

        UiFeedbackUtil.moveFeedbackFromSession(
                session,
                model
        );

        var clients =
                clientService.getClientsByUser(
                        user.getUserId()
                );

        if(clientId == null &&
                !clients.isEmpty()){

            clientId =
                    clients.get(0).getClientId();
        }

        if(startDate == null){

            startDate =
                    LocalDate.now()
                            .minusMonths(1);
        }

        if(endDate == null){

            endDate =
                    LocalDate.now();
        }

        double[] summary =
                reportService.getSummary(
                        clientId,
                        startDate,
                        endDate
                );

        model.addAttribute(
                "clients",
                clients
        );

        model.addAttribute(
                "movements",
                reportService.getTransactions(
                        clientId,
                        startDate,
                        endDate
                )
        );

        model.addAttribute(
                "categories",
                reportService.getCategorySummary(
                        clientId,
                        startDate,
                        endDate
                )
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

        model.addAttribute(
                "selectedClient",
                clientId
        );

        model.addAttribute(
                "startDate",
                startDate
        );

        model.addAttribute(
                "endDate",
                endDate
        );

        return "reports";
    }
}
