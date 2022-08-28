# 5. Implementazione
Il seguente capitolo motiva e dettaglia, aspetti implementativi ritenuti rilevanti
per una corretta comprensione del progetto. 

Va inoltre sottolineato, che il codice realizzato, è stato opportunamente documentato mediante la `Scaladoc`, la quale può essere utilizzata come un ulteriore riferimento per meglio comprendere l'implementazione del programma e il suo comportamento.

## 5.1 Utilizzo del paradigma funzionale
Durante lo sviluppo del progetto, si è cercato di utilizzare il più possibile il paradigma funzionale, cercando di raffinare sempre di più la soluzione adottata per poter sfruttare al meglio i vantaggi che questo pardigma offre. 

Se infatti durante la realizzazione di una detemrinata funzionalità, ci si rendeva conto di aver utilizzato un'approcio più legato all'object-oriented che a al paradigma funzionale, dopo aver valutato i diversi aspetti, della soluzione, che potevano essere migliorati e modificati adottando, invece, elementi del paradigma funzinoale, si procedeva con il refactoring del codice ed ad effettuare queste modifiche.

Nelle seguenti sezioni, verranno descritti con maggiore dettaglio alcuni elmenti della programmazione funzionale, che sono stati utilizzati all'interno del progetto e alcuni esempi del loro utilizzo.

### 5.1.1 Higher-order functions
//Ele
//TODO descrivere utilizzo di higher-order functions all'interno del codice, inserire esempi di utilizzo nel codice, spiegando anche tali esempi

### 5.1.2 Abstract type
// Vero
//TODO descrivere utilizzo di abstract-types all'interno del codice, inserire esempi di utilizzo nel codice, spiegando anche tali esempi

### 5.1.3 For-comphrension
Al fine di rendere il codice meno imperativo si è fatto uso della _for-comphrension_, un costrutto funzionale basato sulle monadi per operare sulle collezioni. Oltre a rendere il codice più funzionale, la scelta dell'utilizzo della _for-comphrension_ è supportato dall'incremento della leggibilità del codice, come si può vedere nel seguente estratto di codice, utilizzato per la creazione degli oggetti `ManageSensor` il cui compito è racchiudere tutte le informazioni utili riguardati un sensore.

```scala
    for
        (key, m) <- mapSensorNamesAndMessages.toList
        optK = m.getOrElse("name", "")
        um = m.getOrElse("um", "")
        msg = m.getOrElse("message", "")
    yield ManageSensorImpl(
        key,
        optimalValueToDouble.getOrElse("min_" + optK, 0.0),
        optimalValueToDouble.getOrElse("max_" + optK, 0.0),
        um,
        sensorsMap(key),
        BigDecimal(sensorsMap(key).getCurrentValue).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble,
        msg,
        firstSensorStatus(
            BigDecimal(sensorsMap(key).getCurrentValue).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble,
            optimalValueToDouble.getOrElse("min_" + optK, 0.0),
            optimalValueToDouble.getOrElse("max_" + optK, 0.0)
        )
    )
```
Nell'esempio si itera sulla mappa contenete le costanti riguardanti i sensori, come il nome del sensore, l'unità di misura ed il messaggio di errore associato ad esso. Questi valori vengono poi impiegati nella costruzione dell'oggetto `ManageSensor` per reperire le informazioni relative ad un sensore per una specifica pianta, creare e memorizzare l'istanza del relativo sensore, inizializzare il suo stato e tenere traccia del valore corrente rilevato da esso. 

//TODO descrivere utilizzo di for-comphrension all'interno del codice, inserire esempi di utilizzo nel codice, spiegando anche tali esempi

### 5.1.4 Trait e mixins
//Anna
//TODO descrivere utilizzo di traits e mixin nel codice (CAKE PATTERN), inserire esempi di utilizzo nel codice, spiegando anche tali esempi

## 5.2 Utilizzo del paradigma logico
//Anna
//TODO parlare dell'utilizzo di prolog all'interno del Programmazione

## 5.3 Programmazione reattiva e asincrona
//Vero
//TODO parlare dell'utilizzo della porgrammazione reattiva e asincrona all'interno del programma eventualmente inserire sottosezioni con esempi di utilizzo nel codice

## 5.4 Richieste dei dati
//Maria
//TODO descrivere come sono state fatte le richieste per ottenre i dati

## 5.5 Utilizzo di ScalaFX e SceneBuilder
//Ele
//TODO parlare dell'utilizzo di ScalaFX e javafx all'interno del progetto

## 5.6 Testing
Per testare le funzinalità principali del porgramma, si è deciso di utilizzare la modalità _Test Driven Development (TDD)_. Questa strategia, prevede di scrivere per prima cosa il codice di _testing_, indicando il compoertamento corretto della funzionalità che si vuole testare e successivamente scrivere il codice di produzione, affinchè i tests individuati passino correttamente. Una volta scritto il codice di produzione e aver passato i tests si può procedere al refactoring e al miglioramento della soluzione ottenuta. 

Il TDD, quindi, si compone di tre diverse fasi che si susseguono: red, green e refactor. Nella fase red si ha solo il codice di testing e i tests che sono stati scritti, pertanto non passerano, in quanto il codice di produzione risulta essere mancante, nella fase green, invece, si procede alla scrittura del codice di produzione in modo da poter superare i tests precedentemente definiti e infine nella fase di refactor, il codice di produzione scritto viene migliorato.

Il team di lavoro per lo sviluppo dell'applicazione, ha inoltre deciso di adottare la pratica di _continuous integration_, decidendo di realizzare due flussi di lavoro sul relativo _Repository_, il primo dedicato all'esecuzione dei tests sui diversi sistemi operativi: Windows, Linux, Mac e il secondo diretto a deterimnare la _coverage_ ottenuta, mediante i tests effettuati.

Per questo progetto le funzionalità del modello, che racchiudono la logica di buisness, sono state testate mediante l'utilizzo di ScalaTest, mentre per testare gli elementi della View, siccome è stata utilizzata la libreria ScalaFX, si è deciso di utilizzare, per il testing, la libreria TestFx.

Nelle seguenti sezioni, è possibile trovare una descrizione maggiormente dettagliata relativa ai tests effettuati, le modalità utilizzate e la coverage ottenuta.

### 5.6.1 Utilizzo di ScalaTest
Per testare le funzionalità legate alla logica di buisness dell'applicazione, si è deciso di utilizzare la libreria ScalaTest, realizzando diverse _Suits_ di testing.

In particolare, tutte le diverse classi di testing realizzate che utilizzano ScalaTest, estendono la classe `AnyFunSuite` e i tests sono stati scritti seguendo il seguente stile:
```scala
test("At the beginning the temperature should be initialized with the default value") {
    val defaultTemperatureValue = 27
    areaComponentsState.temperature shouldEqual defaultTemperatureValue
}
```

Per verificare determinate condizioni, come ad esempio di uguaglianza, minoranza o maggioranza, si è fatto utilizzo dei `matchers` di ScalaTest. Nello specifico, se la classe di testing estende il `trait Matchers`, ha la possibilità di utilizzare all'interno dei tests, delle _keywords_ come: `should be`, `equal`, `shouldEqual` ecc. che consentono di verificare le condizioni espresse. 

Infine, per testare il verificarsi di determinati risultati o condizioni, che però possono impiegare un certo tempo per avvenire, da quando è stato generato l'evento che ne è la causa, si è fatto uso di `eventually`. In particolare, se la classe di test estende il `trait Eventually`, ha la possibilità di definire dei tests, che presentano una condizione che prima o poi si deve verificare entro un lasso di tempo predefinito.
```scala
test("The air humidity value should decrease because the ventilation and the humidity are inactive") {
    setupTimer(500 microseconds)
    initialValueTest()

    eventually(timeout(Span(1000, Milliseconds))) {
        humiditySensor.getCurrentValue should be < initialHumidity
    }
}
```

### 5.6.2 Utilizzo di Unit test e TestFx
Per poter testare gli aspetti relativi alla visualizzazione dei dati e all'interfaccia utente, si è deciso di utilizzare le librerie TestFx e JUnit.

TestFx, richiede che per poter scrivere dei tests, che vadano a verificare degli elementi di JavaFX, la classe di testing estenda la classe `ApplicationExtension`, dopodiché è necessario definire un metodo contrassegnato dalla notazione `@Start`, per impostare la schermata che si vuole testare; una volta fatto questo si ha la possibilità di definire i tests per la GUI.

Nello specifico, i diversi _Unit tests_ che si vogliono realizzare devono prendere tutti come argomenti `FxRobot`, il quale rappresenta un oggetto che può essere utilizzato per poter simulare i comportamenti dell'utente sull'interfaccia grafica, come mostrato nel seguente esempio.
```scala
@Test def testAfterPlantSelection(robot: FxRobot): Unit =
    //...
    val checkBox = robot.lookup(selectablePlantsBoxId)
                        .queryAs(classOf[VBox])
                        .getChildren
                        .get(plantIndex)
    //when:
    robot.clickOn(checkBox)

    //then:
    assert(robot.lookup(selectedPlantBoxId)
                .queryAs(classOf[VBox])
                .getChildren.size == selectedPlantNumber)
    verifyThat(numberPlantsSelectedId, hasText(selectedPlantNumber.toString))
```
Come si può vedere sempre dall'esempio, per verificare le proprietà degli elementi dell'interfaccia, è stata utilizzata la clase `FxAssert` e il metodo `verifyThat`, il quale consente, una volta passato l'id del componente FXML, di verificare una determinata proprietà su di esso. Le proprietà possono essere definite tramite i `matchers` di TestFX.

In questo modo, quindi, è stato possible per il team di sviluppo effettuare dei tests automatici sull'interfaccia grafica che si intende mostrare all'utente. 

Va comunque sottolineato che per testare gli aspetti di View, sono stati svolti anche numerosi tests manuali, anche perchè molto spesso, risultava essere complicato tramite tests automatici, verificare determinate condizioni, questi di fatti non possono essere considerati completamente esaustivi nella verifica degli aspetti di interazione con l'utente.

### 5.6.3 Coverage
Come detto in precedenza, il team di sviluppo ha realizzato anche un flusso di lavoro dedicato alla _coverage_, in modo tale da poter analizzare la copertura ottenuta, ogni qual volta vengono inseriti dei nuovi tests o modificati quelli precedenti.

La _code coverage_ fa riferimento, sostanzialmente, alla quantità di istruzioni di codice che vengono eseguite durante l'esecuzione dei tests, tuttavia ottenere una coverage del 100% non significa che il testing effettuato riesce a ricoprire tutti gli scenari, infatti l'obiettivo che ci si è dati, non è stato quello di raggiungere il 100% della copertura ma di testare le cose giuste.

In particolare, per poter ottenere i risultati relativi alla _coverage_ si è fatto utilizzo del tool `JaCoCo`, ottenendo alla fine il risultato illustrato in figura [Fig. 5.6.3.1].

<div align="center">
  <img src="img/coverage.png" />
  <p> Fig. 5.6.3.1 - Coverage finale ottenuta </p>
</div>

[Fig. 5.6.3.1]: img/coverage.png

Come si può vedere dalla [Fig. 5.6.3.1], la coverage finale ottenuta è del 79% su un totale di 129 test effettuati.

Gli elementi per cui si ha una coverge più elevata sono quelli che fanno riferimento al Model dell'applicazione, mentre quelli per cui si ha una coverage più bassa fanno riferimento agli elementi della View, che come spiegato nella precedente sezione [Sec. 5.6.2](#562-utilizzo-di-unit-test-e-testfx), sono stati testati sia tramite test automatici che tramite test manuali.

## 5.7 Suddivisione del lavoro
//TODO descrivere modalità di assegnazzione e svolgimento dei task

### 5.7.1 Folin Veronika
//TODO illustrare lavoro svolto

### 5.7.2 Mengozzi Maria
Nello sviluppo del progetto mi sono occupata insieme ad _Elena_ della selezione della città in cui è ubicata la serra. Specificatamente ho realizzato:
- l'oggetto di utility `UploadCities`, esso incapsula il meccanismo utile a convertire il file testuale contenente il nome dei comuni italiani nel file prolog utilizzato nell'applicazione per la visualizzazione e la scelta della città;
- il modulo `Environment`, che racchiude l'interfaccia e l'implementazione del meccanismo di salvataggio della città scelta;
- il meccanismo per reperire, mediante una richiesta http, le previsioni meteorologiche previste per la giornata in cui si svolge la simulazione e per ottenere, a partire da questi, i dati ambientali rispettivi ad uno specifico orario (`updateCurrentEnvironmentValues` nel modulo `EnvironmentModelModule`).

Sempre insieme ad _Elena_ mi sono occupata della realizzazione dei controller (`AreaDetailsController` e `AreaXXXController`) per la schermata del dettaglio di un area e lateralmente alla gestione del suo MVC. Nel caso del controller ho gestito i metodi relativi all'interfacciamento con model, da me realizzato.

Ho collaborato inoltre con _Anna_ per quanto riguarda il collegamento tra i sensori e le aree e i parametri ambientali, e insieme al resto del gruppo per la definizione della struttura del progetto e il collegamento tra le varie parti realizzate.

Per quanto riguarda l’identificazione di una sezione di progetto pienamente riconducibile alla sottoscritta, si possono le aree. Nello specifico, le parti da me singolarmente implementate comprendono:
- il modulo `GreenHouseDivisionMVC` e i rispettivi sottomoduli `GHModelModule`, `GHControllerModule` e `GHViewModule`. Tale modulo si occupa di gestire la parte di suddivisione della serra in aree e il suo continuo aggiornamento durante tutto lo svolgimento della simulazione mediante la sottoscrizione di un evento di tipo `interval` e all'evento di cambio di stato di un'area. La sottoscrizione questi due eventi è stata scelta al fine di non sovraccaricare l'_EDT_ (Event Dispatch Thread) con richieste di aggiornamento della view, le quali avrebbero reso meno reattiva l'interfaccia.
- il modulo `AreaMVC` e i rispettivi sottomoduli `AreaModelModule`, `AreaControllerModule` e `AreaViewModule`. Il seguente modulo si occupa di implementare le singole aree che compongono le serra, per farlo interagisce con i sensori presenti al suo interno affinchè possa mantenere aggiornati e mostrare all'utente i valori rilevati da questi ultimi. In particolare il model oltre a tenere traccia dei valori rilevati dai parametri si occupa anche di memorizzare le operazioni effettuate dall'utente per riportare e mantenere i parametri nei range ottimali e notificare il cambio di stato dell'area. 
- le classi `ManageSensor`e `AreaSensorHelper`. Queste vengno utilizzate dal modulo `AreaModelModule` per assolvere al suo compito e in particolare per tutte quelle operazioni che coinvolgono i sensori


Per la parte di testing mi sono occupata della realizzazione delle seguenti classi di test:
- AreaTest.scala
- UploadCitiesTest.scala
- EnvironmentTest.scala
- GreenHouseTest.scala
- GreenHouseDivisionViewTest.scala

Inoltre alcune delle funzionalità da me implementate sono presenti anche nelle altre classi di test, come ad esempio in //TODO

### 5.7.3 Vitali Anna 
//TODO illustrare lavoro svolto

### 5.7.4 Yan Elena
//TODO illustrare lavoro svolto
