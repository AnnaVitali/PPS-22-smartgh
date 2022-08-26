# 5. Implementazione
//TODO introduzione capitolo

## 5.1 Utilizzo del paradigma funzionale
//TODO parlare dell'utilizzo di scala per il progetto

### 5.1.1 Higher-order functions
//TODO descrivere utilizzo di higher-order functions all'interno del codice

### 5.1.2 Abstract type
//TODO descrivere utilizzo di abstract-types all'interno del codice

### 5.1.3 For-comphrension
//TODO descrivere utilizzo di for-comphrension all'interno del codice

### 5.1.4 Trait e mixins
//TODO descrivere utilizzo di traits e mixin nel codice (CAKE PATTERN)

## 5.2 Utilizzo del paradigma logico
//TODO parlare dell'utilizzo di prolog all'interno del Programmazione

## 5.3 Programmazione reattiva e asincrona
//TODO parlare dell'utilizzo della porgrammazione reattiva e asincrona all'interno del programma eventualmente inserire sottosezioni con esempi di utilizzo nel codice

## 5.4 Richieste dei dati
//TODO descrivere come sono state fatte le richieste per ottenre i dati

## 5.5 Utilizzo di ScalaFX e SceneBuilder
//TODO parlare dell'utilizzo di ScalaFX e javafx all'interno del progetto

## 5.6 Testing
Per testare le funzinalità principali del porgramma, si è deciso di utilizzare la modalità _Test Driven Development (TDD)_. Questa strategia, prevede di scrivere per prima cosa il codice di _testing_, indicando il compoertamento corretto della funzionalità che si vuole testare e successivamente scrivere il codice di produzione, affinchè il tests individuati passino correttamente. Una volta scritto il codice di produzione e aver passato i tests si può procedere al refactoring e al miglioramento della soluzione ottenuta. 

Il TDD, quindi, si compone di tre diverse fasi che si susseguono: red, green e refactor. Nella fase red si ha solo il codice di testing e i tests che sono stati scritti, pertanto non passerano, in quanto il codice di produzione risulta essere mancante, nella fase green, invece, si procede alla scrittura del codice di produzione in modo da poter superare i tests precedentemente definiti e infine nella fase di refactor, il codice di produzione scritto viene migliorato.

Il team di lavoro per lo sviluppo dell'applicazione, ha inoltre deciso di adottare la pratica di _continuous integration_, decidendo di realizzare due flussi di lavoro sul relativo _Repository_, il primo dedicato all'esecuzione dei tests sui diversi sistemi operativi: Windows, Linux, Mac e il secondo diretto a deterimnare la _coverage_ ottenuta, mediante i tests effettuati.

Per questo progetto le funzionalità del modello, che racchiudono la logica di buisness, sono state testate mediante l'utilizzo di ScalaTest, mentre per testare gli elementi della View, siccome è stata utilizzata la libreria ScalaFX, si è deciso di utilizzare, per il testing, la libreria TestFx.

Nelle seguenti sezioni, è possibile trovare una descrizione maggiormente dettagliata relativa ai tests effettuati, le modalità utilizzate e la coverage ottenuta.

### 5.6.1 Utilizzo di ScalaTest
Per testare le funzionalità legate alla logica di buisness dell'applicazione, si è deciso di utilizzare la libreria ScalaTest, realizzando diverse _Suits_ di testing.

In particolare, tutte le diverse classi di testing realizzate che utilizzano ScalaTest, estendono la classe `AnyFunSuite` e i tests sono stati scritti seguendo il seguente stile:

    test("At the beginning the temperature should be initialized with the default value") {
        val defaultTemperatureValue = 27
        areaComponentsState.temperature shouldEqual defaultTemperatureValue
    }

Per verificare determinate condizioni, come ad esempio di uguaglianza, minoranza o maggioranza, si è fatto utilizzo dei `matchers` di ScalaTest. Nello specifico, se la classe di testing estende il `trait Matchers`, ha la possibilità di utilizzare all'interno dei tests, delle _keywords_ come: `should be`, `equal`, `shouldEqual` ecc. che li consentono di verificare le condizioni espresse. 

Infine, per testare il verificarsi di determinati risultati o condizioni, che però possono impiegare un certo tempo per avvenire, da quando è stato generato l'evento che ne è la causa, si è fatto uso di `eventually`. In particolare, se la classe di test estende il `trait Eventually`, ha la possibilità di definire dei tests, che presentano una condizione che prima o poi si deve verificare, per cui è possibile anche specificare un lasso di tempo massimo, entro cui deve essersi verificata.

    test("The air humidity value should decrease because the ventilation and the humidity are inactive") {
        setupTimer(500 microseconds)
        initialValueTest()

        eventually(timeout(Span(1000, Milliseconds))) {
            humiditySensor.getCurrentValue should be < initialHumidity
        }
    }

### 5.6.2 Utilizzo di Unit test e TestFx
Per poter testare gli aspetti relativi alla visualizzazione dei dati e all'interfaccia utente, si è deciso di utilizzare le librerie TestFx e JUnit.

TestFx, richiede che per poter scrivere dei tests, che vadano a verificare degli elementi di JavaFX, la classe di testing estenda la classe `ApplicationExtension`, dopodiché è necessario definire un metodo contrassegnato dalla notazione `@Start`, per impostare la schermata che si vuole testare, una volta fatto questo si ha la possibilità di definire i tests per la GUI.

Nello specifico, i diversi _Unit tests_ che si vuole realizzare, devono prendere tutti come argomenti `FxRobot`, il quale rappresenta un oggetto, che può essere utilizzato per poter simulare i comportamenti dell'utente sull'interfaccia grafica, come mostrato nel seguente esempio.

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

Come si può vedere sempre dall'esempio, per verificare le proprietà degli elementi dell'interfaccia, è stata utilizzata la clase `FxAssert` e il metodo `verifyThat`, il quale consente una volta passato l'id del componente FXML, di verificare una determinata proprietà su di esso. Le proprietà possono essere definite tramite i `matchers` di TestFX.

In questo modo, quindi, è stato possible per il team di sviluppo effettuare dei tests automatici sull'interfaccia grafica che si intende mostrare all'utente. 

Va comunque sottolineato che per testare gli aspetti di View, sono stati svolti anche numerosi tests manuali, anche perchè molto spesso, risultava essere complicato tramite tests automatici, verificare determinate condizioni, questi di fatti non possono essere considerati completamente esaustivi nella verifica degli aspetti di interazione con l'utente.

### 5.6.3 Coverage
//TODO illustrtare la coverage ottenuta e i meccanismi di misurazione

## 5.7 Suddivisione del lavoro
//TODO descrivere modalità di assegnazzione e svolgimento dei task

### 5.7.1 Folin Veronika
//TODO illustrare lavoro svolto

### 5.7.2 Mengozzi Maria
//TODO illustrare lavoro svolto

### 5.7.3 Vitali Anna 
//TODO illustrare lavoro svolto

### 5.7.4 Yan Elena
//TODO illustrare lavoro svolto
