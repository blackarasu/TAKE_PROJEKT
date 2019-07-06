var divId = null;
let URL = "http://localhost:8080/liga/rest/liga/";
const errorMsg = "<p class=\"error\">Coś poszło nie tak.</br>Po więcej informacji zgłoś się do twórcy aplikacji.</p>";
document.body.onload = function(){
    let date = new Date();
    document.getElementById('actual-year').innerText = "-"+date.getFullYear();
    selector.value = 'player';
    //sciagnij podpowiedzi
    loadDoc("http://localhost:8080/liga/rest/liga/clubs",loadDatalistClubs);
    loadDoc("http://localhost:8080/liga/rest/liga/players",loadDatalistPlayers);
    loadDoc("http://localhost:8080/liga/rest/liga/matches",loadDatalistMatches);
}
function prepareUIInput(table){
    document.getElementById('button-get').style.display = "inline-block";
    switch(table){
        case "player":
            return "<input class=\"fname\" type=\"text\" placeholder=\"Imie\"/> "+
                    "<input class=\"lname\" type=\"text\" placeholder=\"Nazwisko\"/> "+
                    "<input class=\"position\" type=\"text\" placeholder=\"Pozycja\" list=\"lposition\" maxlength=\"3\"/> "+
                    "<input class=\"age\" type=\"number\" placeholder=\"Wiek\" min=\"5\" max=\"50\"/> "+
                    "<input class=\"clubId\" type=\"text\" placeholder=\"Klub (do dodania)\" list=\"lclubs\"/> ";//dodac pobranie druzyn z serwera do dodania
        case "club":
            return "<input class=\"name\" type=\"text\" placeholder=\"Nazwa\" /> "+
                    "<input class=\"city\" type=\"text\" placeholder=\"Miasto\" /> ";
        case "match":
            return "<input class=\"clubId1\" type=\"text\" placeholder=\"Gospodarz\" list=\"lclubs\" /> "+
                    "<input class=\"clubId2\" type=\"text\" placeholder=\"Gość\" list=\"lclubs\" /> ";//dodac pobranie druzyn z serwera by stworzyc mecz
        case "goal":
            document.getElementById('button-get').style.display = "none";
            return "<input class=\"atMinute\" type=\"number\" placeholder=\"Minuta\" /> "+
                    "<input class=\"matchId\" type=\"text\" placeholder=\"Mecz\" list=\"lmatches\" /> "+
                    "<input class=\"scorerId\" type=\"text\" placeholder=\"Strzelec (gracz)\" list=\"lplayers\" /> "+
                    "<input class=\"assistId\" type=\"text\" placeholder=\"Asystujący (gracz)\" list=\"lplayers\" /> ";//dodac pobranie graczy z druzyny
        default:
            return errorMsg;
    }
}
var selector = document.getElementsByName('table')[0];
var contentInput = document.getElementById('content-input');
var contentOutput = document.getElementById('content-data');
selector.onchange = function(){
    contentInput.innerHTML = prepareUIInput(this.value);
};
function prepareGetDataInput(table){
    let URL;
    var div = table.parentNode;
    switch(table.value){
        case "player":
            divId = null;
            const fname = div.getElementsByClassName('fname')[0].value;
            const lname = div.getElementsByClassName('lname')[0].value;
            const age = div.getElementsByClassName('age')[0].value;
            const position = div.getElementsByClassName('position')[0].value;
            URL ="player?fn="+fname+"&ln="+lname+"&p="+position+"&a="+age;
            return {url:URL, x:"200", functionCall: loadPlayers};
        case "club":
            divId = null;
            const name = div.getElementsByClassName('name')[0].value;
            const city = div.getElementsByClassName('city')[0].value;
            URL ="club?n="+name+"&c="+city;
            return {url:URL, x:"200", functionCall: loadClubs};
        case "match":
            divId = null;
            const clubId1 = div.getElementsByClassName('clubId1')[0].value==""?0:div.getElementsByClassName('clubId1')[0].value;
            const clubId2 = div.getElementsByClassName('clubId2')[0].value==""?0:div.getElementsByClassName('clubId2')[0].value;
            URL = "clubs/"+clubId1+","+clubId2+"/matches";
            return {url:URL, x:"200", functionCall: loadMatches};
            //dodac kolejne gety (dla matchow dryzyny konkretnej, goli,asyst,participation gracza,gole z meczow)
        case "playersGoals":
        case "playersAssists":
        case "playersParticipation":
            divId = div.id;
            URL = "player/"+divId+"/participation";
            return {url:URL, x:"200", functionCall: loadGoals};
        case "clubsMatches":
            divId = div.id;
            URL = "club/"+divId+"/matches";
            return {url:URL, x:"200", functionCall: loadClubsMatches};
        case "clubsPlayers":
            divId = div.id;
            URL = "club/"+divId+"/players";
            return {url:URL, x:"200", functionCall: loadClubsPlayers};
        case "matchesGoals":
            divId = div.id;
            URL = "match/"+divId+"/goals";
            return {url:URL, x:"200", functionCall: loadGoals};
        case "deletePlayer":
            divId = null;
            URL = "player/"+div.id;
            return {url:URL, x:"200"};
        case "deleteMatch":
            divId = null;
            URL = "match/"+div.id;
            return {url:URL, x:"200"};
        case "deleteClub":
            divId = null;
            URL = "club/"+div.id;
            return {url:URL, x:"200"};
        case "deleteGoal":
            divId = null;
            URL = "goal/"+div.id;
            return {url:URL, x:"200"};
        default:
            return {x:"404"};
    }
}
function preparePP(button){
    let xmlPacked;
    var div = button.parentNode;
    
    switch(button.value){
        case "player":
            const fname = div.getElementsByClassName('fname')[0].value;
            const lname = div.getElementsByClassName('lname')[0].value;
            const age = div.getElementsByClassName('age')[0].value;
            const position = div.getElementsByClassName('position')[0].value;
            const clubId = div.getElementsByClassName('clubId').length>0?div.getElementsByClassName('clubId')[0].value:null;
            xmlPacked = packPlayer({id:isNaN(div.id)?0:div.id,fname: fname,lname: lname,age: age,position: position,clubId: clubId});
            return {x:"200",url:"player",xmlPacked: xmlPacked};
        case "club":
            const name = div.getElementsByClassName('name')[0].value;
            const city = div.getElementsByClassName('city')[0].value;
            xmlPacked = packClub({id:isNaN(div.id)?0:div.id,name: name,city: city});
            return {x:"200",url:"club",xmlPacked: xmlPacked};
        case "match":
            const clubId1 = div.getElementsByClassName('clubId1')[0].value;
            const clubId2 = div.getElementsByClassName('clubId2')[0].value;
            xmlPacked = packMatch({id:isNaN(div.id)?0:div.id,clubId1: clubId1,clubId2: clubId2});
            return {x:"200",url:"match",xmlPacked: xmlPacked};
        case "goal":
            const atMinute = div.getElementsByClassName('atMinute')[0].value;
            const matchId = div.getElementsByClassName('matchId')[0].value;
            const scorerId = div.getElementsByClassName('scorerId')[0].value;
            const assistId = div.getElementsByClassName('assistId')[0].value;
            xmlPacked = packGoal({id:isNaN(div.id)?0:div.id,atMinute: atMinute, matchId: matchId, scorerId: scorerId, assistId: assistId});
            return {x:"200",url:"goal",xmlPacked: xmlPacked};
        default:
            return {x:"404"};
    }
}
var buttonGet = document.getElementById('button-get');
buttonGet.onclick = function(){getData(selector);};
function getData(element){
    let restData = prepareGetDataInput(element);
    if(restData.x=="404"){
        console.log(restData.x);
        contentInput.innerHTML = errorMsg;
        return;
    }
    let fullURL = encodeURI(URL + restData.url);
    loadDoc(fullURL, restData.functionCall);
};
function loadDoc(URL, cFunction){
    var xmlhttp;
    if (window.XMLHttpRequest) {
        // code for modern browsers
        xmlhttp = new XMLHttpRequest();
    } else {
        // code for old IE browsers
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    console.log(URL);
    xmlhttp.onreadystatechange = function(){
        if(this.readyState == 4){
            if(this.status == 200)
            {
                var XmlDoc = xmlhttp.responseXML;
                cFunction(XmlDoc, divId);
            }
            else if(this.status == 204){
                globalStatus.style.display = "block";
                globalStatus.innerHTML = "<span onclick=\"this.parentNode.style.display = 'none';\">&times;</span><span>Brak Wynikow do zwrócenia.</span>";
                clearTimeout(statusTimeOut);
                statusTimeOut=setTimeout(function(){
                    globalStatus.style.display = "none";
                },10000);
            }
            else if(this.status == 404){
                globalStatus.style.display = "block";
                globalStatus.innerHTML = "<span onclick=\"this.parentNode.style.display = 'none';\">&times;</span><span>Nie znaleziono adresu zapytania.</span>";
                clearTimeout(statusTimeOut);
                statusTimeOut=setTimeout(function(){
                    globalStatus.style.display = "none";
                },10000);
            }
            else if(this.status >= 500 && this.status < 600){
                contentOutput.innerHTML = "<h1 class=\"error\">"+this.status+" "+this.statusText+"</h1>"+errorMsg;
            }
        }
    }
    xmlhttp.open("GET", URL, true);
    xmlhttp.setRequestHeader("Accept-Language", "pl");
    xmlhttp.send();
}
var globalStatus =  document.getElementById('status');
var statusTimeOut;
var buttonPost = document.getElementById('button-post');
buttonPost.onclick = function(){create(selector);};
function sendData(URL, xmlPacked, method){
    var xmlhttp = new XMLHttpRequest;
    if (window.XMLHttpRequest) {
        // code for modern browsers
        xmlhttp = new XMLHttpRequest();
    } else {
        // code for old IE browsers
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    console.log(URL);
    xmlhttp.onreadystatechange = function(){
        if(this.readyState == 4){
            if(this.status == 200)
            {
                if(URL.includes("club")){
                    loadDoc("http://localhost:8080/liga/rest/liga/clubs",loadDatalistClubs);
                }
                else if(URL.includes("player")){
                    loadDoc("http://localhost:8080/liga/rest/liga/players",loadDatalistPlayers);
                }
                else if(URL.includes("match")){
                    loadDoc("http://localhost:8080/liga/rest/liga/matches",loadDatalistMatches);
                }
                globalStatus.style.display = "block";
                globalStatus.innerHTML = "<span onclick=\"this.parentNode.style.display = 'none';\">&times;</span><span>"+xmlhttp.responseText+"</span>";
                clearTimeout(statusTimeOut);
                statusTimeOut=setTimeout(function(){
                    globalStatus.style.display = "none";
                },10000);
            }
            else if(this.status == 404){
                globalStatus.style.display = "block";
                globalStatus.innerHTML = "<span onclick=\"this.parentNode.style.display = 'none';\">&times;</span><span>Nie znaleziono adresu zapytania.</span>";
                clearTimeout(statusTimeOut);
                statusTimeOut=setTimeout(function(){
                    globalStatus.style.display = "none";
                },10000);
            }
            else if(this.status >= 500 && this.status < 600){
                contentOutput.innerHTML = "<h1 class=\"error\">"+this.status+": "+this.statusText+"</h1>"+errorMsg;
            }
            else{
                globalStatus.style.display = "block";
                globalStatus.innerHTML = "<span onclick=\"this.parentNode.style.display = 'none';\">&times;</span><span>"+this.status+": "+this.statusText+"</span>";
                clearTimeout(statusTimeOut);
                statusTimeOut=setTimeout(function(){
                    globalStatus.style.display = "none";
                },10000);
            }
        }
    }
    xmlhttp.open(method, URL, true);
    if(method=="DELETE"){
        xmlhttp.send();
    }
    else{
        xmlhttp.setRequestHeader("Content-type", "application/xml; charset=utf-8");
        xmlhttp.setRequestHeader("Content-Language", "pl");
        xmlhttp.send(xmlPacked);
    }
}
//Wypakowują xml i zapisuja dane do content-data
function loadPlayers(xmlDoc){
    var i;
    var textHtml = "<h1>Zawodnicy</h1>";
    var x = xmlDoc.getElementsByTagName("player");
    //console.log("jestem w loadPlayers hi");
    //rozpakowanie xml
    for(i=0;i<x.length;++i){
        textHtml+="<div id=\""+x[i].getAttribute("id")+"\" class=\"contentData player\">"+
                "<input title=\"Imię\" type=\"text\" class=\"fname\" value=\""+x[i].getElementsByTagName("firstName")[0].childNodes[0].nodeValue+"\" /> "+
                "<input title=\"Nazwisko\" type=\"text\" class=\"lname\" value=\""+x[i].getElementsByTagName("lastName")[0].childNodes[0].nodeValue+"\" /> "+
                "<input list=\"lposition\" title=\"Pozycja\" type=\"text\" class=\"position\" value=\""+x[i].getElementsByTagName("position")[0].childNodes[0].nodeValue+"\" /> "+
                "<input title=\"Wiek\" type=\"number\" class=\"age\" value=\""+x[i].getElementsByTagName("age")[0].childNodes[0].nodeValue+"\" /> "+
                "<button type=\"button\" onclick=\"update(this);\" value=\"player\">Aktualizuj zawodnika</button>"+
                "<button type=\"button\" onclick=\"deleteMethod(this);\" value=\"deletePlayer\">Usuń zawodnika</button>"+
                "<button type=\"button\" onclick=\"getData(this);\" value=\"playersGoals\">Pobierz bramki gracza</button>"+
                "</br><div id=\""+x[i].getElementsByTagName("club")[0].getAttribute("id")+"\" class=\"contentData club\">"+
                "<input title=\"Nazwa\" type=\"text\" class=\"name\" value=\""+x[i].getElementsByTagName("club")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue+"\" /> "+
                "<input title=\"Miasto\" type=\"text\" class=\"city\" value=\""+x[i].getElementsByTagName("club")[0].getElementsByTagName("city")[0].childNodes[0].nodeValue+"\" /> "+
                "<button type=\"button\" onclick=\"update(this);\" value=\"club\">Aktualizuj klub</button>"+
                "<button type=\"button\" onclick=\"deleteMethod(this);\" value=\"deleteClub\">Usuń klub</button>"+
                "</div>"+
                "</br><div class=\"contentData goals\"></div>"+
                "</div>";
    }
    //wpisanie danych do HTML
    contentOutput.innerHTML = textHtml;
};
function loadDatalistPlayers(xmlDoc){
    //wpisanie do datalist lplayers
    console.log("jestem w loadDatalistPlayers");
    var i;
    var textHtml = "";
    var x = xmlDoc.getElementsByTagName("player");
    //rozpakowanie xml
    for(i=0;i<x.length;++i){
        textHtml+="<option value=\""+x[i].getAttribute("id")+"\" >"+
                x[i].getElementsByTagName("firstName")[0].childNodes[0].nodeValue+" "+
                x[i].getElementsByTagName("lastName")[0].childNodes[0].nodeValue+"("+
                x[i].getElementsByTagName("club")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue+
                x[i].getElementsByTagName("club")[0].getElementsByTagName("city")[0].childNodes[0].nodeValue+")</option>";
    }
    //wpisanie danych do datalist
    document.getElementById('lplayers').innerHTML = textHtml;
};
function loadClubs(xmlDoc){
    var i;
    var textHtml = "<h1>Kluby</h1>";
    var x = xmlDoc.getElementsByTagName("club");
    //rozpakowanie xml
    for(i=0;i<x.length;++i){
        textHtml+="<div id=\""+x[i].getAttribute("id")+"\" class=\"contentData club\">"+
                "<input title=\"Nazwa\" type=\"text\" class=\"name\" value=\""+x[i].getElementsByTagName("name")[0].childNodes[0].nodeValue+"\" /> "+
                "<input title=\"Miasto\" type=\"text\" class=\"city\" value=\""+x[i].getElementsByTagName("city")[0].childNodes[0].nodeValue+"\" /> "+
                "<button type=\"button\" onclick=\"update(this);\" value=\"club\">Aktualizuj Klub</button>"+
                "<button type=\"button\" onclick=\"deleteMethod(this);\" value=\"deleteClub\">Usuń Klub</button>"+
                "<button type=\"button\" onclick=\"getData(this);\" value=\"clubsMatches\">Pobierz mecze</button>"+
                "<button type=\"button\" onclick=\"getData(this);\" value=\"clubsPlayers\">Pobierz zawodników</button>"+
                "<div class=\"contentData matches\"></div></br>"+
                "<div class=\"contentData players\"></div></br>"+
                "</div>";
    }
    //wpisanie danych do HTML
    contentOutput.innerHTML = textHtml;
};
function loadDatalistClubs(xmlDoc){
    //lclubs
    console.log("Jestem w loadDatalistClubs");
    var i;
    var textHtml = "";
    var x = xmlDoc.getElementsByTagName("club");
    //rozpakowanie xml
    for(i=0;i<x.length;++i){
        textHtml+="<option value=\""+x[i].getAttribute("id")+"\" >"+
                x[i].getElementsByTagName("name")[0].childNodes[0].nodeValue+" "+
                x[i].getElementsByTagName("city")[0].childNodes[0].nodeValue+"</option>";
    }
    //wpisanie danych do datalist
    document.getElementById('lclubs').innerHTML = textHtml;
};
function loadMatches(xmlDoc){
    //console.log("jestem w matches");
    var i;
    var textHtml = "<h1>Mecze</h1>";
    var x = xmlDoc.getElementsByTagName("match");
    //rozpakowanie xml
    for(i=0;i<x.length;++i){
        var homeTeam = x[i].getElementsByTagName("homeTeam").length>0?"<input list=\"lclubs\" title=\"Gospodarz\" type=\"text\" class=\"homeTeam\" value=\""+x[i].getElementsByTagName("homeTeam")[0].getAttribute("id")+"\" /> ":"<input list=\"lclubs\" title=\"Gospodarz\" type=\"text\" class=\"clubId1\" />"
        var homeTeamName = x[i].getElementsByTagName("homeTeam").length>0?"<input disabled title=\"Gospodarz\" type=\"text\" value=\""+x[i].getElementsByTagName("homeTeam")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue+"\" />":" ";
        var homeGoals = x[i].getElementsByTagName("homeGoals").length>0?"<input disabled title=\"Bramki Gospodarza\" type=\"text\" class=\"homeGoals\" value=\""+x[i].getElementsByTagName("homeGoals")[0].childNodes[0].nodeValue+"\" />":" ";
        var guestGoals = x[i].getElementsByTagName("guestGoals").length>0?"<input disabled title=\"Bramki Gościa\" type=\"text\" class=\"guestGoals\" value=\""+x[i].getElementsByTagName("guestGoals")[0].childNodes[0].nodeValue+"\" /> ":" ";
        var guestTeam = x[i].getElementsByTagName("guestTeam").length>0?"<input list=\"lclubs\" title=\"Gość\" type=\"text\" class=\"guestTeam\" value=\""+x[i].getElementsByTagName("guestTeam")[0].getAttribute("id")+"\" /> ": "<input list=\"lclubs\" title=\"Gość\" type=\"text\" class=\"clubId2\" />";
        var guestTeamName = x[i].getElementsByTagName("guestTeam").length>0?"<input disabled title=\"Gość\" type=\"text\" value=\""+x[i].getElementsByTagName("guestTeam")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue+"\" />":" ";
        textHtml+="<div id=\""+x[i].getAttribute("id")+"\" class=\"contentData match\">"+
                homeTeam+homeTeamName+homeGoals+"<span>:</span>"+guestGoals+guestTeam+guestTeamName+
                "<button type=\"button\" onclick=\"update(this);\" value=\"match\">Aktualizuj mecz</button>"+
                "<button type=\"button\" onclick=\"deleteMethod(this);\" value=\"deleteMatch\">Usuń mecz</button>"+
                "<button type=\"button\" onclick=\"getData(this);\" value=\"matchesGoals\">Pobierz bramki</button>"+
                "<div class=\"contentData goals\"></div></div></br>";
    }
    //wpisanie danych do HTML
    contentOutput.innerHTML = textHtml;
};
function loadDatalistMatches(xmlDoc){
    //lmatches
    console.log("Jestem w loadDatalistMatches");
    var i;
    var textHtml = "";
    var x = xmlDoc.getElementsByTagName("match");
    //rozpakowanie xml
    if(x.length)
    for(i=0;i<x.length;++i){
        var homeTeam = x[i].getElementsByTagName("homeTeam").length>0?x[i].getElementsByTagName("homeTeam")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue:" "
        var homeGoals = x[i].getElementsByTagName("homeGoals").length>0?x[i].getElementsByTagName("homeGoals")[0].childNodes[0].nodeValue:" ";
        var guestGoals = x[i].getElementsByTagName("guestGoals").length>0?x[i].getElementsByTagName("guestGoals")[0].childNodes[0].nodeValue:" ";
        var guestTeam = x[i].getElementsByTagName("guestTeam").length>0?x[i].getElementsByTagName("guestTeam")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue: " ";
        textHtml+="<option value=\""+x[i].getAttribute("id")+"\" >"+
                "("+homeGoals+")"+homeTeam+"-"+guestTeam+"("+guestGoals+")"+"</option>";
    }
    //wpisanie danych do datalist
    document.getElementById('lmatches').innerHTML = textHtml;
}
//Wypakowują xml i zapisują do odpowiedniego diva pod rekordem
function loadGoals(xmlDoc, divId){//goals,assists,participation
    //console.log("jestem w bramkach gracza/bramkach strzelonych w meczu");
    var i;
    var textHtml = "";
    var x = xmlDoc.getElementsByTagName("goal");
    //rozpakowanie xml
    for(i=0;i<x.length;++i){
        var assist = x[i].getElementsByTagName("assist").length>0?x[i].getElementsByTagName("assist")[0]:0;
        var assistHtml = assist==0?" ":"<input list=\"lplayers\" title=\"Asystujący\" type=\"text\" class=\"assistId\" value=\""+assist.getAttribute("id")+"\" /> ";
        var assistName = assist==0?" ":"<input disabled title=\"Asystujący\" type=\"text\" value=\""+assist.getElementsByTagName("firstName")[0].childNodes[0].nodeValue+" "+assist.getElementsByTagName("lastName")[0].childNodes[0].nodeValue+"\" />";
        var scorerName = x[i].getElementsByTagName("scorer").length>0?"<input disabled title=\"Strzelający\" type=\"text\" value=\""+x[i].getElementsByTagName("scorer")[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue+" "+x[i].getElementsByTagName("scorer")[0].getElementsByTagName("lastName")[0].childNodes[0].nodeValue+"\" />":" ";
        var matchName = x[i].getElementsByTagName("match").length>0?"<input disabled title=\"Gospodarz\" type=\"text\" value=\""+x[i].getElementsByTagName("match")[0].getElementsByTagName("homeTeam")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue+"-"+x[i].getElementsByTagName("match")[0].getElementsByTagName("guestTeam")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue+"\" />":" ";
        textHtml+="<div id=\""+x[i].getAttribute("id")+"\" class=\"contentData goal\">"+
                "<input title=\"Minuta\" type=\"number\" min=\"0\" class=\"atMinute\" value=\""+x[i].getElementsByTagName("atMinute")[0].childNodes[0].nodeValue+"\" /> "+
                "<input list=\"lplayers\" title=\"Strzelający\" type=\"text\" class=\"scorerId\" value=\""+x[i].getElementsByTagName("scorer")[0].getAttribute("id")+"\" /> "+
                scorerName+assistHtml+assistName+
                "<input list=\"lmatches\" title=\"Mecz\" type=\"text\" class=\"matchId\" value=\""+x[i].getElementsByTagName("match")[0].getAttribute("id")+"\" /> "+
                matchName+
                "<button type=\"button\" onclick=\"update(this);\" value=\"goal\">Aktualizuj gol</button>"+
                "<button type=\"button\" onclick=\"deleteMethod(this);\" value=\"deleteGoal\">Usuń gol</button>"+
                "</div></br>";
    }
    //wpisanie danych do HTML
    var players = document.getElementsByClassName("player");
    var player=null;
    for(var i = 0;i<players.length; ++i){
        if(players[i].id==divId){
            player = players[i];
            break;
        }
    }
    if(player==null){
        document.getElementById(divId).getElementsByClassName("goals")[0].innerHTML = textHtml;
    }
    else{
        player.getElementsByClassName("goals")[0].innerHTML = textHtml;
    }
};
function loadClubsMatches(xmlDoc, divId){
    var i;
    var textHtml = "";
    var x = xmlDoc.getElementsByTagName("match");
    //console.log("jestem w loadClubsMatches hi");
    //rozpakowanie xml
    for(i=0;i<x.length;++i){
        var homeTeamName = x[i].getElementsByTagName("homeTeam").length>0?"<input disabled title=\"Gospodarz\" type=\"text\" value=\""+x[i].getElementsByTagName("homeTeam")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue+"\" />":" ";
        var guestTeamName = x[i].getElementsByTagName("guestTeam").length>0?"<input disabled title=\"Gość\" type=\"text\" value=\""+x[i].getElementsByTagName("guestTeam")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue+"\" />":" ";
        
        textHtml+="<div id=\""+x[i].getAttribute("id")+"\" class=\"contentData match\">"+
                "<input disabled title=\"Bramki Gospodarza\" type=\"text\" class=\"homeGoals\" value=\""+x[i].getElementsByTagName("homeGoals")[0].childNodes[0].nodeValue+"\" /> "+
                "<input disabled title=\"Bramki Gościa\" type=\"text\" class=\"guestGoals\" value=\""+x[i].getElementsByTagName("guestGoals")[0].childNodes[0].nodeValue+"\" />"+
                "<input list=\"lclubs\" title=\"Gospodarz\" type=\"text\" class=\"clubId1\" value=\""+x[i].getElementsByTagName("homeTeam")[0].getAttribute("id")+"\" /> "+
                homeTeamName+
                "<input list=\"lclubs\" title=\"Gość\" type=\"text\" class=\"clubId2\" value=\""+x[i].getElementsByTagName("guestTeam")[0].getAttribute("id")+"\" /> "+
                guestTeamName+
                "<button type=\"button\" onclick=\"update(this);\" value=\"match\">Aktualizuj mecz</button>"+
                "<button type=\"button\" onclick=\"deleteMethod(this);\" value=\"deleteMatch\">Usuń mecz</button>"+
                "</div></br>";
    }
    //wpisanie danych do HTML
    var clubs = document.getElementsByClassName("club");
    var club=null;
    for(var i = 0;i<clubs.length; ++i){
        if(clubs[i].id==divId){
            club = clubs[i];
            break;
        }
    }
    if(club==null){
        contentOutput.innerHTML = textHtml;
    }
    else{
        club.getElementsByClassName("matches")[0].innerHTML = textHtml;
    }
};
function loadClubsPlayers(xmlDoc, divId){
    var i;
    var textHtml = "";
    var x = xmlDoc.getElementsByTagName("player");
    //console.log("jestem w loadClubsPlayers hi");
    //rozpakowanie xml
    for(i=0;i<x.length;++i){
        textHtml+="<div id=\""+x[i].getAttribute("id")+"\" class=\"contentData player\">"+
                "<input title=\"Imię\" type=\"text\" class=\"fname\" value=\""+x[i].getElementsByTagName("firstName")[0].childNodes[0].nodeValue+"\" /> "+
                "<input title=\"Nazwisko\" type=\"text\" class=\"lname\" value=\""+x[i].getElementsByTagName("lastName")[0].childNodes[0].nodeValue+"\" /> "+
                "<input list=\"lposition\" title=\"Pozycja\" type=\"text\" class=\"position\" value=\""+x[i].getElementsByTagName("position")[0].childNodes[0].nodeValue+"\" /> "+
                "<input title=\"Wiek\" type=\"number\" class=\"age\" value=\""+x[i].getElementsByTagName("age")[0].childNodes[0].nodeValue+"\" /> "+
                "<button type=\"button\" onclick=\"update(this);\" value=\"player\">Aktualizuj zawodnika</button>"+
                "<button type=\"button\" onclick=\"deleteMethod(this);\" value=\"deletePlayer\">Usuń zawodnika</button>"+
                "</div></br>";
    }
    //wpisanie danych do HTML
    var clubs = document.getElementsByClassName("club");
    var club=null;
    for(var i = 0;i<clubs.length; ++i){
        if(clubs[i].id==divId){
            club = clubs[i];
            break;
        }
    }
    if(club==null){
        contentOutput.innerHTML = textHtml;
    }
    else{
        club.getElementsByClassName("players")[0].innerHTML = textHtml;
    }
    //document.getElementById(divId).getElementsByClassName("players")[0].innerHTML = textHtml;
};
//dodac funkcje na kazdy GET
//Pakują XML i wysylają na odpowiedni adres
function packPlayer(restData){
    var club_id = restData.clubId == null ? " ": "<club_id>"+restData.clubId+"</club_id>";
    var id = restData.id == 0? "":"id=\""+restData.id+"\""; 
    var xmlPacked = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><player "+id+"><age>"+restData.age+"</age>"+
                    "<firstName>"+restData.fname+"</firstName>"+"<lastName>"+restData.lname+"</lastName>"+
                    "<position>"+restData.position+"</position>"+
                    club_id+"</player>";
    return xmlPacked;
};
function packClub(restData){
    var id = restData.id == 0? "":"id=\""+restData.id+"\""; 
    var xmlPacked = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><club "+id+"><city>"+restData.city+"</city>"+
                    "<name>"+restData.name+"</name></club>";
    return xmlPacked;
};
function packMatch(restData){
    var id = restData.id == 0? "":"id=\""+restData.id+"\""; 
    var xmlPacked = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><match "+id+"><home_id>"+restData.clubId1+"</home_id>"+
                    "<guest_id>"+restData.clubId2+"</guest_id></match>";
    return xmlPacked;
};
function packGoal(restData){
    var id = restData.id == 0? "":"id=\""+restData.id+"\""; 
    var xmlPacked = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><goal "+id+"><atMinute>"+restData.atMinute+"</atMinute>"+
                    "<match_id>"+restData.matchId+"</match_id><scorer_id>"+restData.scorerId+"</scorer_id>"+
                    "<assist_id>"+restData.assistId+"</assist_id></goal>";
    return xmlPacked;
};
//funkcje aktualizacji danych
function create(element){//method PUT
    var restData = preparePP(element);
    if(restData.x=="404"){
        console.log(restData.x);
        contentInput.innerHTML = errorMsg;
        return;
    }
    let fullURL = URL + restData.url;
    console.log(restData.xmlPacked);
    sendData(fullURL, restData.xmlPacked, "POST");
};
function update(element){//method PUT
    var restData = preparePP(element);
    if(restData.x=="404"){
        console.log(restData.x);
        contentInput.innerHTML = errorMsg;
        return;
    }
    let fullURL = URL + restData.url;
    console.log(restData.xmlPacked);
    sendData(fullURL, restData.xmlPacked, "PUT");
};
function deleteMethod(element){//method DELETE
    var restData = prepareGetDataInput(element);
    if(restData.x=="404"){
        console.log(restData.x);
        contentInput.innerHTML = errorMsg;
        return;
    }
    let fullURL = URL + restData.url;
    sendData(fullURL, null, "DELETE");
    element.parentNode.style.display = "none";
};
