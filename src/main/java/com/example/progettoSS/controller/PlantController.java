package com.example.progettoSS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.progettoSS.repository.PlantR;
import com.example.progettoSS.entity.Plant;
import java.io.IOException;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Base64;
import org.json.JSONObject;
import org.apache.commons.text.StringEscapeUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import jakarta.servlet.http.HttpServletRequest; //Nb qualsiasi cosa javax.servelet usa jakarta
import jakarta.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import java.util.regex.Pattern;

@Controller
public class PlantController {

    @Autowired
    private PlantR plantr;


     @GetMapping("/admin/addplant")
    public String addPlant(Model model)  {  
         Plant plant = new Plant();
        model.addAttribute("plant", plant);
       return "addplant";
       //return "addplantmodify"; //Con input validation
    }
/*************UPLOAD PLANT SENZA VALIDAZIONE E SANITIFICAZIONE***************************/
     @PostMapping("/admin/uploadplant")
    public String uploadPlant(@RequestParam("name") String name,@RequestParam("prezzo") String prezzo, @RequestParam("altezza") String altezzaPianta, @RequestParam("immagine") MultipartFile immagine, Model model) throws IOException {
     //  Files files = fileStorageService.storeFile(file);
        System.out.println("sono in upload plant");
        plantr.addPlant(name, prezzo, altezzaPianta,immagine);
        return "redirect:/admin/getplants";
    }

/*************UPLOAD PLANT SENZA VALIDAZIONE E SANITIFICAZIONE***************************/
/*
@PostMapping("/admin/uploadplant")
public String uploadPlant(@RequestParam("name") String name, @RequestParam("prezzo") String prezzo, @RequestParam("altezza") String altezzaPianta, @RequestParam("immagine") MultipartFile immagine, Model model) throws IOException {
  
    // Esegui la validazione dei parametri
    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(prezzo) || StringUtils.isEmpty(altezzaPianta)) {
        model.addAttribute("error", "Compila tutti i campi");
        return "addplantmodify"; // Ritorna la vista di caricamento della pianta con un messaggio di errore
    }

    // Verifica il formato del nome (solo lettere)
    if (!isAlpha(name)) {
        model.addAttribute("error", "Inserisci un nome valido (solo lettere)");
        return "addplantmodify"; // Ritorna la vista di caricamento della pianta con un messaggio di errore
    }

    // Verifica il formato dell'altezza (valore numerico con punto come separatore decimale)
    if (!isCorrectNumber(altezzaPianta)) {
        model.addAttribute("error", "Inserisci una altezza valida (valore numerico con punto come separatore decimale)");
        return "addplantmodify"; // Ritorna la vista di caricamento della pianta con un messaggio di errore
    }

    // Verifica il formato del prezzo (valore numerico con punto come separatore decimale)
    if (!isCorrectNumber(prezzo)) {
        model.addAttribute("error", "Inserisci un prezzo valido (valore numerico con punto come separatore decimale)");
        return "addplantmodify"; // Ritorna la vista di caricamento della pianta con un messaggio di errore
    }

   String nameSanitized = sanitize(name);
   String altezzaSanitized = sanitizeNumber(altezzaPianta);
   String prezzoSanitized = sanitizeNumber(prezzo);
   
    // Esegui il caricamento della pianta nel tuo sistema
    plantr.addPlant(nameSanitized, altezzaSanitized, prezzoSanitized, immagine);

    return "redirect:/admin/getplants";
}
*/




    @GetMapping("/user/getplants")
    public String getPlants(Model model,HttpSession session)  {  
 

       List<Plant> plants = plantr.getAllPlants();
       List <String> immagini = new ArrayList();
   

       model.addAttribute("plants", plants);

       for (int i = 0; i < plants.size(); i++){
            String s = Base64.getEncoder().encodeToString(plants.get(i).getImmagine());
            immagini.add(s);
       }
       model.addAttribute("immagini", immagini);


        return "plantuser";
    }


    @GetMapping("/admin/getplants")
    public String getPlantsAdmin(Model model)  {  

       List<Plant> plants = plantr.getAllPlants();
       List <String> immagini = new ArrayList();
   

       model.addAttribute("plants", plants);

       for (int i = 0; i < plants.size(); i++){
            String s = Base64.getEncoder().encodeToString(plants.get(i).getImmagine());
            immagini.add(s);
       }
       model.addAttribute("immagini", immagini);


        return "plantadmin";
    }

  @DeleteMapping("/admin/deleteplant/{id}")
    public ResponseEntity<String> deletePlant(@PathVariable("id") String id) {
        // Logica per eliminare la pianta
        
        int rowsAffected = plantr.deletePlant(id);
        boolean success = (rowsAffected > 0);
        String message = success ? "Eliminazione riuscita. Numero di righe eliminate: " + rowsAffected : "Nessuna riga eliminata.";

        // Creazione dell'oggetto JSON per la risposta
        JSONObject response = new JSONObject();
        response.put("success", success);
        response.put("message", message);

        // Restituisci la risposta come JSON con lo status appropriato
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }


//     // Verifica se una stringa contiene solo caratteri alfabetici
//     public static boolean isAlpha(String str) {
//         return str.matches("[A-Za-z]+");
//     }
    
//     // Verifica se una stringa rappresenta un valore di altezza valido (valore numerico con punto come separatore decimale)
//     public static boolean isCorrectNumber(String str) {
//         return str.matches("^\\d+(\\.\\d+)?$");
// //     }


//    //Sanatizzazione name 
//     public static String sanitize(String input) {
//     // Rimozione degli spazi vuoti all'inizio e alla fine del testo
//     String sanitizedInput = input.trim();

//     // Rimozione dei caratteri non validi
//     sanitizedInput = StringEscapeUtils.escapeHtml4(sanitizedInput);

//     // Rimuovi eventuali caratteri non validi per il nome
//    sanitizedInput = sanitizedInput.replaceAll("[^A-Za-z0-9 ]", "");

//     return sanitizedInput;
// }
// public String sanitizeNumber(String input) {
//     // Rimuovi gli spazi vuoti all'inizio e alla fine
//     String sanitizedNumber = input.trim();

//     // Sanitizza 
//     sanitizedNumber = StringEscapeUtils.escapeHtml4(sanitizedNumber);

//     // Rimuovi eventuali caratteri non validi (es. lettere, caratteri speciali ecc.)
//     sanitizedNumber = sanitizedNumber.replaceAll("[^0-9.]", "");

//     return sanitizedNumber;
// }

}