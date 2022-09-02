# PPS-22-smartgh

Project for `Paradigmi di Programmazione e Sviluppo` course of `University of Bologna` (A.Y. 2021/2022).

`PPS-22-smartgh` application aims to simulate the management of an intelligent greenhouse, able to interact with the environment in which it is located through sensors that detect relevant parameters for plant growth and receive updates on external environmental conditions.

Once the application is started, it will be possible to set the location of the greenhouse and the plants to cultivate inside it.

The user will be able to view the evolution of the state of the greenhouse during the day and will be able to interact with the individual areas, carrying out routine maintenance actions or guided corrective actions if the sensors detect an alarm situation.

## Requirements
The following dependencies are required to run the application:
- sbt version 1.6.1
- scala version 3.1.2
- JDK version 11

## Documentation and development process

You can find the project's report in the `docs` folder into the `doc` branch or at this [link](https://annavitali.github.io/PPS-22-smartgh/).

Futhermore, you can find the detailed description of the development process in the `process` folder into the `doc` branch.

## Usage
You can find the latest `jar` for your OS inside the [Releases section](https://github.com/AnnaVitali/PPS-22-smartgh/releases).

To execute the application:
```powershell
$ java -jar `path-to-downloaded-jar`
```

Otherwise, you can clone the repository, move to the `smart_greenhouse` folder and execute the application with the following commands:

```powershell
$ sbt compile
$ sbt run
```

You can find the user guide [here](https://annavitali.github.io/PPS-22-smartgh/08_user_guide.html) (IT version) or within the application, when you start the simulation (EN version).

## Test
You can clone the repository, move to the `smart_greenhouse` folder and execute tests with the command:
```powershell
$ sbt test
```


## Authors
- [Veronika Folin](https://github.com/veronikafolin)
- [Maria Mengozzi](https://github.com/MariaMengozzi)
- [Anna Vitali](https://github.com/AnnaVitali)
- [Elena Yan](https://github.com/yan-elena)
