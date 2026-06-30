package SistemaContador.controller;

import SistemaContador.model.User;
import SistemaContador.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {

        session.removeAttribute("error");

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
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

            return "redirect:/dashboard";
        }

        session.setAttribute("error","Credenciales incorrectas");

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

            session.setAttribute(
                    "error",
                    "Las contraseñas no coinciden"
            );

            return "redirect:/register";
        }

        if(userService.emailExists(email)){

            session.setAttribute(
                    "error",
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

        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){

        session.invalidate();

        return "redirect:/login";
    }
}
