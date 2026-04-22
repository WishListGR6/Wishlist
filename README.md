# Wishlist

En webapplikation der giver brugere mulighed for at oprette og dele ønskelister med familie og venner.

## Features

- Opret ønskelister med ejer- og gæsteadgang
- Tilføj ønsker med navn, beskrivelse, pris og produktlink
- Organiser ønsker i begivenheder (f.eks. jul, fødselsdag)
- Reserver ønsker som gæst, så dubletter undgås
- Adgangstokens til deling af ønskelister via link
- Separat login for ejere og gæster

## Teknologier

| Teknologi      | Version |
|----------------|---------|
| Java           | 21      |
| Spring Boot    | 4.0.5   |
| Thymeleaf      | -       |
| MySQL          | -       |
| Bootstrap      | 5.0.2   |
| Maven          | -       |

## Installation

### Forudsætninger

- Java 21
- Maven
- MySQL-database

### Klargøring af database

```sql
CREATE DATABASE WishDB CHARACTER SET utf8mb4;
```

Kør derefter `src/main/resources/sql/schema.sql` og `src/main/resources/sql/data.sql` for at oprette tabeller og testdata.

### Konfiguration

Sæt følgende miljøvariabler:

```
dev_db_host=jdbc:mysql://localhost:3306/WishDB
dev_db_username=<dit-brugernavn>
dev_db_password=<dit-kodeord>
```

### Start applikationen

```bash
git clone https://github.com/WishListGR6/Wishlist.git
cd Wishlist
mvn spring-boot:run
```

## Brug

Åbn `http://localhost:8080` i din browser.

**Testdata (fra data.sql):**
- Liste-ID: `abcd1234`
- Ejerkodeord: `o1234`
- Gæstekodeord: `g1234`

Log ind som ejer for at tilføje og redigere ønsker. Log ind som gæst for at se og reservere ønsker.

## Bidrag

Se [CONTRIBUTING.md](CONTRIBUTING.md) for retningslinjer om, hvordan du bidrager til projektet.
