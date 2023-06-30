package com.example.progettoSS;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // Verifica se l'utente è autenticato controllando l'attributo di sessione
        if (session == null || session.getAttribute("id") == null) {
            // Utente non autenticato, reindirizza alla pagina di login
            response.sendRedirect("/login/register");
            return false;
        }

        // Verifica il ruolo dell'utente controllando l'attributo di sessione "role"
        String role = (String) session.getAttribute("role");
        if (role == null) {
            // Ruolo non presente nell'attributo di sessione, reindirizza a una pagina di accesso negato o gestisci l'errore
            response.sendRedirect("/access-denied");
            return false;
        }

        // Esegui la divisione del ruolo
        if ("admin".equals(role)) {
            // Utente con ruolo "admin", permetti l'accesso ai percorsi dell'amministratore
            if (!request.getRequestURI().startsWith("/admin/")) {
                // Percorso non valido per l'amministratore, reindirizza a una pagina di accesso negato o gestisci l'errore
                response.sendRedirect("/access-denied");
                return false;
            }
        } else if ("user".equals(role)) {
            // Utente con ruolo "user", permetti l'accesso ai percorsi dell'utente
            if (!request.getRequestURI().startsWith("/user/")) {
                // Percorso non valido per l'utente, reindirizza a una pagina di accesso negato o gestisci l'errore
                response.sendRedirect("/access-denied");
                return false;
            }
        } else {
            // Ruolo non valido, reindirizza a una pagina di accesso negato o gestisci l'errore
            response.sendRedirect("/access-denied");
            return false;
        }

        // Utente autenticato e percorso valido per il ruolo, continua l'esecuzione del percorso richiesto
        return true;
    }


}
