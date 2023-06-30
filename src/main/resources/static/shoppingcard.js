
// Card
let cardIcon = document.querySelector("#card-icon");
let card = document.querySelector(".card");
let closeCard = document.querySelector("#close-card");
let shopContent = document.querySelector(".shop-content");
let addIcon = document.querySelector("#add-icon");
const namePlants = []
const quantity = []
const prices = []
const images = []


// Open Card
cardIcon.onclick = () => {
    card.classList.add("active");
};

// Close Card
closeCard.onclick = () => {
    card.classList.remove("active");
};

// Card Working JS

if(document.readyState == "loading") {
    document.addEventListener("DOMContentLoaded", ready);
} else {
    ready();
}

// Making Function
function ready() {
    //Inizializzo carrello con cookie e local storage
    var namePlant = []
    var quantity = []
    var valoreCookie = leggiCookie("carrello");
    console.log( leggiCookie("carrello"));

    if (valoreCookie) {
        const oggettoJSON = JSON.parse(valoreCookie);
        namePlant = oggettoJSON.namePlant;
        quantity = oggettoJSON.quantita;
        prezzo = oggettoJSON.prezzi;
    }
    
    console.log(quantity[1]);
     for (i= 0; i < namePlant.length; i++){
        var savedImageData = localStorage.getItem(namePlant[i]+"_img");
        addProductToCard(namePlant[i], prezzo[i], savedImageData, quantity[i]);
     }
     //AGGIORNO IL TOTALE DEL CARRELLO
     updateTotal();

    // Remove Items From Card
    let removeCardButtons = document.getElementsByClassName("card-remove");
    console.log(removeCardButtons);
    setTimeout(() => {
    for (let i = 0; i < removeCardButtons.length; i++) {
        let button = removeCardButtons[i];
        button.addEventListener("click", removeCardItem);
    }
}, 10);
    // Quantity Changes
    let quantityInputs = document.getElementsByClassName("card-quantity");
    setTimeout(() => {
    for (let i = 0; i < quantityInputs.length; i++) {
        let input = quantityInputs[i];
        input.addEventListener("change", quantityChanged);
    }
}, 10);

    // Add To Card
    let addCard = document.getElementsByClassName("add-card");
    setTimeout(() => {
    for (let i = 0; i < addCard.length; i++) { 
        console.log(addCard[i])
        let button = addCard[i];
        button.addEventListener("click", addCardClicked);
    }
    }, 10);

    // Buy Button Work
    document.getElementsByClassName("btn-buy")[0].addEventListener("click", buyButtonClicked);
}

// Buy Button 
function buyButtonClicked() {
    for (let i = 0; i < namePlants.length; i++) {
    const jsonData1 = JSON.stringify({
        namePlant: namePlants[i], // ?️ add missing comma here
        quantity: quantity[i]
    });
  fetch("/user/addbooking",{
     method:"POST",
     headers:{"Content-Type":"application/json"},
     body:jsonData1
  }).then((response) => {
       response.text().then(data => {
        console.log(data);
        window.location.href ="/user/getplants"; 
       });
   }).catch(err => {
      console.log(err);
   });
    }

    alert("Il tuo ordine è stato inoltrato")
    let cardContent = document.getElementsByClassName("card-content")[0];
    while(cardContent.hasChildNodes()) {
        cardContent.removeChild(cardContent.firstChild);
    }
    updateTotal();

}

// Remove Items From Card
function removeCardItem(event) {
    let buttonClicked = event.target;
    console.log(buttonClicked.parentElement.getElementsByClassName("card-product-title")[0].innerText)
    var index = namePlants.indexOf(buttonClicked.parentElement.getElementsByClassName("card-product-title")[0].innerText);
    if (index >= 0) {
    namePlants.splice( index, 1 );
    quantity.splice(index,1)
    var valoreCookie = leggiCookie("carrello");

    if (valoreCookie) {
        const oggettoJSON = JSON.parse(valoreCookie);
        namePlant = oggettoJSON.namePlant;
        quantita = oggettoJSON.quantita;
        prezzo = oggettoJSON.prezzi;
        namePlant.splice( index, 1 );
        quantita.splice(index,1);
        prezzo.splice(index,1);
    }
    var dati = {
        namePlant: namePlant,
        quantita: quantita,
        prices: prezzo 
      };
      console.log(buttonClicked.parentElement.getElementsByClassName("card-product-title")[0].innerText)
      localStorage.removeItem(buttonClicked.parentElement.getElementsByClassName("card-product-title")[0].innerText+"_img");
      
    inviaRichiestaPOST("/user/aggiungiprodotto", dati);

    }

    buttonClicked.parentElement.remove();
    updateTotal();
}

// Quantity Changes
function quantityChanged(event) {
    let input = event.target;
    var index = namePlants.indexOf(input.parentElement.getElementsByClassName("card-product-title")[0].innerText);
    console.log(index)
    console.log(input.value)
    if (index >= 0) {
        quantity[index] = input.value
        console.log(quantity)
        const valoreCookie = leggiCookie("carrello");


        if (valoreCookie) {
            const oggettoJSON = JSON.parse(valoreCookie);
            const quantita = oggettoJSON.quantita;
            const namePlants = oggettoJSON.namePlant;
            const prezzo = oggettoJSON.prezzo;
            quantita[index]=input.value;
           
        var dati = {
            namePlant: namePlants,
            quantita: quantita,
            prices: prices 
          };
        
        inviaRichiestaPOST("/user/aggiungiprodotto", dati);
        }
    }
    if (isNaN(input.value) || input.value <= 0) {
        input.value = 1;
    }

    updateTotal();
}
function inviaRichiestaPOST(url, dati) {
    console.log("sono qui")
    fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(dati)
    })
    .then(function(response) {
        console.log(response.ok)
      if (response.ok) {
        return  "risposta ok";
      }
      throw new Error("Errore nella richiesta POST");
    })
  }

  function leggiCookie(nomeCookie) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i].trim();
      if (cookie.startsWith(nomeCookie + '=')) {
        const valoreCodificato = cookie.substring(nomeCookie.length + 1);
        const valoreDecodificato = atob(valoreCodificato);
        console.log(valoreDecodificato)
        return valoreDecodificato;
        }
    }
    
    return null; // Cookie non trovato
  }
  

// Add To Card
function addCardClicked(event) {
    console.log("print")
    let button = event.target;
    let shopProducts = button.parentElement;
    let namePlant = shopProducts.getElementsByClassName("product-title")[0].innerText;
    let prezzo = shopProducts.getElementsByClassName("price")[0].innerText;
    let image = shopProducts.getElementsByClassName("product-img")[0].src;
    addProductToCard(namePlant, prezzo, image, 1);
   
    updateTotal();
    
}

function addProductToCard(namePlant, prezzo, image, quantita) {
   
    let cardShopBox = document.createElement("div");
    cardShopBox.classList.add("card-box");
    let cardItems = document.getElementsByClassName("card-content")[0];
    let cardItemsNames = cardItems.getElementsByClassName("card-product-title");
    for (let i = 0; i < cardItemsNames.length; i++) {
        if (cardItemsNames[i].innerText == namePlant) {
            alert("Prodotto già aggiunto al carrello!");
        return;
        }
    }   

let cardBoxContent = `
                        <img src="${image}" alt="" class="card-img">
                        <div class="detail-box">
                            <div class="card-product-title">${namePlant}</div>
                            <div class="card-price">${prezzo}</div>
                            <input type="number" value=${quantita} class="card-quantity">
                        </div>  
                        <!-- Remove Card -->
                        <i class='bx bxs-trash card-remove' ></i>`;
cardShopBox.innerHTML = cardBoxContent;
cardItems.append(cardShopBox);
cardShopBox.getElementsByClassName("card-remove")[0].addEventListener("click", removeCardItem);
cardShopBox.getElementsByClassName("card-quantity")[0].addEventListener("change", quantityChanged);
namePlants.push(cardShopBox.getElementsByClassName("card-product-title")[0].innerText);
quantity.push(cardShopBox.getElementsByClassName("card-quantity")[0].value);
prices.push(cardShopBox.getElementsByClassName("card-price")[0].innerText);



localStorage.setItem(namePlant+"_img", image);

var dati = {
    namePlant: namePlants,
    quantita: quantity,
    prices: prices,
  
  };
  

  
inviaRichiestaPOST("/user/aggiungiprodotto", dati);
const valoreCookie = leggiCookie("carrello");
console.log( leggiCookie("carrello"));

if (valoreCookie) {
    const oggettoJSON = JSON.parse(valoreCookie);
    const namePlant = oggettoJSON.prezzi;
    console.log(namePlant);
  }
}



// Update Total
function updateTotal() {
    let cardContent = document.getElementsByClassName("card-content")[0];
    let cardBoxes = cardContent.getElementsByClassName("card-box");
    let total = 0;
    for (let i = 0; i < cardBoxes.length; i++) {
        let cardBox = cardBoxes[i];
        let priceElement = cardBox.getElementsByClassName("card-price")[0];
        let quantityElement = cardBox.getElementsByClassName("card-quantity")[0];
        let price = parseFloat(priceElement.innerText.replace("€", ""));
        let quantity = quantityElement.value;
        total = total + price * quantity;
    }
        // If price Contain some  Cents Value
        total = Math.round(total * 100) / 100;

        document.getElementsByClassName("total-price")[0].innerText = "€" + total;
    
}