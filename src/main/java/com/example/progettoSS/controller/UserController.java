package com.example.progettoSS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.example.progettoSS.repository.UserR;
import com.example.progettoSS.repository.SessionR;
import com.example.progettoSS.entity.User;

import jakarta.servlet.http.HttpServletRequest; //Nb qualsiasi cosa javax.servelet usa jakarta
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.apache.commons.text.StringEscapeUtils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Optional;

@Controller
public class UserController{
   
    @Autowired
    private UserR userr;

    @Autowired
    private SessionR sessionR;

    /**************************************LOGIN-REGISTER**************************************/
    //Get per la home di login-registrazione
    @GetMapping("/login/register")
    public String loginregister(Model model, HttpSession session){
        User user = new User();
        System.out.println(session.getAttribute("id"));
        if(session.getAttribute("id")==null){
            System.out.println("non loggato");
        }
        model.addAttribute("user", user);
        return "login"; 
        //return "loginmodify"; //input validation
    }

    /**************************************REGISTER**************************************/
    //Registrazione Get
    @GetMapping("/userRegister")
        public String register(@RequestParam("name") String name, @RequestParam("surname") String surname, @RequestParam("email") String email,
                            @RequestParam("phone") String phone,  @RequestParam("password") String password, @RequestParam("cpassword") String cpassword) {

        // Crea un nuovo oggetto User con i dati forniti
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setCpassword(cpassword);
        user.setRole("user");
        
        // Esegui l'inserimento nel database
        userr.registerUser(user);

        return "redirect:/user/getplants";
    }

  /*@PostMapping("/user/registerUser")
public String registerUser(@ModelAttribute("user") User user, Model model) {
    System.out.println(user);
    
    // Esegui la validazione dei campi del modulo
    if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getSurname()) || StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(user.getCpassword())) {
        model.addAttribute("error", "Compila tutti i campi");
        return "loginmodify"; // Ritorna la vista di registrazione con un messaggio di errore
    }

    // Verifica se le password coincidono
    if (!user.getPassword().equals(user.getCpassword())) {
        model.addAttribute("error", "Le password non coincidono");
        return "loginmodify"; // Ritorna la vista di registrazione con un messaggio di errore
    }

    // Controlla il formato dell'email
    if (!isValidEmail(user.getEmail())) {
        model.addAttribute("error", "Email non valida");
        return "loginmodify"; // Ritorna la vista di registrazione con un messaggio di errore
    }

    // Controlla il formato del numero di telefono
    if (!isValidPhoneNumber(user.getPhone())) {
        model.addAttribute("error", "Numero di telefono non valido");
        return "loginmodify"; // Ritorna la vista di registrazione con un messaggio di errore
    }

    // Controlla se il nome e il cognome contengono solo caratteri alfabetici
    if (!isAlpha(user.getName()) || !isAlpha(user.getSurname())) {
        model.addAttribute("error", "Il nome e il cognome devono contenere solo caratteri alfabetici");
        return "loginmodify"; // Ritorna la vista di registrazione con un messaggio di errore
    }

      // Controlla la lunghezza della password
    if (user.getPassword().length() < 6 || user.getPassword().length() > 24) {
        model.addAttribute("error", "La password deve essere compresa tra 6 e 24 caratteri");
        return "loginmodify";
    }

    // Sanitizza i dati prima di utilizzarli
    user.setName(sanitize(user.getName()));
    user.setSurname(sanitize(user.getSurname()));
    user.setEmail(sanitizeEmail(user.getEmail()));
    user.setPhone(sanitizePhoneNumber(user.getPhone()));
    // Non sanatizzo la password perché è buona norma non toccarla
    user.setPassword(user.getPassword());
    user.setCpassword(user.getCpassword());

    // Esegui la registrazione dell'utente nel tuo sistema
    user.setRole("user");
    userr.registerUser(user);

    return "redirect:/user/getplants";
}

*/



    /**************************************LOGIN**************************************/
    
    //LOGIN CON GET
    @GetMapping("/userLogin")
    public String login(HttpSession session, Model model, @RequestParam("email") String email, @RequestParam("password") String password) {
    String ritorno =  null;
    User userresponse = userr.findUser(email, password);
        if (userresponse != null ){
        session.setAttribute("id", userresponse.getId());
        session.setAttribute("role", userresponse.getRole());
        // Leggi un valore dalla sessione
        String value = (String) session.getAttribute("id");
        System.out.println(value);
        System.out.println("UTENTE TROVATO");
            if ("user".equals(userresponse.getRole())){
                    // Reindirizza alla pagina "/user/getplants"
                ritorno =  "redirect:/user/getplants";
            }else if ("admin".equals(userresponse.getRole())){
                ritorno = "redirect:/admin/getplants";
            } else ritorno = "login";

        } else {
             // Aggiungi un attributo al modello per indicare l'errore
             model.addAttribute("error", "Credenziali non valide");
            // Ritorna la vista "login"
             ritorno = "login";

        }
        return ritorno;
        
    }




/*LOGIN CON POST*/
/*
@PostMapping("/userLogin")
public String loginUser(Model model, @ModelAttribute("user") User user, HttpSession session) {
    System.out.println(user);
    String email = sanitizeEmail(user.getEmail());
    String password = user.getPassword();

    System.out.println(email);
    System.out.println(password);

    // Effettua la validazione lato server
    if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
        model.addAttribute("error", "Compila tutti i campi");
        return "loginmodify";
    }

    // Verifica la validità dell'email
    if (!isValidEmail(email)) {
        model.addAttribute("error", "Email non valida");
        return "loginmodify";
    }

    // Controlla la lunghezza della password
    if (password.length() < 6 || password.length() > 24) {
        model.addAttribute("error", "La password deve essere compresa tra 6 e 24 caratteri");
        return "loginmodify";
    }


    User userResponse = userr.findUser(email, password);
    System.out.println(userResponse);
    if (userResponse != null) {
        session.setAttribute("id", userResponse.getId());
        session.setAttribute("role", userResponse.getRole());
        System.out.println(session.getAttribute("id"));

        if ("user".equals(userResponse.getRole())) {
            return "redirect:/user/getplants";
        } else if ("admin".equals(userResponse.getRole())) {
            return "redirect:/admin/getplants";
        } else {
            model.addAttribute("error", "Ruolo non valido");
            return "loginmodify";
        }
    } else {
        model.addAttribute("error", "Credenziali non valide");
        return "loginmodify";
    }
}

*/



    /**************************************GET PROFILE**************************************/

    @GetMapping("/admin/getprofile")
    public String adminprofile(Model model, HttpSession session){
      
        String value = (String) session.getAttribute("id");
        System.out.println(value);
        //SPOTBUGS: Eliminazione variabile user in quanto non sicura
       // User user = new User();
      //  user = userr.findUserById(value);
      //  model.addAttribute("user", user);

        //Versione corretta:
       model.addAttribute("user", userr.findUserById(value));
        return "profileadmin";
    }

    @GetMapping("/user/getprofile")
    public String userrprofile(Model model, HttpSession session){
        
        String value = (String) session.getAttribute("id");
        System.out.println(value);
         //SPOTBUGS: Eliminazione variabile user in quanto non sicura
        //User user = new User();
        //user = userr.findUserById(value);
    
        model.addAttribute("user", userr.findUserById(value));
        return "profile";
    }

    /**************************************LOGOUT**************************************/
    //Logout admin
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
           // Recupera l'ID della sessione
        String sessionId = session.getId();
        System.out.println("sessionid"+sessionId);
    
         // Esegui la logica per rimuovere i dati della sessione dal database
        sessionR.deleteSessionDataFromDatabase(sessionId);

        // Rimuovi l'attributo di sessione
        session.removeAttribute("id");
        session.removeAttribute("role");

        // Invalida la sessione
        session.invalidate();

        // Reindirizza l'utente alla pagina di login o a una pagina di conferma del logout
        return "redirect:/";
    }

    //Logout user
    @GetMapping("/user/logout")
    public String logoutuser(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
        /*Elimino cookie carrello*/
        // Ottieni l'array dei cookie dalla richiesta
    
        
    Cookie carrelloCookie = new Cookie("carrello", null);
        carrelloCookie.setPath("/");
        carrelloCookie.setMaxAge(0);
        response.addCookie(carrelloCookie);

        // Recupera l'ID della sessione
        String sessionId = session.getId();
        System.out.println("sessionid"+sessionId);
    
        // Esegui la logica per rimuovere i dati della sessione dal database
        sessionR.deleteSessionDataFromDatabase(sessionId);

        // Rimuovi l'attributo di sessione
        session.removeAttribute("id");
        session.removeAttribute("role");

        // Invalida la sessione
        session.invalidate();

        // Reindirizza l'utente alla pagina di login o a una pagina di conferma del logout
        return "redirect:/";
    }

    /**************************************ACCESSO NEGATO**************************************/
    //Accesso negato
    @GetMapping("/access-denied")
    public String accessdenied() {
            return "accessdenied";
    }


//     /**************************************SANATIZZAZIONE**************************************/
//     //Sanatizzazione name e surname
//     public static String sanitize(String input) {
//     // Rimozione degli spazi vuoti all'inizio e alla fine del testo
//     String sanitizedInput = input.trim();

//     // Rimozione dei caratteri non validi
//     sanitizedInput = StringEscapeUtils.escapeHtml4(sanitizedInput);

//     // Rimuovi eventuali caratteri non validi per il nome
//    sanitizedInput = sanitizedInput.replaceAll("[^A-Za-z0-9 ]", "");

//     return sanitizedInput;
// }
// public String sanitizeEmail(String email) {
//     // Rimuovi gli spazi vuoti all'inizio e alla fine dell'indirizzo email
//     String sanitizedEmail = email.trim();

//     // Sanitizza l'indirizzo email
//     sanitizedEmail = StringEscapeUtils.escapeHtml4(sanitizedEmail);

//     // Rimuovi eventuali caratteri non validi per un indirizzo email
//     sanitizedEmail = sanitizedEmail.replaceAll("[^A-Za-z0-9@._-]", "");

//     return sanitizedEmail;
// }

// public String sanitizePhoneNumber(String phoneNumber) {
//     // Rimuovi gli spazi vuoti all'inizio e alla fine del numero di telefono
//     String sanitizedPhoneNumber = phoneNumber.trim();

//     // Rimuovi tutti i caratteri non numerici
//     sanitizedPhoneNumber = sanitizedPhoneNumber.replaceAll("[^0-9]", "");

//     return sanitizedPhoneNumber;
// }


//     /**************************************VALIDAZIONE**************************************/
// // Funzione per verificare il formato corretto dell'email
// private boolean isValidEmail(String email) {
//     String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
//     return email.matches(emailRegex);
// }

// // Funzione per verificare il formato corretto del numero di telefono
// private boolean isValidPhoneNumber(String phoneNumber) {
//     String phoneRegex = "^[0-9]{10}$";
//     return phoneNumber.matches(phoneRegex);
// }

// // Funzione per verificare se una stringa contiene solo caratteri alfabetici
// private boolean isAlpha(String str) {
//     return str.matches("[a-zA-Z]+");
// }


}