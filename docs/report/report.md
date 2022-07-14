# Requisiti

In questa sezione verranno trattati i requisiti dell'applicazione che verrà realizzata. A partire dai requisiti di business fino ai requisiti funzionali e non funzionali.

I requisiti verranno numerati in modo tale che nel seguito potranno essere ricondotti alle rispettive sottosezioni.

## Requisiti di business
I requisiti di business previsiti dall'applicazione sono:

1. simulare una serra intelligente in cui sono posti dei sensori per il monitoraggio delle coltivazioni;
2. ricevere una segnalazione al verificarsi di rilevazioni di valori critici;
3. effettuare azioni correttive al fine di ripristinare i valori ottimali.

## Requisiti utente
Il committente ha richiesto un sistema che rispetti la seguente descrizione:

> "Sono un'insegnante di "Tecniche delle produzioni vegetali e zootecniche" dell'istituto agrario di Cesena. Nell'ultimo periodo, per via della pandemia, gli studenti non hanno avuto la possibilità di sperimentare la gestione di una serra, argomento previsto dal corso. Vorrei, quindi, che gli studenti avessero la possibilità, attraverso un simulatore, di provare questa esperienza. 
> 
> La serra è una struttura divisa in aree adibit1 alla coltivazione e alla conservazione di specie vegetali bisognose di particolari condizioni ambientali. In ogni area dell'impianto è possibile coltivare una sola tipologia di piantagione che viene solitamente monitorata da dei sensori che rilevano 4 parametri essenziali per la loro crescita: luminosità e temperatura ambientale, umidità del suolo e dell'aria. 
> 
> Mi piacerebbe che ogni studente potesse personalizzare la propria serra scegliendo la località in cui verrà virtualmente situata, il numero di aree in cui sarà suddivisa e la tipologia di pianta da coltivare in quest'ultime.
>
> Un buono stato di salute delle coltivazioni può essere ottenuto rispettando specifici range di valori: questi ultimi possono essere influenzati sia dalle condizioni ambientali che da quelle metereologiche. I parametri ottimali dovranno essere visualizzabili dagli studenti, in modo tale che abbiano un riferimento teorico durante la simulazione.
>
> Vorrei quindi un'applicazione che simuli lo stato globale della serra nell'arco di una giornata, mostrando in particolare i valori rilevati nelle singole aree ed eventuali situazioni critiche per le coltivazioni all'interno di esse, segnalate mediante un avviso. Inoltre, al verificarsi di quest'ultima situazione, lo studente dovrà essere opportunamente guidato nelle azioni correttive.
>
> Tali criticità potranno essere gestite dallo studente mediante uno o più dei seguenti metodi:
> - regolando l'intensità luminosa delle lampade o schermando l'area, in relazione ad una scarsa o eccessiva luminosità; 
> - isolando l'area interessata e impostando la temperatura interna della stessa;
> - annaffiando o smuovendo il terreno, rispettivamente in caso di bassa o alta umidità del suolo;
> - nebulizzando o ventilando l'ambiente, rispettivamente in caso di bassa o alta umidità ambientale."

Chi farà uso dell'applicazione potrà quindi:

1. impostare i seguenti parametri della simulazione:
	1. la località di ubicazione della serra
	2. il numero di aree
	3. per ogni area, la tipologia di pianta da coltivare all'interno di essa
	
2. visualizzare le variazioni ambientali nell'arco della giornata, relative a:
	1. luminosità
	2. temperatura
	3. umidità

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
	3. indicazione dello stato attuale dell'area, se risulta essere o meno in allarme

5. compiere operazioni per la cura ordinaria delle coltivazioni all'interno delle singole aree, le quali potranno essere messe in atto anche per far fronte alle diverse situazioni di allarme, in particolare:
	1. regolare l'intensità luminosa delle lampade
	2. schermare l'area
	3. isolare l'area
	4. impostare la temperatura interna all'area
	5. annaffiare il terreno
	6. smuovere il terreno
	7. nebulizzare l'ambiente
	8. ventilare l'ambiente

6. in caso di allarme, visualizzare i suggerimenti rispetto alle azioni da compiere per ristabilire lo stato dell'area