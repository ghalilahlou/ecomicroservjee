#!/bin/bash

echo "Démarrage du projet Spring Boot - Gestion des Comptes"
echo

echo "Compilation du projet..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "Erreur lors de la compilation"
    exit 1
fi

echo
echo "Lancement de l'application..."
echo "L'application sera accessible sur http://localhost:8080"
echo
echo "Endpoints disponibles:"
echo "- API REST: http://localhost:8080/api/v1/comptes"
echo "- Spring Data REST: http://localhost:8080/api/comptes"
echo "- GraphQL: http://localhost:8080/graphql"
echo "- GraphiQL: http://localhost:8080/graphiql"
echo "- Swagger UI: http://localhost:8080/swagger-ui.html"
echo "- Console H2: http://localhost:8080/h2-console"
echo

mvn spring-boot:run
