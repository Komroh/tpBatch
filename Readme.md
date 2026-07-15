# TPBatch - Traitement des Adresses BAN

## Description


L'application charge les données depuis des fichiers CSV, les filtre selon des critères personnalisés, détecte les doublons, et synchronise les données avec une base de données SQLite.

L'application permet aussi de détecter les lignes supprimées, ajoutés ou mis à jour


### API REST
- **Recherche simple** : Recherche d'adresses avec pagination
- **Critères multiples** : Filtrage par code postal, rue, commune
- **Pagination** : Support complet de la pagination et du tri

## Installation


### 2. Compiler le projet

```bash
mvn clean compile
```

### 3. Exécuter l'application

#### Sans paramètre (traitement complet)
```bash
mvn spring-boot:run
```

#### Avec filtrage par département
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="dept 79"
```

#### Avec filtrage par code postal
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="postal 79000"
```

#### Avec filtrage par code INSEE
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="insee 79007"
```

## Utilisation

### Paramètres de la ligne de commande

L'application accepte **optionnellement** 2 paramètres pour filtrer les données :

| Paramètre | Description | Exemple |
|-----------|-------------|---------|
| `dept` | Filtre par département (2 premiers chiffres du code postal) | `dept 79` |
| `postal` | Filtre par code postal exact | `postal 79000` |
| `insee` | Filtre par code INSEE exact | `insee 79007` |

**Note** : Si vous fournissez des paramètres, il faut exactement 2 arguments (le type et la valeur), sinon une exception sera levée.

## Configuration

Les paramètres de configuration sont dans `src/main/resources/application.properties` :

### Base de données
Modifier :

spring.datasource.url=jdbc:sqlite:./src/main/resources/data/ban.db

### Fichier CSV à traiter (par défaut)
file=data/adresses-79.csv

### Logging
logging.level.com.example.tpbatch=info


## API REST

#### Recherche simple
```http
GET /recherche?codePostal=79000&rue=Victor&commune=Niort
```

#### Recherche paginée
```http
GET /recherche/page?codePostal=79000&rue=Victor&commune=Niort&page=0&size=20&sort=nomCommune,asc
```

#### Documentation Swagger
Une fois l'application démarrée, accédez à :
```
http://localhost:8080/swagger-ui.html
```

## Flux de traitement Batch

Le job Batch exécute les étapes dans cet ordre :

1. **Init Insert Step** : 
   - Lecture du CSV
   - Validation et filtrage
   - Détection des doublons
   - Écriture en base de données

2. **Update Step** : Identification des mises à jour

3. **Added Step** : Identification des nouvelles adresses

4. **Deleted Step** : Identification des adresses suppressions

5. **Insert Step** : Insertion de la table courante dans une table backup pour pouvoir faire des comparaisons au prochain batch

##  Base de données

### Schéma
La base de données est initialisée via `schema-sqlite.sql` :
- Table **BAN** : Stockage des adresses
- Colonnes : id, id_fantoir, numero, nom_voie, code_postal, code_insee, etc.

### Accès
```
SQLite: ./src/main/resources/data/ban.db
```

