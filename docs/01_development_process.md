# 1. Processo di sviluppo

Per il processo di sviluppo, il team di lavoro ha deciso di utilizzare un approccio _SCRUM-inspired_. In particolare,  [_SCRUM_](https://www.scrum.org/) è un _framework_ di sviluppo software agile, iterativo e incrementale, che consente la gestione dello sviluppo del prodotto.

Specificatamente, tale processo prevede di:
1. nominare uno studente (ad esempio, chi ha l'idea del progetto) che fungerà da _committente_ o esperto del dominio, e che cercherà di garantire l'usabilità e la qualità del risultato;
2. designare uno studente (ad esempio, chi pensa di avere doti di coordinamento) che ricoprirà il ruolo di _product owner_;
3. effettuare un meeting iniziale in cui redigere un _product backlog_ e definire un primo sprint organizzativo;
4. utilizzare sprint corti, da 15-20 ore di lavoro, con l'obiettivo di ottenere ad ogni sprint dei risultati "tangibili", ossia con un valore per gli _stakeholder_;
5. fare meeting frequenti ed a inizio/fine sprint (opportunamente documentati da un brevissimo report del risultato, anch'esso da tenere in versione), realizzando uno _sprint backlog_ per mantenere traccia dell'organizzazione dei lavori.

## 1.1 Meetings

I meetings sono stati realizzati definendo, inizialmente, un ordine del giorno redatto principalmente dal _product owner_, in collaborazione con gli altri membri del gruppo che possono suggerire modifiche o nuovi aspetti da discutere.

Sono stati svolti incontri brevi e frequenti, pressoché quotidiani, in modo da mantenere aggiornati i diversi membri del gruppo sullo stato di avanzamento del progetto.

Per quanto riguarda gli sprint, come specificato precedentemente, essi sono stati svolti nell'arco di una settimana lavorativa e discussi in incontri a inizio e fine periodo. 

In particolare, durante l’incontro di inizio sprint, viene definito il _product backlog_ relativo mentre, durante quello di fine Sprint, vengono discussi i risultati ottenuti e viene revisionato il lavoro svolto.

I diversi meetings sono stati tenuti in modalità _smart-working_, attraverso l’utilizzo di piattaforme per videoconferenze.

Ogni meeting e il rispettivo ordine del giorno sono stati tracciati in un apposito file, che è possibile trovare fra gli artefatti del progetto.

### 1.1.1 Sprint planning

Lo _sprint planning_ è un meeting in cui il team pianifica il lavoro che deve essere svolto e portato a termine durante lo sprint. Questo prevede di individuare i diversi elementi del _product backlog_ e i differenti _sprint tasks_ che devono essere effettuati per poter raggiungere gli obiettivi prefissati. Di conseguenza, durante i diversi _sprint planning_ sono stati discussi i seguenti punti:

1.	definizione dell’obiettivo principale dello sprint;
2.	stato di sviluppo da raggiungere alla fine dello sprint, tramite il raffinamento del _product backlog_;
3.	definizione dei diversi tasks da svolgere per il raggiungimento degli obiettivi preposti;
4.	assegnazione dei task ai diversi membri del gruppo.

### 1.1.2 Sprint review

Lo _sprint review_ è un meeting che ha luogo alla fine dello sprint e che ha l’obiettivo di revisionare e valutare il lavoro svolto dal team di sviluppo. 

Pertanto, durante i differenti _sprint review_ sono stati discussi i seguenti punti:

1.	analisi dell’incremento ottenuto rispetto al risultato precedente, valutando il lavoro che è stato svolto;
2.	aggiornamento e adattamento del _product backlog_;
3.	valutazione di eventuali ritardi rispetto ai tempi definiti in precedenza;
4.	formalizzazione del risultato ottenuto, riportandolo nello _sprint result_ del relativo _product backlog_.

### 1.1.3 Daily SCRUM

Il _daily SCRUM_ rappresenta un momento in cui il team si riunisce e ogni membro mette al corrente i collaboratori del proprio operato. Il meeting è breve (circa 15-20 minuti) e durante questo tipo di incontro ogni membro del gruppo espone i seguenti punti:

1.	il lavoro svolto nella giornata corrente;
2.	le eventuali problematiche riscontrate durante lo sviluppo, chiedendo consigli agli altri membri del team su come poterle risolvere;
3.	la pianificazione dei successivi compiti che si intende svolgere, a fronte di quanto emerso nei precedenti punti.

Va specificato che questi meetings non sono stati svolti necessariamente tutti in videoconferenza, ma anche via chat.. Inoltre, queste occasioni di discussione, in quanto giornaliere e di breve durata, non sono state documentate.

## 1.2 Divisione dei tasks

All’avvio di ciascuno sprint settimanale, mediante il _product backlog_,  sono stati assegnati a ciascun membro del team una serie di tasks da svolgere.

In particolare, ogni componente del team ha contribuito al progetto realizzando i tasks descritti di seguito.

#### Folin Veronika

<table>
  <thead>
    <td><b>Macro-obiettivo</b></td>
    <td><b>Task</b></td>
  </thead>
  
  <tr>
    <td rowspan="2">Selezionare le piante da coltivare all'interno della serra</td>
    <td>Caricamento delle piante selezionabili in un file Prolog</td>
  </tr>
  <tr>
    <td>Creare interfaccia componente pianta</td>
  </tr>
  
  <tr>
    <td rowspan="2">Avvio della simulazione</td>
    <td>Creare layout schermata di visualizzazione stato globale della serra</td>
  </tr>
  <tr>
    <td>Creare componente Model per gestire l'avvio del tempo</td>
  </tr>
  
  <tr>
    <td rowspan="3">Consentire lo scorrimento fra le diverse pagine dell'applicazione</td>
    <td>Realizzazione architettura MVC tramite cake pattern per la visualizzazoine dello stato globale della serra</td>
  </tr>
  <tr>
    <td>Realizzazione MVC globale della simulazione*</td>
  </tr>
  <tr>
    <td>Realizzazione collegamento fra i diversi MVC*</td>
  </tr>
  
  <tr>
    <td rowspan="2">Visualizzare lo stato aggiornato della simulazione</td>
    <td>Aggiornamento parametri ambientali tramite richieste periodiche*</td>
  </tr>
  <tr>
    <td>Gestione e visualizzazione aggiornamento tempo</td>
  </tr>
  
  <tr>
    <td>Visualizzare lo stato aggiornato delle singole aree</td>
    <td>Gestire interazione e collegamento fra: l'environment e i sensori*</td>
  </tr>
  
   <tr>
    <td rowspan="2">Consentire la gestione di un'area</td>
    <td>Refactor e ottimizzazione views dell'applicazione</td>
  </tr>
  <tr>
    <td>Collegare mvc dettaglio aree all'applicazione*</td>
  </tr>
  
  <tr>
    <td>Miglioramento reattività dell'applicazione</td>
    <td>Gestire tramite reactive programming le richieste per i parametri dell'ambiente</td>
  </tr>
  
  <tr>
    <td rowspan="3">Miglioramento dell'esperienza utente</td>
    <td>Sistemare elementi dell'interfaccia grafica*</td>
  </tr>
  <tr>
    <td>Verifica del coretto funzionamento dell'applicazione tramite test manuali*</td>
  </tr>
  <tr>
    <td>Sostituzione piante prive di descrizione o con descrizione non esaustiva</td>
  </tr>
  
  <tr>
    <td>Guida utente per l'applicazione</td>
    <td>Inserire guida utente nell'applicazione</td>
  </tr>
  
  <tr>
    <td rowspan="3">Documentazione</td>
    <td>Raffinare diagrammi UML*</td>
  </tr>
  <tr>
    <td>Terminare report*</td>
  </tr>
  <tr>
    <td>Scrivere file README</td>
  </tr>
  
  <tr>
    <td rowspan="4">Applicazione finale</td>
    <td>Operazioni finali di refactoring del codice, miglioramento dei test e ottimizzazione degli imports*</td>
  </tr>
  <tr> 
    <td>Realizzare file .jar*</td>
  </tr>
  <tr>
    <td>Valutare coverage finale ottenuta*</td>
  </tr>
  <tr>
    <td>Effettuare prima release dell'applicazione*</td>
  </tr>
  
 </table>
 
#### Mengozzi Maria

<table>
  <thead>
    <td><b>Macro-obiettivo</b></td>
    <td><b>Task</b></td>
  </thead>
  
  <tr>
    <td rowspan="3">Selezionare la città di ubicazione della serra</td>
    <td>Caricamento delle città in un file Prolog</td>
  </tr>
  <tr>
    <td>Creare interfaccia componente città</td>
  </tr>
  <tr>
    <td>Realizzazione meccanismo di salvataggio della città selezionata</td>
  </tr>
  
  <tr>
    <td>Avvio della simulazione</td>
    <td>Realizzare la visualizzazione della suddivisione in aree e del nome delle piante in ciascuna zona della serra</td>
  </tr>
  
  <tr>
    <td rowspan="3">Consentire lo scorrimento fra le diverse pagine dell'applicazione</td>
    <td>Realizzazione architettura MVC tramite cake pattern per la divisione in aree della serra</td>
  </tr>
  <tr>
    <td>Realizzazione MVC globale della simulazione*</td>
  </tr>
  <tr>
    <td>Realizzazione collegamento fra i diversi MVC*</td>
  </tr>
  
  <tr>
    <td rowspan="3">Visualizzare lo stato aggiornato della simulazione</td>
    <td>Aggiornamento parametri ambientali tramite richieste periodiche*</td>
  </tr>
  <tr>
    <td>Definizione interfaccia area e realizzare la sua implementazione</td>
  </tr>
  <tr>
    <td>Gestire il collegamento fra: la serra, le aree e le piante</td>
  </tr>
  
  <tr>
    <td>Visualizzare lo stato aggiornato delle singole aree</td>
    <td>Gestire interazione e collegamento fra: le aree e i sensori*</td>
  </tr>
  
  <tr>
    <td rowspan="3">Consentire la gestione di un'area</td>
    <td>Realizzare model dettaglio aree</td>
  </tr>
  <tr>
    <td>Realizzare controller dettaglio aree*</td>
  </tr>
  <tr>
    <td>Collegare mvc dettaglio aree all'applicazione*</td>
  </tr>
  
  <tr>
    <td rowspan="5">Miglioramento dell'esperienza utente</td>
    <td>Sistemare elementi dell'interfaccia grafica*</td>
  </tr>
  <tr>
    <td>Verificare adattabilità dei diversi componenti alla dimensione dello schermo e al resizing</td>
  </tr>
  <tr>
    <td>Verifica del coretto funzionamento dell'applicazione tramite test manuali*</td>
  </tr>
  <tr>
    <td>Miglioramento stile aree nella schermata principale</td>
  </tr>
  <tr>
    <td>Refactor gestione stati aree e sensori</td>
  </tr>
  
  <tr>
    <td>Guida utente per l'applicazione</td>
    <td>Scrivere guida utente per l'applicazione*</td>
  </tr>
  
  <tr>
    <td rowspan="2">Documentazione</td>
    <td>Raffinare diagrammi UML*</td>
  </tr>
  <tr>
    <td>Terminare report*</td>
  </tr>
  
  <tr>
    <td rowspan="4">Applicazione finale</td>
    <td>Operazioni finali di refactoring del codice, miglioramento dei test e ottimizzazione degli imports*</td>
  </tr>
  <tr> 
    <td>Realizzare file .jar*</td>
  </tr>
  <tr>
    <td>Valutare coverage finale ottenuta*</td>
  </tr>
  <tr>
    <td>Effettuare prima release dell'applicazione*</td>
  </tr>
  
 </table>

#### Vitali Anna

<table>
  <thead>
    <td><b>Macro-obiettivo</b></td>
    <td><b>Task</b></td>
  </thead>
  
  <tr>
    <td rowspan="2">Selezionare le piante da coltivare all'interno della serra</td>
    <td>Creare layout schermata di selezione delle piante</td>
  </tr>
  <tr>
    <td>Realizzazione meccanismo di salvataggio delle piante selezionate</td>
  </tr>
  
  <tr>
    <td rowspan="2">Avvio della simulazione</td>
    <td>Creare componente Controller gestire l'avvio del tempo</td>
  </tr>
  <tr>
    <td>Creare layout schermata di fine simulazione</td>
  </tr>
 
  <tr>
    <td rowspan="3">Consentire lo scorrimento fra le diverse pagine dell'applicazione</td>
    <td>Realizzazione architettura MVC tramite cake pattern per la selezione delle piante</td>
  </tr>
  <tr>
    <td>Realizzazione MVC globale della simulazione*</td>
  </tr>
  <tr>
    <td>Realizzazione collegamento fra i diversi MVC*</td>
  </tr>
  
 <tr>
    <td rowspan="6">Visualizzare lo stato aggiornato delle singole aree</td>
    <td>Impostazione formula di aggiornamento dei valori dei sensori*</td>
  </tr>
  <tr>
    <td>Definizione interfaccia sensori*</td>
  </tr>
  <tr>
    <td>Implementazione sensore luminosità</td>
  </tr>
  <tr>
    <td>Implementazione sensore temperatura</td>
  </tr>
  <tr>
    <td>Gestire interazione e collegamento fra: le aree e i sensori*</td>
  </tr>
  <tr>
    <td>Gestire interazione e collegamento fra: l'environment e i sensori*</td>
  </tr>
  
  <tr>
    <td rowspan="2">Consentire la gestione di un'area</td>
    <td>Refactor e ottimizzazione controllers dell'applicazione</td>
  </tr>
  <tr>
    <td>Collegare mvc dettaglio aree all'applicazione*</td>
  </tr>
  
  <tr>
    <td>Miglioramento reattività dell'applicazione</td>
    <td>Gestire tramite reactive programming le richieste per i parametri delle piante</td>
  </tr>
  
  <tr>
    <td rowspan="4">Miglioramento dell'esperienza utente</td>
    <td>Sistemare elementi dell'interfaccia grafica*</td>
  </tr>
  <tr>
    <td>Verifica del coretto funzionamento dell'applicazione tramite test manuali*</td>
  </tr>
  <tr>
    <td>Miglioramento stile schermata selezione delle piante</td>
  </tr>
  <tr>
    <td>Refactor registrazione callbacks timer*</td>
  </tr>
  
  <tr>
    <td>Guida utente per l'applicazione</td>
    <td>Scrivere guida utente per l'applicazione*</td>
  </tr>
  
  <tr>
    <td rowspan="2">Documentazione</td>
    <td>Raffinare diagrammi UML*</td>
  </tr>
  <tr>
    <td>Terminare report*</td>
  </tr>
  
  <tr>
    <td rowspan="4">Applicazione finale</td>
    <td>Operazioni finali di refactoring del codice, miglioramento dei test e ottimizzazione degli imports*</td>
  </tr>
  <tr> 
    <td>Realizzare file .jar*</td>
  </tr>
  <tr>
    <td>Valutare coverage finale ottenuta*</td>
  </tr>
  <tr>
    <td>Effettuare prima release dell'applicazione*</td>
  </tr>
  
 </table>

#### Yan Elena

<table>
  <thead>
    <td><b>Macro-obiettivo</b></td>
    <td><b>Task</b></td>
  </thead>
  
  <tr>
    <td rowspan="2">Selezionare la città di ubicazione della serra</td>
    <td>Creare layout schermata di selezione della città</td>
  </tr>
  <tr>
    <td>Realizzazione live search delle città</td>
  </tr>
  
  <tr>
    <td rowspan="2">Avvio della simulazione</td>
    <td>Gestire l'aggiornamento dello scorrere del tempo</td>
  </tr>
  <tr>
    <td>Gestire la modifica della velocità dello scorre del tempo</td>
  </tr>
  
  <tr>
    <td rowspan="4">Consentire lo scorrimento fra le diverse pagine dell'applicazione</td>
    <td>Refactoring schermata BaseView, con aggiunta pulsanti per la navigazione</td>
  </tr>
  <tr>
    <td>Realizzazione architettura MVC tramite cake pattern per la selezione della città</td>
  </tr>
  <tr>
    <td>Realizzazione MVC globale della simulazione*</td>
  </tr>
  <tr>
    <td>Realizzazione collegamento fra i diversi MVC*</td>
  </tr>
  
  <tr>
    <td rowspan="6">Visualizzare lo stato aggiornato delle singole aree</td>
    <td>Impostazione formula di aggiornamento dei valori dei sensori*</td>
  </tr>
  <tr>
    <td>Definizione interfaccia sensori*</td>
  </tr>
  <tr>
    <td>Implementazione sensore umidità dell'aria</td>
  </tr>
  <tr>
    <td>Implementazione sensore umidità del terreno</td>
  </tr>
  <tr>
    <td>Gestire interazione e collegamento fra: le aree e i sensori*</td>
  </tr>
  <tr>
    <td>Gestire interazione e collegamento fra: l'environment e i sensori*</td>
  </tr>
  
  <tr>
    <td rowspan="3">Consentire la gestione di un'area</td>
    <td>Realizzare controller dettaglio aree*</td>
  </tr>
  <tr>
    <td>Realizzare view dettaglio aree</td>
  </tr>
  <tr>
    <td>Collegare mvc dettaglio aree all'applicazione*</td>
  </tr>
  
  <tr>
    <td rowspan="6">Miglioramento dell'esperienza utente</td>
    <td>Sistemare stile pulsanti delle diverse schermate</td>
  </tr>
  <tr>
    <td>Sistemare elementi dell'interfaccia grafica*</td>
  </tr>
  <tr>
    <td>Verifica del coretto funzionamento dell'applicazione tramite test manuali*</td>
  </tr>
  <tr>
    <td>Aggiunta icona applicazione</td>
  </tr>
  <tr>
    <td>Refactor registrazione callbacks timer*</td>
  </tr>
  <tr>
    <td>Refactor gestione stati pulsanti della schermata dettaglio area</td>
  </tr>
  
  <tr>
    <td rowspan="2">Documentazione</td>
    <td>Raffinare diagrammi UML*</td>
  </tr>
  <tr>
    <td>Terminare report*</td>
  </tr>
  
  <tr>
    <td rowspan="4">Applicazione finale</td>
    <td>Operazioni finali di refactoring del codice, miglioramento dei test e ottimizzazione degli imports*</td>
  </tr>
  <tr> 
    <td>Realizzare file .jar*</td>
  </tr>
  <tr>
    <td>Valutare coverage finale ottenuta*</td>
  </tr>
  <tr>
    <td>Effettuare prima release dell'applicazione*</td>
  </tr>
  
</table>

\* questi task sono stati completati a seguito di una collaborazione fra più componenti del gruppo (vedere i diversi _product backlog_ relativi al progetto per maggiori dettagli).

### 1.2.1 Aggregazione dei risultati

Il progetto è stato realizzato attraverso l'utilizzo di un _repository_ GitHub.

In questo modo, dopo che ad ogni membro del gruppo sono stati affidati i diversi compiti da svolgere, è possibile procedere all'esecuzione dei tasks in modo indipendente, aprendo un proprio _branch_ di lavoro sul _repository_ del progetto.

Al termine dello sprint, i diversi _branches_ vengono chiusi e uniti, tramite l'operazione di `merge`, al _branch_ di sviluppo principale (`develop`). 

Tuttavia, nel caso in cui il completamente del lavoro di uno dipenda dall'esecuzione di quello di altri, i membri coinvolti collaborano al fine di unire i diversi _branches_ o ne creano di nuovi da cui partire, in modo da proseguire e completare i tasks successivi.

Una volta che tutti i lavori per il progetto sono stati terminati e si è quindi pronti per la prima _release_ dell'aplicazione, si provvederà all'unione del _branch_ `develop` con il _branch_ `master`, che contiene le versioni rilasciate del progetto.

### 1.2.2 Revisione dei task

Al termine di ogni sprint, si procede alla revisione del lavoro svolto  durante la settimana. In particolare, si verifica la realizzazione dei tasks affidati a ogni membro del gruppo, analizzandone la loro completezza in base alla _definition of done_ stabilita all'inizio del progetto.

Se durante l'incontro ci si rende conto di alcuni aspetti del lavoro svolto che potrebbero essere migliorati o di incompletezze nel risultato ottenuto, si richiede al rispettivo membro responsabile di correggere o completare i lavori che gli sono stati affidati,  prima di proseguire con il successivo sprint.

Infine, a seguito dell'incontro di _sprint review_, si può decidere di effettuare il _refactoring_ di elementi già realizzati, dando luogo a nuovi tasks che dovranno essere realizzati nello sprint successivo.

## 1.3 Strumenti utilizzati

Per la realizzazione del progetto sono stati utilizzati differenti _tool_ a supporto del processo di sviluppo. Tali strumenti hanno come obiettivo quello di agevolare gli sviluppatori durante tutta la realizzazone del progetto, cercando di automatizzarne i diversi aspetti.

Pertanto, gli strumenti impiegati per il progetto sono riportati di seguito:

-	**SBT**, per la _build automation_;
-	**Wartremover**, per valutare la qualità del codice prodotto, individuando possibili pattern problematici;
-	**Scalastyle**, per formattare correttamente il codice in conformità alle convenzioni di _Scala_;
-	**ScalaTest**, per la scrittura ed esecuzione di test automatizzati sul codice di produzione;
-	**UnitTest**, per la verifica delle funzionalità della _UI_;
-	**GitHub**, come servizio di hosting per il codice sorgente e i files utilizzati durante il processo di sviluppo;
-	**GitHub Actions**, per promuovere la _continuous integration_;
-	**Trello**, come tool collaborativo per la gestione degli sprint tasks.