/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */




/* global myJson */
async function printJSON(input) {
    const response = await fetch("script/dischi.json");
    const json = await response.json();
    const l = input.length;
    let i = 0;
    let leng = Object.keys(json).length;
    for(var index=0;index<leng;index++){
        if(input.toUpperCase()===(json[index].nomeDisco.substring(0,l)).toUpperCase()){
            if(i===0){
                document.getElementById("inserisciDiscoHDSJS1").style.visibility="visible";
                document.getElementById("inserisciDiscoHDSJS1").href=("servletDiProvaInserisciDisco?AdviceD="+json[index].ID);
                document.getElementById("inserisciDiscoHDSJSpan1").innerHTML=json[index].nomeDisco;
                i++;
            }
            else if(i===1){
                document.getElementById("inserisciDiscoHDSJS2").style.visibility="visible";
                document.getElementById("inserisciDiscoHDSJS2").href=("servletDiProvaInserisciDisco?AdviceD="+json[index].ID);
                document.getElementById("inserisciDiscoHDSJSpan2").innerHTML=json[index].nomeDisco;
                i++;
            }
        }
    }
    if(i===0){
        document.getElementById("inserisciDiscoHDSJS1").style.visibility="hidden";
        document.getElementById("inserisciDiscoHDSJS2").style.visibility="hidden";
    }
    if(i===1){
        document.getElementById("inserisciDiscoHDSJS2").style.visibility="hidden";
    }
    
}

async function printJSONArtistaSingolo(input){
    const response = await fetch("script/artistiSingoli.json");
    const json = await response.json();
    const l = input.length;
    let i = 0;
    let leng = Object.keys(json).length;
    for(var index=0;index<leng;index++){
         if(input.toUpperCase()===(json[index].nomeDarte.substring(0,l)).toUpperCase()){
            if(i===0){
                document.getElementById("inserisciArtistaHASJS1").style.visibility="visible";
                document.getElementById("inserisciArtistaHASJS1").href=("servletDiProvaInserisciDisco?AdviceA="+json[index].ID);
                document.getElementById("inserisciArtistaHASJSpan1").innerHTML=json[index].nomeDarte;
                i++;
            }
            else if(i===1){
                document.getElementById("inserisciArtistaHASJS2").style.visibility="visible";
                document.getElementById("inserisciArtistaHASJS2").href=("servletDiProvaInserisciDisco?AdviceA="+json[index].ID);
                document.getElementById("inserisciArtistaHASJSpan2").innerHTML=json[index].nomeDarte;
                i++;
            }
        }
    }
    if(i===0){
        document.getElementById("inserisciArtistaHASJS1").style.visibility="hidden";
        document.getElementById("inserisciArtistaHASJS2").style.visibility="hidden";
    }
    if(i===1){
        document.getElementById("inserisciArtistaHASJS2").style.visibility="hidden";
    }
}

async function printJSONGruppo(input){
    const response = await fetch("script/gruppi.json");
    const json = await response.json();
    const l = input.length;
    let i = 0;
    let leng = Object.keys(json).length;
    for(var index=0;index<leng;index++){
         if(input.toUpperCase()===(json[index].nomeDarte.substring(0,l)).toUpperCase()){
            if(i===0){
                document.getElementById("inserisciArtistaHASJS1").style.visibility="visible";
                document.getElementById("inserisciArtistaHASJS1").href=("servletDiProvaInserisciDisco?AdviceA="+json[index].ID);
                document.getElementById("inserisciArtistaHASJSpan1").innerHTML=json[index].nomeDarte;
                i++;
            }
            else if(i===1){
                document.getElementById("inserisciArtistaHASJS2").style.visibility="visible";
                document.getElementById("inserisciArtistaHASJS2").href=("servletDiProvaInserisciDisco?AdviceA="+json[index].ID);
                document.getElementById("inserisciArtistaHASJSpan2").innerHTML=json[index].nomeDarte;
                i++;
            }
        }
    }
    if(i===0){
        document.getElementById("inserisciArtistaHASJS1").style.visibility="hidden";
        document.getElementById("inserisciArtistaHASJS2").style.visibility="hidden";
    }
    if(i===1){
        document.getElementById("inserisciArtistaHASJS2").style.visibility="hidden";
    }
}




function completamento(){
    setTimeout(
        function(){
            document.getElementById("inserisciDiscoHidden").style.visibility="visible";
            const input = document.getElementById("inserisciDiscoNomeDisco").value;
            printJSON(input);
            
    },500);      
}

function completamentoArtistaSingolo(){
    setTimeout(
            function(){
            document.getElementById("inserisciArtistaHidden").style.visibility="visible";
            const input = document.getElementById("inserisciArtistaDivSingoloInput1").value;
            printJSONArtistaSingolo(input);
    },500);
}

function completamentoGruppo(){
    setTimeout(
            function(){
            document.getElementById("inserisciArtistaHidden").style.visibility="visible";
            const input = document.getElementById("inserisciArtistaDivGruppoInput1").value;
            printJSONGruppo(input);
    },500);
}
