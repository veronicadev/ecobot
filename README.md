<p align="center">
</p>
<h2 align="center"> Ecobot</h2>
  <p align="center">
    A telegram bot for the separate collection calendar in Asti municipality
    <br />
    <a href="https://github.com/veronicadev/ecobot"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/veronicadev/ecobot" target="_blank">View Demo</a>
    ·
    <a href="https://github.com/veronicadev/ecobot/issues">Report Bug</a>
    ·
    <a href="https://github.com/veronicadev/ecobot/issues">Request Feature</a>
  </p>


<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Features](#features)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [Contact](#contact)

 
## About The Project
Ecobot is a telegram bot that provides information about the separate collection calendar in Asti. It reads the ```data.json``` file which contains data of the calendar. This is an example:
```json
{
  "municipalityName": "Asti",
  "areas":[
      {
        "name": "Zona 1",
        "streets": "Via Roma, Corso Torino ecc",
        "addressedTo": "Utenze domestiche, attività commerciali e artigiane, uffici",
        "weekCalendar":[
            {
              "day": 1,
              "type": "ORGANICO"
            },
            {
              "day": 2,
              "type": "CARTA"
            }
          ]
      }
    ]
}
```

### Built With
Ecobot is built with:
* Telegram Api
* Java
* Maven

## Getting Started

Follow the instructions for setting up the project locally.

### Prerequisites
* Java
* Maven

### Installation

1. Clone the repo
```sh
git clone https://github.com/veronicadev/ecobot.git
```
3. Install Maven packages
```sh
mvn install
```
4. Create the ```.env ``` file in the main folder of the project and enter your API
```.env
BOT_TOKEN=<YOUR_BOT_TOKEN>

```


## Features

## Roadmap

See the [open issues](https://github.com/veronicadev/ecobot/issues) for a list of proposed features (and known issues).



## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


## Contact

Project Link: [https://github.com/veronicadev/ecobot](https://github.com/veronicadev/ecobot)
