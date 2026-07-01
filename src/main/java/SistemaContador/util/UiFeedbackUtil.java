package SistemaContador.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public final class UiFeedbackUtil {

    private static final String FEEDBACK_TYPE = "feedbackType";
    private static final String FEEDBACK_MESSAGE = "feedbackMessage";

    private UiFeedbackUtil() {
    }

    public static void moveFeedbackFromSession(
            HttpSession session,
            Model model
    ) {
        Object feedbackType =
                session.getAttribute(FEEDBACK_TYPE);
        Object feedbackMessage =
                session.getAttribute(FEEDBACK_MESSAGE);

        if (feedbackType != null &&
                feedbackMessage != null) {

            model.addAttribute(
                    FEEDBACK_TYPE,
                    feedbackType.toString()
            );

            model.addAttribute(
                    FEEDBACK_MESSAGE,
                    feedbackMessage.toString()
            );

            session.removeAttribute(FEEDBACK_TYPE);
            session.removeAttribute(FEEDBACK_MESSAGE);
        }

        Object legacyError =
                session.getAttribute("error");

        if (legacyError != null &&
                !model.containsAttribute(FEEDBACK_MESSAGE)) {

            addFeedback(
                    model,
                    "danger",
                    legacyError.toString()
            );

            session.removeAttribute("error");
        }
    }

    public static void queueFeedback(
            HttpSession session,
            String type,
            String message
    ) {
        session.setAttribute(
                FEEDBACK_TYPE,
                type
        );

        session.setAttribute(
                FEEDBACK_MESSAGE,
                message
        );
    }

    public static void addFeedback(
            Model model,
            String type,
            String message
    ) {
        model.addAttribute(
                FEEDBACK_TYPE,
                type
        );

        model.addAttribute(
                FEEDBACK_MESSAGE,
                message
        );
    }
}
