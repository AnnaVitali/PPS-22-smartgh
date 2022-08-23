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

![plant selector MVC][4.2.2.1]

[4.2.2.1]: img/plant_selector_MVC.png

<p align = "center">
Fig. 4.2.2.1 - Architettura PlantSelectorMVC
</p>

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
