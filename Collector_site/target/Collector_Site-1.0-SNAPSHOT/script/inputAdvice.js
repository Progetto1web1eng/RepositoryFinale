/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function adviceCercaIMieiDischiVisible(){
    //suggerimenti visibili quando cominciamo a scrivere qualcosa
        document.getElementById("cercaIMieiDischiSuggerimenti").style.visibility = "visible";
    
}

function adviceHeadBarVisible(){
    //suggerimenti visibili quando cominciamo a scrivere qualcosa
    document.getElementById("headBarSuggerimenti").style.visibility = "visible";
}
function adviceHeadBar(){
    
    //suggerimenti nascosti se l'input torna vuoto
    if(document.getElementById("headBarInput").value===""){
        document.getElementById("headBarSuggerimenti").style.visibility = "hidden";
    }
    //autocompletamento suggerimenti
    const inputV = document.getElementById("headBarInput").value;
    document.getElementById("hBSS1").innerHTML=inputV;
    document.getElementById("hBSS2").innerHTML=inputV;
    document.getElementById("hBSS3").innerHTML=inputV;
    document.getElementById("hBSS4").innerHTML=inputV;
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


function adviceHeadBarUsername(){
    //aggiunta caratteri speciali per ottimizzare la ricerca
    const nuovoVal = document.getElementById("headBarInput").value +":U";
    document.getElementById("headBarInput").value=nuovoVal;
    document.getElementById("headBarSuggerimenti").style.visibility = "hidden";
}
function adviceHeadBarCollezione(){
     //aggiunta caratteri speciali per ottimizzare la ricerca
    const nuovoVal = document.getElementById("headBarInput").value +":C";
    document.getElementById("headBarInput").value=nuovoVal;
    document.getElementById("headBarSuggerimenti").style.visibility = "hidden";
}
function adviceHeadBarDisco(){
     //aggiunta caratteri speciali per ottimizzare la ricerca
    const nuovoVal = document.getElementById("headBarInput").value +":D";
    document.getElementById("headBarInput").value=nuovoVal;
    document.getElementById("headBarSuggerimenti").style.visibility = "hidden";
}
function adviceHeadBarArtista(){
    //aggiunta caratteri speciali per ottimizzare la ricerca
    const nuovoVal = document.getElementById("headBarInput").value +":A";
    document.getElementById("headBarInput").value=nuovoVal;
    document.getElementById("headBarSuggerimenti").style.visibility = "hidden";
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