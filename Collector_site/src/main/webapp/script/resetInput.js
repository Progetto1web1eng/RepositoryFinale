/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */



function resetCercaIMieiDischiInput(){
    const cercaIMieiDischiInput = document.getElementById("cercaIMieiDischiInput");
    if(cercaIMieiDischiInput.value==="cerca"){
        cercaIMieiDischiInput.value="";
    }
    //elimino i caratteri speciali se presenti
    if(cercaIMieiDischiInput.value.substring(cercaIMieiDischiInput.value.length-2,cercaIMieiDischiInput.value.length)===":T")
    {
        cercaIMieiDischiInput.value = cercaIMieiDischiInput.value.substring(0,cercaIMieiDischiInput.value.length-2);
    }
    if(cercaIMieiDischiInput.value.substring(cercaIMieiDischiInput.value.length-2,cercaIMieiDischiInput.value.length)===":A")
    {
        cercaIMieiDischiInput.value = cercaIMieiDischiInput.value.substring(0,cercaIMieiDischiInput.value.length-2);
    }
    if(cercaIMieiDischiInput.value.substring(cercaIMieiDischiInput.value.length-2,cercaIMieiDischiInput.value.length)===":G")
    {
        cercaIMieiDischiInput.value = cercaIMieiDischiInput.value.substring(0,cercaIMieiDischiInput.value.length-2);
    }
}   
function resetHeadBarInput(){
    const headBarInput = document.getElementById("headBarInput");
    if(headBarInput.value==="cerca"){
        headBarInput.value="";
    }
}
function resetLoginUsernameInput(){
    const loginUsernameInput = document.getElementById("loginUsernameInput");
    if(loginUsernameInput.value==="Username"){
        loginUsernameInput.value="";
    }
}
function resetLoginPasswordInput(){
    const loginPasswordInput = document.getElementById("loginPasswordInput");
    if(loginPasswordInput.value==="Password"){
        loginPasswordInput.value="";
         loginPasswordInput.type="password";
    }
}
function resetRegistrazionetelefonoInput(){
    const registrazionetelefonoInput = document.getElementById("registrazionetelefonoInput");
    if(registrazionetelefonoInput.value==="Numero cellulare"){
        registrazionetelefonoInput.value="";
    }
}
function resetRegistrazioneEmailInput(){
    const registrazioneEmailInput = document.getElementById("registrazioneEmailInput");
    if(registrazioneEmailInput.value==="Email"){
        registrazioneEmailInput.value="";
    }
}
function resetRegistrazioneUsernameInput(){
    const registrazioneUsernameInput = document.getElementById("registrazioneUsernameInput");
    if(registrazioneUsernameInput.value==="Username"){
        registrazioneUsernameInput.value="";
    }
}
function resetRegistrazionePasswordInput(){
    const registrazionePasswordInput = document.getElementById("registrazionePasswordInput");
    if(registrazionePasswordInput.value==="Password"){
        registrazionePasswordInput.value="";
    }
}
function resetInserisciNomeDiscoInput(){
    const nomeDiscoInput = document.getElementById("inserisciDiscoNomeDisco");
    if(nomeDiscoInput.value==="Nome disco"){
        nomeDiscoInput.value="";
    }
    
}