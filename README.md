# App CallBlock
Utilizzare Android Studio >= 2.0 con Gradle >= 2.0

## Dove trovare i file HTML e JS?
Navigando nella directory `src/main/assets/www` è possibile trovare i files HTML e JS

## Funzionamento
L'app si appoggia a livello d'interfaccia grafica ai files HTML e JS, utilizzando JS come ponte tra l'interazione dell'utente e Java.
<br/>La funzione di blocco chiamate è interamente scritta in Java.

## To-Do:
* Sistemazione a livello grafico dell'interfaccia utente
* Rimozione del disclaimer
* Prima pubblicazione di un upgrade (a seguito dell'ottenimento del `keystore`)
* <b>Fix:</b> All'aggiunta di un numero di telefono in BlackList, questo non viene mandato al database online finché non viene ricevuta una chiamata da quel numero.
