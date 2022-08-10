# Sprint Backlog2022-08-02
## Sprint goal
L'obiettivo di questo sprint, consiste nel dare la possibilità all'utente di visualizzare lo stato della simulazione.

In particolare, per raggiungere questo scopo, occorre gestire i seguenti componenti:
 - le aree della serra,
 - i sensori,
 - i valori rilevati dai sensori nelle diverse aree,
 - i parametri ambientali,
 - il tempo virtuale della simulazione.

## Product backlog

<table>
    <thead>
        <td><b>Elemento del product backlog</b></td>
        <td><b>Sprint Task</b></td>
        <td><b>Volontario</b></td>
        <td><b>Stima iniziale del costo</b></td>
        <td><b>Dopo il 1<sup>o</sup> giorno</b></td>
        <td><b>Dopo il 2<sup>o</sup> giorno</b></td>
        <td><b>Dopo il 3<sup>o</sup> giorno</b></td>
        <td><b>Dopo il 4<sup>o</sup> giorno</b></td>
        <td><b>Dopo il 5<sup>o</sup> giorno</b></td>
        <td><b>Dopo il 6<sup>o</sup> giorno</b></td>
        <td><b>Dopo il 7<sup>o</sup> giorno</b></td>
    </thead>
    <tbody>
        <tr>
            <td rowspan="7">Consentire lo scorrimento fra le diverse pagine dell'applicazione</td>
            <td>Refactoring schermata BaseView, con aggiunta pulsanti per la navigazione</td>
            <td>Elena</td>
            <td>2</td>
            <td>2</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Realizzazione architettura MVC tramite cake pattern per la selezione della città</td>
            <td>Elena</td>
            <td>4</td>
            <td>4</td>
            <td>3</td>
            <td>2</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Realizzazione architettura MVC tramite cake pattern per la selezione delle piante</td>
            <td>Anna</td>
            <td>6</td>
            <td>2</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Realizzazione architettura MVC tramite cake pattern per la visualizzazoine dello stato globale della serra</td>
            <td>Veronika</td>
            <td>7</td>
            <td>7</td>
            <td>5</td>
            <td>3</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Realizzazione architettura MVC tramite cake pattern per la divisione in aree della serra</td>
            <td>Maria</td>
            <td>6</td>
            <td>6</td>
            <td>4</td>
            <td>2</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
         <tr>
            <td>Realizzazione MVC globale della simulazione</td>
            <td>Anna, Elena, Maria, Veronika</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>2</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Realizzazione collegamento fra i diversi MVC</td>
            <td>Anna, Elena, Maria, Veronika</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>2</td>
            <td>0</td>
        </tr>
        <tr>
            <td rowspan="6">Visualizzare lo stato aggiornato della simulazione</td>
            <td>*Refactoring schermata di visualizzazione stato globale della serra</td>
            <td>Veronika</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>*Completare controller per gestire l'avvio del tempo</td>
            <td>Anna</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Aggiornamento parametri ambientali tramite richieste periodiche</td>
            <td>Veronika, Maria</td>
            <td>4</td>
            <td>2</td>
            <td>2</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Gestione e visualizzazione aggiornamento tempo</td>
            <td>Veronika</td>
            <td>3</td>
            <td>3</td>
            <td>2</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Definizione interfaccia area e realizzare la sua implementazione</td>
            <td>Maria</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Gestire il collegamento fra: la serra, le aree e le piante</td>
            <td>Maria</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td rowspan="8">Visualizzare lo stato aggiornato delle singole aree</td>
            <td>Impostazione formula di aggiornamento dei valori dei sensori</td>
            <td>Elena, Anna</td>
            <td>2</td>
            <td>2</td>
            <td>2</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Definizione interfaccia sensori</td>
            <td>Elena, Anna</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>4</td>
            <td>2</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Implementazione sensore luminosità</td>
            <td>Anna</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Implementazione sensore temperatura</td>
            <td>Anna</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Implementazione sensore umidità dell'aria</td>
            <td>Elena</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>2</td>
            <td>1</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Implementazione sensore umidità del terreno</td>
            <td>Elena</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>2</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Gestire interazione e collegamento fra: le aree e i sensori</td>
            <td>Elena, Anna, Maria</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
        <tr>
            <td>Gestire interazione e collegamento fra: l'environment e i sensori</td>
            <td>Elena, Anna, Veronika</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>3</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
        </tr>
    </tbody>
</table>

## Sprint result
Al termine di questo sprint è possibile interagire con le schermate principali che riguardano la personalizzazione dei parametri scelti dall'utente e la visualizzazione dell'andamento della simulazione. Quest'ultima consiste nell'aggiornamento periodico (diretto dal tempo virtuale) dei parametri rilevati dai sensori nelle diverse aree, sulla base di quelli ottenuti rispetto alla località selezionata. 

#### Note:
Gli sprint tasks marcati con (*), derivano dal precedente sprint.
