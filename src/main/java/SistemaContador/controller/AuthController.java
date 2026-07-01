package SistemaContador.controller;

import SistemaContador.model.User;
import SistemaContador.service.UserService;
import SistemaContador.util.UiFeedbackUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(
            HttpSession session,
            Model model
    ) {

        UiFeedbackUtil.moveFeedbackFromSession(
                session,
                model
        );

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(
            HttpSession session,
            Model model
    ) {
        UiFeedbackUtil.moveFeedbackFromSession(
                session,
                model
        );

        return "auth/register";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        User user = userService.login(email,password);

        if(user != null){

            session.setAttribute("user",user);

            UiFeedbackUtil.queueFeedback(
                    session,
                    "success",
                    "Bienvenido nuevamente al sistema contable"
            );

            return "redirect:/dashboard";
        }

        UiFeedbackUtil.queueFeedback(
                session,
                "danger",
                "Credenciales incorrectas"
        );

        return "redirect:/login";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpSession session){

        if(!password.equals(confirmPassword)){

            UiFeedbackUtil.queueFeedback(
                    session,
                    "danger",
                    "Las contraseñas no coinciden"
            );

            return "redirect:/register";
        }

        if(userService.emailExists(email)){

            UiFeedbackUtil.queueFeedback(
                    session,
                    "danger",
                    "El correo ya está en uso"
            );

            return "redirect:/register";
        }

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);

        user = userService.register(user);

        session.setAttribute("user",user);

        UiFeedbackUtil.queueFeedback(
                session,
                "success",
                "Cuenta creada correctamente"
        );

        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){

        session.invalidate();

        return "redirect:/login";
    }
}
