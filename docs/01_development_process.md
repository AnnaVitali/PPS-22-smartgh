# 1. Processo di sviluppo

Per il porcesso di sviluppo, il team di lavoro, ha deciso di utilizzare un apporcio _SCRUM-inspired_. In particolare,  [_Scrum_](https://www.scrum.org/) è un _framework_ di sviluppo software: agile, iterativo e incrementale, che consente la gestione dello sviluppo del prodotto.

Specificatamente, il porcesso SCRUM-inspired seguito, prevede di:
1. nominare uno studente (ad esempio, chi ha l'idea del progetto), il quale fungerà da sorta di committente o esperto del dominio, il quale cercherà di garantire l'usabilità/qualità del risultato;
2. designare uno studente (ad esempio, chi pensa di avere doti di coordinamento) che ricoprirà il ruolo di _product owner_;
3. effettuare un meeting iniziale, in cui redigere un _product backlog_ e definire un primo sprint organizzativo;
4. utilizzare sprint corti, da 15-20 ore di lavoro, con l'obiettivo di ottenere, ad ogni sprint, dei risultati "tangibili", con già un valore per gli _stakeholder_;
5. fare meeting frequenti e a inizio/fine sprint (con brevissimo report del risultato, anch'esso da tenere in versione), realizzando anche uno _sprint backlog_ per mantenere traccia dell'organizzazione dei lavori.

## 1.1 Meetings

I meetings sono stati realizzati definendo, inizialmente, un ordine del giorno redatto principalmente dal _product owner_, a cui anche gli altri membri del gruppo possono contribuire, suggerendo nuovi aspetti da discutere, o modifiche rispetto agli argomenti di discussione individuati.

Sono stati svolti incontri brevi e frequenti, pressoché quotidiani, in modo da mantenere aggiornati i diversi membri del gruppo, sullo stato di avanzamento del progetto.

Per quanto riguarda gli Sprint, come specificato precedentemente, essi sono stati svolti settimanalmente, con incontri di inizio e di fine. In particolare, durante l’incontro di inizio Sprint, viene definito il _product backlog_ relativo, mentre durante quello di fine Sprint, vengono discussi i risultati ottenuti e revisionato il lavoro che è stato svolto.

I diversi meetings, sono stati tenuti in modalità smart-working, attraverso l’utilizzo di piattaforme per videoconferenze.

Ogni meeting con il rispettivo ordine del giorno, è stato tracciato in un apposito file di riferimento, che è possibile trovare fra gli artefatti del progetto.

### 1.1.1 Sprint planning

Lo _Sprint planning_, è un meeting in cui il team pianifica il lavoro che deve essere svolto e portato a termine durante lo Sprint, esso prevede di individuare i diversi elementi del _product backlog_ e i differenti _sprint tasks_ che devono essere effettuati per poter raggiungere gli obiettivi prefissati. Di conseguenza, durante i diversi _sprint planning_ sono stati discussi i seguenti punti:

1.	definizione dell’obiettivo principale dello Sprint;
2.	stato di sviluppo da raggiungere, alla fine dello Sprint, tramite il raffinamento del product backlog;
3.	definizione dei diversi tasks da svolgere, per il raggiungimento degli obiettivi preposti;
4.	assegnazione dei task ai diversi membri del gruppo.

### 1.1.2 Sprint review

Lo _Sprint review_, è un meeting che ha luogo alla fine dello Sprint, avente l’obiettivo di revisionare e valutare il lavoro svolto dal team di sviluppo. Pertanto, durante i differenti _sprint review_ sono stati discussi i seguenti punti:

1.	Analisi dell’incremento ottenuto, valutando il lavoro che è stato svolto;
2.	Aggiornamento e adattamento del product backlog;
3.	Valutazione di eventuali ritardi rispetto ai tempi definiti in precedenza;
4.	Formalizzazione del risultato ottenuto, riportandolo nello _sprint result_ del relativo _product backlog_.


//TODO Pensare se inserire anche Sprint Review (che dice sostanzialmente valutazione miglioramenti  per prossimo sprint)

### 1.1.3 Daily SCRUM

Il _Daily SCRUM_, rappresenta un momento in cui il team si riunisce e ogni membro, mette al corrente i collaboratori del proprio operato. Il meeting è breve circa 15-20 minuti e durante questi incontri ogni membro del gruppo ha esposto i seguenti punti:

1.	Il lavoro svolto nella giornata corrente,
2.	Le eventuali problematiche riscontrate durante lo sviluppo, chiedendo consigli agli altri membri del team, su come poterle risolvere,
3.	La pianificazione dei successivi compiti che si intende svolgere, a fronte di quanto emerso dai precedenti punti.

Va specificato, che questi meetings non necessariamente, sono stati svolti tutti in videoconferenza, ma anche tramite una discussione via chat tra i diversi membri del gruppo. Inoltre, questi incontri, inquanto giornalieri e di breve durata, non sono stati registrati nel file di riferimento, dove invece si è tenuto traccia degli sprint planning e degli sprint review.

## 1.2 Divisione dei tasks

All’avvio di ciascuno Sprint settimanale, mediante il _product backlog_ sono stati assegnati, a ciascun membro del team, una serie di tasks da svolgere durante lo Sprint.

Nel momento successivo alla suddivisione dei tasks fra i diversi membri del gruppo, ogni sviluppatore ha la possibilità di iniziare a svolgere i diversi lavori che li sono stati affidati, nell’ ordine che ritiene più opportuno. In particolare, ogni componente del team ha contribuito al progetto realizzando i tasks descritti di seguito.

#### Folin Veronika
//TODO
#### Mengozzi Maria
//TODO
#### Vitali Anna
//TODO
#### Yan Elena
//TODO

### 1.2.1 Aggregazione dei risultati

Il progetto è stato realizzato attraverso l'utilizzo di un repository GitHub.

Dopo che ad ogni membro del gruppo, sono stati affidati i diversi compiti da svolgere per lo sprint corrente, a seguito dell'incontro di sprint preview, è possibile per ogni  sviluppatore procedere nell'esecuzione dei tasks che li sono stati assegnati, in modo indipendente rispetto agli altri, aprendo un proprio branch di lavoro sul repository del progetto.

Al termine dello sprint, i diversi branch su cui sono stati svolti i diversi lavori vengono chiusi e uniti tramite l'operazione di merge, al branch di sviluppo principale (`develop`). Tuttavia, nel caso in cui alcuni lavori che devono essere svolti, dipendano da altri, i diversi membri del gruppo coinvolti nella loro realizzazione, collaborano insieme per poter unire i diversi branch di lavoro realizzati, o per poterne creare di nuovi da cui partire, in modo da proseguire e completare i tasks successivi.

Una volta che tutti i lavori per il progetto sono stati terminati e si è quindi pronti per la prima relese dell'aplicazione, si provvederà all'unione del branch `develop`, contenente tutti i risultati raggiunti fino a questo momento, con il branch `master`, che invece contiene le diverse versioni aggiornate e rilasciate del progetto realizzato.


### 1.2.2 Revisione dei task

Al termine di ogni sprint e durante l'incontro di sprint review, si procede nella revisione del lavoro che è stato eseguito durante la settimana. In particolare, si verifica la realizzazione dei diversi tasks affidati a ogni membro del gruppo, analizzandone la loro completezza, in base alla _Definition of done_ stabilita all'inizio del progetto.

Se durante l'incontro, ci si rende conto che alcuni aspetti del lavoro svolto potrebbero essere migliorati o di incompletezze nel risultato ottenuto, si richiede al membro del gruppo responsabile, di correggere o completare i lavori che li sono stati affidati,  prima di proseguire con i lavori del prossimo sprint, in base agli elementi emersi durante la revisione.

Infine, a seguito dell'incontro di sprint review, si può decidere di effettuare il _refactoring_ di elementi già realizzati, dando luogo a nuovi tasks che dovranno essere realizzati nello sprint successivo.

## 1.3 Strumenti utilizzati

Per la realizzazione del progetto, sono stati utilizzati differenti _tool_ a supporto del processo di sviluppo. Tali strumenti, hanno come obiettivo quello di agevolare gli sviluppatori, durante tutta la realizzazone del progetto, cercando di automatizzare i diversi aspetti di questo, che possono essere svolti automaticamente.

Pertanto, gli strumenti impiegati per il progetto, sono di seguito riportati:

-	**SBT**,  per la build automation;
-	**Wartremover**, per valutare la qualità del codice prodotto, individuando possibili pattern problematici;
-	**Scalastyle**, per formattare correttamente il codice in conformità alle convenzioni di Scala;
-	**ScalaTest**, per la scrittura ed esecuzione dei test automatizzati sul codice di produzione;
-	**UnitTest**, per la verifica delle funzionalità della UI;
-	**GitHub**, come servizio di hosting per il codice sorgente e i files utilizzati, durante il processo di sviluppo;
-	**GitHub Actions**, per promuovere la continuous integration;
-	**Trello**, come tool collaborativo per la gestione degli sprint tasks.