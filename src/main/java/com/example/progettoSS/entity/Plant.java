package com.example.progettoSS.entity;

import lombok.*;

import javax.persistence.*;

//SPOTBUGS: IMMAGINE PUO' DARE ERRORE, QUINDI EVITO DI USARE GET E SET E COSTRUTTORI CON LE ANNOTAZIONI E LE SCRIVO IO
// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Entity
// @Table(name = "plant")

// public class Plant{

//     @Id
//     private String id;
//     private String name;
//     private String prezzo;
//     private String altezzaPianta;
//     @Lob
// 	  @Column(length = 65555)
//     private byte[] immagine;
 
  


//     public String getId() {
//       return id;
//     }


	


// }


import java.util.Arrays;

@Entity
@Table(name = "plant")
public class Plant {

    @Id
    private String id;
    private String name;
    private String prezzo;
    private String altezzaPianta;

    @Lob
    @Column(length = 65555)
    private byte[] immagine;

    // Costruttore vuoto
    public Plant() {
    }

    // Costruttore con tutti i campi tranne l'id
    public Plant(String id, String name, String prezzo, String altezzaPianta, byte[] immagine) {
        this.id = id;
        this.name = name;
        this.prezzo = prezzo;
        this.altezzaPianta = altezzaPianta;
        this.immagine = immagine != null ? Arrays.copyOf(immagine, immagine.length) : null;
    }

    // Getter e setter per l'id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter e setter per il nome
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter e setter per il prezzo
    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    // Getter e setter per l'altezza della pianta
    public String getAltezzaPianta() {
        return altezzaPianta;
    }

    public void setAltezzaPianta(String altezzaPianta) {
        this.altezzaPianta = altezzaPianta;
    }

    // Getter e setter per l'immagine
   public byte[] getImmagine() {
    return immagine != null ? Arrays.copyOf(immagine, immagine.length) : new byte[0];
  }

       public void setImmagine(byte[] immagine) {
        this.immagine = immagine != null ? Arrays.copyOf(immagine, immagine.length) : new byte[0];
    }
}
