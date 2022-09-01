# 2. Requisiti

In questa sezione verranno discussi i requisiti dell'applicazione che verrà realizzata, a partire da quelli di business fino a quelli funzionali e non funzionali.

I requisiti verranno numerati in modo tale che, nel seguito, potranno essere ricondotti alle rispettive sottosezioni.

## 2.1 Requisiti di business
I requisiti di business previsti dall'applicazione sono:

1. simulare una serra intelligente in cui sono posti dei sensori per il monitoraggio delle coltivazioni;
2. ricevere una segnalazione ogni qual volta venga rilevato un valore critico;
3. effettuare azioni correttive al fine di ripristinare i valori ottimali.

## 2.2 Requisiti utente
Il committente ha richiesto un sistema che rispetti la seguente descrizione:

> Sono un'insegnante di "_Tecniche delle produzioni vegetali e zootecniche_" dell'istituto agrario di Cesena. Nell'ultimo periodo, per via della pandemia, gli studenti non hanno avuto la possibilità di sperimentare la gestione di una serra, argomento previsto dal corso. Vorrei, quindi, che gli studenti avessero la possibilità, attraverso un simulatore, di provare questa esperienza. 
> 
> La serra è una struttura divisa in aree adibite alla coltivazione e alla conservazione di specie vegetali bisognose di particolari condizioni ambientali. In ogni area dell'impianto è possibile coltivare una sola tipologia di piantagione che viene solitamente monitorata da dei sensori che rilevano 4 parametri essenziali per la loro crescita: luminosità e temperatura ambientale, umidità del suolo e dell'aria. 
> 
> Mi piacerebbe che ogni studente potesse personalizzare la propria serra scegliendo la località in cui verrà virtualmente situata, il numero di aree in cui sarà suddivisa e le tipologie di piante da coltivare in quest'ultime.
>
> Un buono stato di salute delle coltivazioni può essere ottenuto rispettando specifici range di valori; questi ultimi possono essere influenzati sia dalle condizioni ambientali all'interno della serra che dalle condizioni metereologiche. I parametri ottimali dovranno essere visualizzabili dagli studenti, in modo tale che abbiano un riferimento teorico durante la simulazione.
>
> Vorrei quindi un'applicazione che simuli lo stato globale della serra nell'arco di una giornata, mostrando in particolare i valori rilevati nelle singole aree ed eventuali situazioni critiche per le coltivazioni all'interno di esse, segnalate mediante un avviso. Inoltre, al verificarsi di quest'ultima situazione, lo studente dovrà essere opportunamente guidato nelle azioni correttive.
>
> Tali criticità potranno essere gestite dallo studente mediante uno o più dei seguenti metodi:
> - regolando l'intensità luminosa delle lampade o schermando l'area, in relazione ad una scarsa o eccessiva luminosità; 
> - isolando l'area interessata e impostando la temperatura interna della stessa;
> - annaffiando o smuovendo il terreno, rispettivamente in caso di bassa o alta umidità del suolo;
> - nebulizzando o ventilando l'ambiente, rispettivamente in caso di bassa o alta umidità ambientale.

Chi farà uso dell'applicazione potrà quindi:

1. impostare i seguenti parametri della simulazione:
	1. la località di ubicazione della serra
	2. il numero di aree
	3. la tipologia di pianta da coltivare all'interno di ogni area
	
2. visualizzare le variazioni ambientali nell'arco della giornata, relative a:
	1. luminosità
	2. temperatura
	3. umidità
	4. condizioni metereologiche (es: soleggiato, nuvoloso, ecc...)

3. osservare lo stato globale della serra, in particolare:
	1. la suddivisione in aree
	2. il nome della pianta coltivata per ogni rispettiva area 
	3. lo stato dell'area, nello specifico se risulta essere o meno in allarme

4. osservare lo stato all'interno di una specifica area, includendo:
	1. le informazioni riguardanti la coltivazione
		1. immagine
		2. nome
		3. descrizione
		4. parametri ottimali di riferimento
	2. i parametri rilevati dai sensori
	3. un'indicazione dello stato attuale dell'area, se risulta essere o meno in allarme

5. compiere operazioni per la cura ordinaria delle coltivazioni all'interno delle singole aree, le quali potranno essere messe in atto anche per far fronte alle diverse situazioni di allarme, in particolare:
	1. regolare l'intensità luminosa delle lampade
	2. schermare l'area
	3. isolare l'area
	4. impostare la temperatura interna all'area
	5. annaffiare il terreno
	6. smuovere il terreno
	7. nebulizzare l'ambiente
	8. ventilare l'ambiente

6. in caso di allarme, visualizzare i suggerimenti rispetto alle azioni da intraprendere per ristabilire lo stato dell'area

## 2.3 Requisiti funzionali

Sono stati identificati i seguenti requisiti funzionali:

1. la personalizzazione della simulazione, permettendo all'utente di specificare:
	1. la località 
	2. le tipologie di piante da coltivare, scegliendo tra quelle disponibili e visualizzando quelle selezionate
	
2. all'avvio della simulazione da parte dell'utente, il sistema dovrà essere in grado di:
	1. richiedere le previsioni metereologiche previste per il giorno stesso
	2. dedurre il numero di aree in base alla quantità di specie scelte e, di conseguenza, disegnarne la struttura della serra

3. l'osservazione dello stato di esecuzione della simulazione:
	1. visualizzando lo scorrere del tempo e permettendo all'utente di impostarne la velocità

4. l'osservazione dello stato globale della serra, visualizzando:
	1. le condizioni ambientali che verranno aggiornate durante tutta l'esecuzione in base alle previsioni metereologiche
	2. la suddivisione in aree
	3. il nome della pianta presente in ogni area
	4. i parametri rilevati dai sensori all'interno delle singole aree
	5. l'eventuale stato di allarme di un'area

5. alla richiesta di accesso ad una specifica area, mostrare:
	1. lo scorrere del tempo
	2. le informazioni relative alla coltivazione (immagine, nome, descrizione)
	3. lo stato attuale 
	4. i valori ottimali per ogni parametro
	5. i valori attuali per ogni grandezza misurata, evidenziando quando questi non rientrano nel range ottimale
	6. le possibili azioni che possono essere intraprese in caso di allarme, associandole ai parametri che influenzano, come:
		1. aprire e chiudere la schermatura dell'area
		2. regolare l'intensità luminosa delle lampade, una volta schermata l'area
		3. isolare termicamente la struttura
		4. regolare la temperatura, una volta isolata l'area
		5. nebulizzare l'ambiente
		6. ventilare l'ambiente
		7. smuovere il terreno 
		8. innaffiare

6. visualizzare in tempo reale l'aggiornamento dei valori a seguito di:
	1. modifiche delle condizioni metereologiche
	2. azioni intraprese dall'utente

7. la possibilità di terminare la simulazione in qualsiasi momento e di avviarne una nuova

## 2.4 Requisiti non funzionali
Sono stati identificati i seguenti requisiti non funzionali:
- l'applicazione deve essere _cross-platform_, cioè eseguibile sia su Windows che su MacOS e Linux, o comunque su qualsiasi sistema operativo capace di supportare _Java Runtime Environment_ versione 16 e successive;
- l'interfaccia deve essere reattiva alle azioni dell'utente;
- l'applicazione non deve mai interrompersi qualora si verifichi un errore ma deve, invece, mostrare un messaggio di errore all'utente;
- l'applicazione deve essere sufficientemente modulare in modo tale che sia possibile riutilizzare i suoi componenti in contesti diversi.
- il sistema deve essere capace di scalare a seguito dell'aggiunta di nuovi componenti (es: aggiunta di nuovi sensori).

## 2.5 Requisiti implementativi
Di seguito vengono riportati i requisiti relativi all'implementazione del sistema:
- il sistema sarà sviluppato in _Scala 3_ e per eventuali feature sarà possibile integrare delle teorie _Prolog_;
- il sistema farà riferimento a _JDK 11_ ed eventuali librerie esterne utilizzate dovranno supportare, almeno, tale versione;
- il testing del sistema sarà effettuato utilizzando _ScalaTest_ e, per i componenti che rappresentano la View, _JUnit_.