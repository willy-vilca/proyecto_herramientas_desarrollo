package SistemaContador.controller;

import SistemaContador.dto.DashboardSummary;
import SistemaContador.model.Transaction;
import SistemaContador.model.User;
import SistemaContador.service.DashboardService;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("user");

        if(user == null){
            return "redirect:/login";
        }

        DashboardSummary summary =
                dashboardService
                        .getSummary(
                                user.getUserId()
                        );

        List<Transaction> movimientos =
                dashboardService
                        .getLastMovements(
                                user.getUserId()
                        );

        model.addAttribute(
                "ingresos",
                summary.getIngresos()
        );

        model.addAttribute(
                "gastos",
                summary.getGastos()
        );

        model.addAttribute(
                "balance",
                summary.getBalance()
        );

        model.addAttribute(
                "ultimosMovimientos",
                movimientos
        );

        String primerNombre =
                user.getFullName().split(" ")[0];

        model.addAttribute(
                "primerNombre",
                primerNombre
        );

        return "dashboard";
    }
}
