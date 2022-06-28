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
    alert(json.length);
    for(var index=0;index<3;index++){
        if(input.toUpperCase()===(json[index].nomeDisco.substring(0,l)).toUpperCase()){
            if(i===0){
                document.getElementById("inserisciDiscoHDSJS1").style.visibility="visible";
                document.getElementById("inserisciDiscoHDSJSpan1").innerHTML=json[index].nomeDisco;
                i++;
            }
            else if(i===1){
                document.getElementById("inserisciDiscoHDSJS2").style.visibility="visible";
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


function completamento(){
    setTimeout(
        function(){
            document.getElementById("inserisciDiscoHidden").style.visibility="visible";
            const input = document.getElementById("inserisciDiscoNomeDisco").value;
            printJSON(input);
            
    },1000);      
}
