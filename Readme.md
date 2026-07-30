# TPBatch — Traitement des adresses BAN

TPBatch est une application Spring Boot (batch + API REST) pour importer, filtrer, dédupliquer et synchroniser des fichiers CSV d'adresses (format BAN) vers une base de données. Le projet fournit des profils pour PostgreSQL et SQLite, des schémas SQL d'initialisation et des jobs batch pour détecter les ajouts, suppressions et mises à jour.

Principales informations
- ArtifactId : `tpbatch`
- Version : `0.0.1-SNAPSHOT`
- Classe principale : `com.example.tpbatch.TpbatchApplication`

Prérequis
- Java 17+ (ou version définie dans `pom.xml`)
- Maven (ou les wrappers fournis : `mvnw` / `mvnw.cmd`)

Construction et exécution

1) Compiler

```powershell
mvnw.cmd -v; mvnw.cmd -q clean package
# ou sans wrapper
mvn -q clean package
```

2) Exécuter en développement

Avec le wrapper (Windows PowerShell) :

```powershell
mvnw.cmd spring-boot:run
```

Ou exécuter le jar produit :

```powershell
java -jar target/tpbatch-0.0.1-SNAPSHOT.jar
```

Profils et configuration

- Fichier principal : `src/main/resources/application.properties`
- Profil PostgreSQL : `src/main/resources/application-postgresql.properties` (profil par défaut)
- Profil SQLite : `src/main/resources/application-sqlite.properties`
- Schémas d'initialisation : `src/main/resources/schema-postgresql.sql` et `src/main/resources/schema-sqlite.sql`

Le profil actif par défaut (dans le dépôt) est `postgresql`. Pour forcer SQLite, lancez :

```powershell
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=sqlite
```

Bases de données et fichiers de données
- Fichier SQLite attendu (si vous utilisez le profil `sqlite`) : `data/ban.db`
- Fichiers CSV d'exemple : `data/adresses-79.csv`, `data/adresses-france.csv`, etc.

Utilisation (batch & arguments)

Le job batch traite des fichiers CSV et propose des options de filtrage via les arguments de démarrage. Exemples :

```powershell
# Traitement complet (sans filtre)
mvnw.cmd spring-boot:run

# Filtrer par département (ex : 79)
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="dept 79"

# Filtrer par code postal exact
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="postal 79000"

# Filtrer par code INSEE
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="insee 79007"
```

Remarque : l'application attend deux arguments pour un filtre (type + valeur) ; fournir une mauvaise combinaison peut provoquer une erreur d'exécution.

API REST

L'application expose des endpoints REST pour rechercher les adresses :

- Recherche simple : `GET /recherche?codePostal=79000&rue=Victor&commune=Niort`
- Recherche paginée : `GET /recherche/page?...&page=0&size=20&sort=nomCommune,asc`

La documentation OpenAPI (springdoc) est incluse. Après démarrage, ouvrez l'UI Swagger :

- Swagger UI (interface) : http://localhost:8080/swagger-ui.html  ou  http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON : http://localhost:8080/v3/api-docs

Exemple direct (copiez dans votre navigateur) :

```
http://localhost:8080/swagger-ui/index.html
```

Lancer le job via l'API REST (recommandé)

Le moyen principal pour déclencher le traitement batch est l'endpoint REST fourni par l'application :

- URL : POST /batch/lancer
- Paramètres (query string, optionnels) :
  - `typeCriteria` : type de filtre (`dept`, `postal`, `insee`, ...)
  - `criteria` : valeur du filtre (ex. `79`, `79000`, `79007`)

Exemples :

Curl (Linux/macOS) :

```bash
curl -X POST "http://localhost:8080/batch/lancer"
curl -X POST "http://localhost:8080/batch/lancer?typeCriteria=dept&criteria=79"
```

PowerShell (Windows) :

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/batch/lancer"
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/batch/lancer?typeCriteria=postal&criteria=79000"
```

Réponses et codes HTTP courants :
- 200 OK : job démarré (corps = statut de sortie du job ou message)
- 409 CONFLICT : job déjà terminé
- 423 LOCKED : job déjà en cours d'exécution
- 500 INTERNAL_SERVER_ERROR : erreur lors du lancement

Comportement important :
- Si la propriété `downloadFile` (fichier de configuration) est à `true`, le contrôleur lance d'abord un téléchargement (BAN puis DVF) puis exécute les jobs de traitement associés. Si `downloadFile` est `false`, le job de traitement s'exécute en utilisant le fichier local configuré (`application-*.properties`).
- Les rapports et logs de traitement sont générés dans le répertoire `data/reports/` et les fichiers temporaires dans `data/tmp/`.

Vérifiez les logs de l'application pour le détail de l'exécution et des erreurs.

Flux batch (résumé)

Le job exécute les étapes principales suivantes :
- Lecture et validation du CSV
- Détection et gestion des doublons
- Identification des enregistrements mis à jour, ajoutés ou supprimés
- Persistance et sauvegarde pour comparaison aux runs suivants

Schéma et initialisation

Les scripts d'initialisation se trouvent dans :
- `src/main/resources/schema-postgresql.sql`
- `src/main/resources/schema-sqlite.sql`

Tests et qualité

Exécuter les tests unitaires :

```powershell
mvnw.cmd test
```

Packaging

Créer le package exécutable :

```powershell
mvnw.cmd -DskipTests package
```

Fichiers utiles dans le dépôt

- `pom.xml` : dépendances et configuration Maven
- `mvnw`, `mvnw.cmd` : wrappers Maven
- `src/main/resources/application-*.properties` : profils
- `data/` : CSV et base de données (ex. `data/ban.db`)

Contact / contribution

Ouvrez une issue ou une PR sur le dépôt pour signaler un bug ou proposer une amélioration. Pour toute question, ajoutez des informations de contexte (ex. fichier CSV utilisé, profil, logs).

---
Cette documentation est générée automatiquement à partir du code du projet. Si vous souhaitez un README plus détaillé (exemples d'API complets, diagrammes ou instructions Docker), dites-le et je l'ajouterai.
