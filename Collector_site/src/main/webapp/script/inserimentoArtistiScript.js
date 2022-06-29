/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function singoloVisible(){
     const inserisciAristaDivSingolo = document.getElementById("inserisciAristaDivSingolo");
     const inserisciArtistaDivB = document.getElementById("inserisciArtistaDivB");
     inserisciArtistaDivB.style.display="none";
     inserisciAristaDivSingolo.style.display="block";
}
function gruppoVisible(){
    const inserisciAristaDivGruppo = document.getElementById("inserisciAristaDivGruppo");
    const inserisciArtistaDivB = document.getElementById("inserisciArtistaDivB");
    inserisciArtistaDivB.style.display="none";
    inserisciAristaDivGruppo.style.display="block";
}