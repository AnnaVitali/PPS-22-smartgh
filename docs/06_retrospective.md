# 6. Retrospettiva
Come descritto nel precedente capitolo [Ch. 1](#1-processo-di-sviluppo), per realizzare il progetto è stato utilizzato un approcio _SCRUM-inspired_, pertanto il team ha provveduto ad effettuare degli Sprint settimanali, con l'obiettivo di realizzare diversi _product backlog_ che potessero fornire un risultato tangibile per l'utente finale.

Prima di definire i diversi _product backlog_ ed effettuare gli sprint, il team di lavoro ha concordato sulla _"definition of done"_ per svolgere i diversi tasks; sostanzialmente i membri del gruppo hanno stabilito il criterio che deve essere rispettato, secondo il quale un prodotto realizzato possa effettivamente definirsi concluso. In particolare, un prodotto si ritiene completato quando tutti i tasks individuati per poterlo realizzare sono stati portati a termine e il codice di implementazione è stato adeguatamente testato con esito positivo ed opportunamente documentato tramite la _Scala doc_.

Nelle seguenti sezioni verranno riassunte le diverse iterazioni svolte dal team di sviluppo, considerando una fase iniziale di avviamento, in cui si è provveduto a definire i requisiti del progetto e ad impostare l'ambiente di lavoro e le successive fasi, corrispondenti ai diversi sprint effettuati.

Molte delle informazioni che varranno riportate, è possibile ritrovarle anche all'interno dei files relativi al porcesso di sviluppo del progetto. Infatti, gli Sprint che sono stati effettuati, i _prduct backlog_ e i tasks eseguiti per essi, sono stati opportunamente documentati ed i files ad essi relativi si trovano all'interno della directory `/process` del progetto.

## 6.1 Avviamento
In questa prima fase del processo di sviluppo del progetto, i membri del gruppo hanno provveduto a definire i requisiti dell'applicativo che si vuole realizzare e che dovranno essere rispettati.

In particolare, sono stati definiti i diversi requisiti: di business, utente, funzionali, non funzionali e implementativi che l'applicazione deve avere. Tali requisiti sono stati appositamente numerati, in modo tale che all'interno della documentazione si potesse avere un chiaro riferimento ad essi.

Successivamente, i diversi membri del gruppo una volta definiti i requisiti, hanno impostato l'ambiente di lavoro per il progetto, creando le diverse directories per contenere i diversi files e i _mockup_ delle diverse schermate, in modo da avere un riferimento, seppur minimale, del risultato che si vuole ottenere e infine hanno impostato i diversi flussi di lavoro per il processo di _Continuous Integration_.

Va inoltre sottolineato, che per la gestione del _repository_ è stata utilizzata la metodologia _Git flow_, pertanto, in questa fase, sono anche stati creati il branch principale `master` e il branch di sviluppo `develop`.

## 6.2 Sprint

### 6.2.1 Sprint 1 - 25/07/2022
L'obiettivo di questo sprint, consiste nel realizzare le funzionalità di base per poter impostare l'ambiente della simulazione, pertanto i _product backlog_ che sono stati individuati sono i seguenti:

- selezionare la città di ubicazione della serra;
- selezionare le piante da coltivare all'interno della serra;
- avvio della simulazione.

Al termine di questo sprint, l'utente ha la possibilita di visualizzare e interagire con le diverse schermate dell'applicazione singolarmente ed ha inoltre la possibilità, di lanciare l'avvio della simulazione tramite dei parametri di default, potendo osservare lo scorrere del tempo e modificarne la velocità d'esecuzione.

Infine, alla conclusione dello sprint, sommando i diversi costi per i lavori che sono stati svolti, si ottiene che il costo totale, per il lavoro richiesto nel realizzare i diversi _product backlog_, è di 52.

### 6.2.2 Sprint 2 - 02/08/2022
L'obiettivo di questo sprint, consiste nel dare la possibilità all'utente di visualizzare lo stato della simulazione. In particolare, per raggiungere questo scopo occorre gestire i seguenti componenti: le aree della serra, i sensori, i valori rilevati dai sensori delle diverse aree, i parametri ambientali e il tempo virtuale della simulazione.

Pertanto, i _product backlog_ che sono stati individuati sono i seguenti:

- consentire lo scorrimento fra le diverse pagine dell'applicazione;
- visualizzare lo stato aggiornato della simulazione;
- visualizzare lo stato aggiornato delle singole aree.

Al termine di questo sprint, è possibile interagire con le schermate principali che riguardano la personalizzazione dei parametri scelti dall'utente e la visualizzazione dell'andamento della simulazione. Quest'ultima consiste nell'aggiornamento periodico (diretto dal tempo virtuale) dei parametri rilevati dai sensori nelle diverse aree, sulla base di quelli ottenuti rispetto alla località selezionata.

Infine, alla conclusione dello sprint, sommando i diversi costi per i lavori che sono stati svolti, si ottiene che il costo totale, per il lavoro richiesto nel realizzare i diversi _product backlog_, è di 68.

### 6.2.3 Sprint 3 - 10/08/2022
L'obiettivo di questo sprint, consiste nel dare la possibilità all'utente di poter visualizzare il dettaglio dello stato di un'area e di poter intraprendere operazioni correttive, necessarie al fine di riportare i parametri in allarme all'interno del range ottimale per la pianta. Inoltre, si vuole migliorare l'esperienza utente fornita fino a questo momento, migliorando la reattività e l'aspetto dell'interfaccia grafica. 

I _product backlog_ che sono stati individuati per questo sprint sono i seguenti:

- consentire la gestione di un area;
- miglioramento reattività dell'applicazione;
- miglioramento dell'esperienza utente;
- guida utente per l'applicazione.

Al termine dello sprint, l'utente è in grado di visualizzare il dettaglio di un area potendo gestire situazioni di allarme, accedere a una guida utente per poter meglio comprendere il funzionamento dell'applicazione e interagire con un'interfaccia grafica maggiormante reattiva e stilisticamente migliorata rispetto alla precedente.

Infine, alla conclusione dello sprint, sommando i diversi costi per i lavori che sono stati svolti, si ottiene che il costo totale, per il lavoro richiesto nel realizzare i diversi _product backlog_, è di 46.

### 6.2.4 Sprint 4 - 21/08/2022
L'obiettivo di questo sprint, consiste nella revisione di quanto prodotto fino ad ora e nella consegna dell'applicazione ultimata all'utente.

Pertanto, i _product backlog_ che sono stati individuati sono i seguenti:

- miglioramento dell'esperienza utente;
- documentazione;
- applicazione finale.

Al termine di questo sprint, il team di svilippo consegna l'applicazione finale ottimizzata rispetto all'esperienza utente e alla qualità del codice. Inoltre, viene consegnato il report che documenta le fasi di sviluppo del progetto.

Infine, alla conclusione dello sprint, sommando i diversi costi per i lavori che sono stati svolti, si ottiene che il costo totale, per il lavoro richiesto nel realizzare i diversi _product backlog_, è di 41.

## 6.3 Commenti finali

Il gruppo, sin dall'inizio, ha valutato positivamente l'adozione della metodologia _Scrum_ in quanto ha permesso di suddividere il carico di lavoro in maniera equa, valutando preventivamente l'_effort_ dei singoli _task_.

Inoltre, sin dal termine del primo sprint, è stato possibile consegnare all'utente un prototipo dell'applicazione funzionante ed opportunamente testato.

Il primo sprint ha permesso di ottenere una prima versione dell'applicazione attraverso cui è già possibile impostare i parametri della simulazione e visualizzare lo scorrere del tempo.

A posteriori, si è evidenziato che il secondo sprint è stato quello più consistente in quanto ha introdotto le funzionalità core dell'applicazione: la visualizzazione dello stato globale e delle singole aree, periodicamente aggiornata.

Il terzo sprint si è rilevato meno impegnativo dei precedenti, pur introducendo la seconda parte di funzionalità core dell'applicazione ossia la gestione delle azioni compiute dall'utente all'interno delle aree.

Infine, nel quarto sprint il gruppo non ha sviluppato nuove funzionalità ma si è concentrato sulle migliorie rispetto all'esperienza utente e alla qualità del codice, al fine di rilasciare l'applicazione finale.