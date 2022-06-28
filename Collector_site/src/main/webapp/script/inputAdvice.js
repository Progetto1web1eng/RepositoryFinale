/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function adviceCercaIMieiDischiVisible(){
    //suggerimenti visibile quando cominciamo a scrivere qualcosa
        document.getElementById("cercaIMieiDischiSuggerimenti").style.visibility = "visible";
    
}

function adviceCercaIMieiDischi(){
    //suggerimenti nascosti se l'input torna vuoto
    if(document.getElementById("cercaIMieiDischiInput").value===""){
        document.getElementById("cercaIMieiDischiSuggerimenti").style.visibility = "hidden";
    }
    //autocompletamento suggerimenti
    const inputVal = document.getElementById("cercaIMieiDischiInput").value;
    document.getElementById("cIMDSS1").innerHTML=inputVal;
    document.getElementById("cIMDSS2").innerHTML=inputVal;
    document.getElementById("cIMDSS3").innerHTML=inputVal;
}

function adviceCercaIMieiDischiTitolo(){
    //aggiunta caratteri speciali per ottimizzare la ricerca
    const nuovoVal = document.getElementById("cercaIMieiDischiInput").value +":T";
    document.getElementById("cercaIMieiDischiInput").value=nuovoVal;
    document.getElementById("cercaIMieiDischiSuggerimenti").style.visibility = "hidden";
}
function adviceCercaIMieiDischiArtista(){
     //aggiunta caratteri speciali per ottimizzare la ricerca
    const nuovoVal = document.getElementById("cercaIMieiDischiInput").value +":A";
    document.getElementById("cercaIMieiDischiInput").value=nuovoVal;
    document.getElementById("cercaIMieiDischiSuggerimenti").style.visibility = "hidden";
}
function adviceCercaIMieiDischiGenere(){
     //aggiunta caratteri speciali per ottimizzare la ricerca
    const nuovoVal = document.getElementById("cercaIMieiDischiInput").value +":G";
    document.getElementById("cercaIMieiDischiInput").value=nuovoVal;
    document.getElementById("cercaIMieiDischiSuggerimenti").style.visibility = "hidden";
}