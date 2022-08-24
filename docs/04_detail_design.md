# 4. Design dettagliato
//TODO introduzione capitolo, che cosa verrà inserito, cosa prevede, parlare di Cake Pattern e MVC

## 4.1 Simulazione
//TODO Simulation View Ele, Simulation Controller Vero

//TODO decidere sezioni capitoli
## 4.2 Impostazione dei parametri della simulazione

### 4.2.1 Selezione della città
//TODO Elena

#### 4.2.2.1 Environment
//TODO Maria

### 4.2.2 Selezione delle piante
Per poter realizzare il meccanismo di selezione delle piante si è deciso di adottare, come già detto precedentemente, il _pattern MVC_ e il _Cake pattern_.

In particolare, come si può vedere dalla figura [4.2.2.1], la classe `PlantSelectorMVC`, racchiude i componenti: `PlantSelectorModel`, `PlantSelectorController` e `PlantSelectorView` derivanti dai rispettivi moduli. L’adozione di quest’architettura, quindi, non rende più necessaria l’istanziazione di ogni componente e il loro successivo collegamento per risolvere le diverse dipendenze, ma gli elementi del _pattern MVC_ vengono racchiusi all’interno di `PlantSelectorMVC` e possono essere acceduti liberamente.

Per poter utilizzare `PlantSelectorModel`, `PlantSelectorController` o `PlantSelectorView`, basterà semplicemente istanziare `PlantSelectorMVC` e accedere ai suoi elementi. 

<div align="center">
  <img src="img/plant_selector_MVC.png" />
  <p> Fig. 4.2.2.1 - Architettura PlantSelectorMVC </p>
</div>

[4.2.2.1]: img/plant_selector_MVC.png

Il Model per la selezione delle piante (figura [4.2.2.2]) viene racchiuso all'interno di un modulo chiamato `PlantSelectorModelModule`, nello specifico all'interno del suddetto modulo torviamo:

- il `trait PlantSelectorModel`, il quale espone i diversi metodi che potranno essere richiamati sul Model e che consentono la gestione del meccanismo di selezione delle piante;
- la classe `PlantSelectorModelImpl`, la quale detiene l'implementazione dei metodi dell'interfaccia `PlantSelectorModel` e viene racchiusa all'interno del `trait Component`;
- il `trait Provider`, che detiene l'oggetto `plantSelectorModel` che potrà essere utilizzato dall'MVC;
- il `trait interface`, il quale estende sia il `trait Provider` che il `trait Component` riusciendo così a comprendere tutte le loro proprietà e a sua volta `Interface` verrà poi esteso da `PlantSelectorMVC` che in questo modo potrà utilizzare l'elemento `plantSelectorModel` e accedere alle proprieta del modello.

L'architettura realizzata tramite questi componenti e il loro _mix-in_ costituisce, quindi, il _Cake Pattern_ del Model.

<div align="center">
  <img src="img/plant_selector_model.png" />
  <p> Fig. 4.2.2.2 - Model per la selezione delle piante </p>
</div>

[4.2.2.2]: img/plant_selector_model.png

La View per la selezione delle piante (figura [4.2.2.3]), viene racchiusa all'interno del modulo `SelectPlantViewModule` e al suo interno troviamo:

- il `trait SelectPlantView`, che detiene i diversi metodi che potranno essere richiamati sulla View e che si occupano di gestire l'interazione con l'utente. 

  il `trait SelectPlantView` rappresenta anche il Controller dell'FXML, per la relativa schermata di selezione delle piante. Essa implementa sia l'interfaccia  `ViewComponent` che l'interfaccia `ContiguousSceneView`. In particolare, `SelectPlantView`, racchiude il `BorderPane` che contiente i diversi elementi della     scena di selzione delle piante, tale scena viene inserita all'interno della `BaseView` dell'applicazione che contiene gli elmenti comuni a tutte le schermate   che vengono sempre riportati; in questo modo, quindi, `SelectPlantView` rappresenta una scena che verrà racchiusa all'interno di un'altra e tutte le scene,     per poter essere visualizzate correttamente e racchiuse all'interno della scena principale, devono implementare l'interfaccia `ViewComponent`. 

  L'interfaccia `ContiguousSceneView`, invece, racchiude i metodi comuni a tutte le views che danno la possibilità all'utente di effettuare uno spostamento a     una schermata successiva;
- la classe `SelectPlantViewImpl`, la quale detiene l'implementazione dei metodi del `trait SelectPlantView` e viene racchiusa all'interno del `trait Component`;
- il `trait Component`, il quale rispetto al Model, contiente un campo `context` di tipo `Requirements`, nello specifico `Requirments` è un  _Abstract type_ e viene utilizzato per specificare le dipendenze che legano la View agli altri elementi del _pattern MVC_, infatti la View, per poter funzionare correttamente ha necessita di informare il Controller delle azioni che sono state compiute dall'utente, per fare questo, quindi, ha bisogno del componente Controller e di conseguenza il tipo `Requirements` sarà proprio rappresentato dal `Provider` del Controller;
- il `trait Provider`, che detiene l'oggetto `SelectPlantView` che potrà essere utilizzato dall MVC;
- il `trait Interface`, il quale come per il Model, estende sia il `trait Provider` che il `trait Component`, riuscendo ad ereditare tutte le loro proprietà dando quindi la possibilità a `PlantSelectorMVC`, che lo estende, di poter utilizzare l'elemento `selectPlantView` e accedere alle proprietà della View.

<div align="center">
  <img src="img/select_plant_view.png" />
  <p> Fig. 4.2.2.3 - View per la selezione delle piante </p>
</div>

[4.2.2.3]: img/select_plant_view.png

Rispetto all'architettura del Model vista precedentemente, la View presenta l'elemento `Requirements` che viene utilizzato per specificare le dipendenze che vi sono e che devono essere risolte fra View e Controller. Per il _pattern MVC_ infatti, sappiamo che vi sono queste dipendenze: C->V, V->C e C->M e tramite il _Cake Pattern_, una volta realizzati tutti gli elementi di cui si compone, possiamo risolvere atomaticamente queste dipendenze ed è proprio questo il suo principale vantaggio.

#### 4.2.2.1 Plant
//TODO Vero

## 4.3 Avvio Simulazione
//TODO Ele e Vero

### 4.3.1 Caricamento dei dati
//TODO Anna schermata caricamento,  Vero parametri ambientali

### 4.3.2 Ambiente
//TODO Vero

### 4.3.3 Tempo virtuale
//TODO Model Ele, View Vero, Controller Ele e Vero

## 4.4 Serra
//TODO

### 4.4.1 Suddivisione in aree
//TODO Maria

### 4.4.2 Visualizzazione dettaglio aree
//TODO Model Maria, View Ele Controller Maria e Ele

### 4.4.3 Sensori
//TODO Sensor Anna, Luminosità e temperatura Anna, Umidità Ele

## 4.5 Fine simulazione
//TODO Anna
