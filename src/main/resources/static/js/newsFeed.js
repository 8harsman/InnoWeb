
var completelist= document.getElementById("ereignisListe");

completelist.innerHTML = '<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-1]) + '</span> </li>'
    +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-2]) + '</span> </li>'
    +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-3]) + '</span> </li>'
    +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-4]) + '</span> </li>'
    +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-5]) + '</span> </li>'
    +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-6]) + '</span> </li>'
    +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-7]) + '</span> </li>';

function getEreignisText(ereignis) {
    switch(ereignis.art) {
        case "InnovationNeu":
            return getNeueInnovationText(ereignis);
        case "BewertungNeu":
            return getNeueBewertungText(ereignis);
        case "AnhangNeu":
            return getNeueAnhangText(ereignis);
        default:
            return "";
    }
}

function getNeueInnovationText(ereignis) {
    return getFormattedDateString(ereignis.datum) + ': ' + ereignis.ersteller.vorname + ' ' + ereignis.ersteller.nachname +
        ' hat die Innovation <a href=\"' + window.location.href + 'details/' + ereignis.innovation.id + '\">' + ereignis.innovation.titel + '</a> angelegt';
}

function getNeueBewertungText(ereignis) {
    return getFormattedDateString(ereignis.datum) + ': ' + ereignis.ersteller.vorname + ' ' + ereignis.ersteller.nachname +
        ' hat die Innovation <a href=\"' + window.location.href + 'details/' + ereignis.innovation.id + '\">' + ereignis.innovation.titel + '</a> bewertet';
}

function getNeueAnhangText(ereignis) {
    return getFormattedDateString(ereignis.datum) + ': ' + ereignis.ersteller.vorname + ' ' + ereignis.ersteller.nachname +
        ' hat einen Anhang zur Innovation <a href=\"' + window.location.href + 'details/' + ereignis.innovation.id + '\">' + ereignis.innovation.titel + '</a> hinzugefügt';
}

function getFormattedDateString(date) {
    var result = date.substr(0, 16);
    return result.substr(0, 10) + ' ' + result.substr(11, 16);
}



console.log(ereignisse);
var counter= ereignisse.length-8;

    document.getElementById("eintragButton").addEventListener("click", function(){
    console.log("EventListener");
    var completelist= document.getElementById("ereignisListe");

    completelist.innerHTML += '<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[counter]) + '</span> </li>'
                               +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[counter-1]) + '</span> </li>'
                               +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[counter-2]) + '</span> </li>'
                               +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[counter-3]) + '</span> </li>'
                               +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[counter-4]) + '</span> </li>';
    counter = counter-5;
    });

    document.getElementById("eintragButtonUp").addEventListener("click", function(){
    console.log("EventListener");
    var completelist= document.getElementById("ereignisListe");


    completelist.innerHTML = '<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-1]) + '</span> </li>'
                            +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-2]) + '</span> </li>'
                            +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-3]) + '</span> </li>'
                            +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-4]) + '</span> </li>'
                            +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-5]) + '</span> </li>'
                            +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-6]) + '</span> </li>'
                            +'<li class="list-group-item-info mt-3"> <span>' + getEreignisText(ereignisse[ereignisse.length-7]) + '</span> </li>';

   counter= ereignisse.length-8;

    });

