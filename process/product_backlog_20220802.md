# Sprint Backlog2022-08-02
## Sprint goal:
Visualizzare lo stato della simulazione e, in particolare, gestire i seguenti componenti:
 - aree della serra,
 - sensori,
 - i valori rilevati dai sensori nelle diverse aree,
 - i parametri ambientali.

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
            <td rowspan="5">Consentire lo scorrimento fra le diverse pagine dell'applicazione</td>
            <td>Refactoring schermata BaseView, con aggiunta pulsanti per la navigazione</td>
            <td>Elena</td>
            <td>2</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Realizzazione architettura MVC tramite cake pattern per la selezione della città</td>
            <td>Elena</td>
            <td>4</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
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
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Realizzazione architettura MVC tramite cake pattern per la divisione in aree della serra</td>
            <td>Maria</td>
            <td>7</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td rowspan="6">Visualizzare lo stato aggiornato della simulazione</td>
            <td>*Refactoring schermata di visualizzazione stato globale della serra</td>
            <td>Veronika</td>
            <td>1</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
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
            <td>3</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Gestione e visualizzazione aggiornamento tempo</td>
            <td>Veronika</td>
            <td>3</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Definizione interfaccia area e realizzare la sua implementazione</td>
            <td>Maria</td>
            <td>3</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Gestire il collegamento fra: la serra, le aree e le piante</td>
            <td>Maria</td>
            <td>3</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td rowspan="7">Visualizzare lo stato aggiornato delle singole aree</td>
            <td>Impostazione formula di aggiornamento dei valori dei sensori</td>
            <td>Elena, Anna</td>
            <td>2</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Definizione interfaccia sensori</td>
            <td>Elena, Anna</td>
            <td>3</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Implementazione sensore luminosità</td>
            <td>Anna</td>
            <td>2</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Implementazione sensore temperatura</td>
            <td>Anna</td>
            <td>2</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Implementazione sensore umidità del suolo</td>
            <td>Elena</td>
            <td>2</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Implementazione sensore umidità del terreno</td>
            <td>Elena</td>
            <td>2</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>Gestire interazione e collegamento fra: le aree e i sensori</td>
            <td>Elena, Anna</td>
            <td>4</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </tbody>
</table>

## Sprint result:
//TODO (Conclusioni sul lavoro svolto)

#### Note:
Gli sprint tasks marcati con (*), derivano dal precedente sprint.
