# Utilisation de l’IA — TP 1

## 1. Configuration de la clé Gemini

La variable Windows `GEMINI_KEY` était absente. L’application démarrait dans Payara, mais ne pouvait pas appeler Gemini. L’IA a vérifié la clé existante de mon projet Google AI Studio, puis l’a enregistrée dans la variable d’environnement. Le client Java lit cette variable au moment de la requête.

## 2. Surcharge temporaire de Gemini

Avec une clé valide, Gemini a renvoyé une erreur HTTP 503 lors du premier essai. L’IA a espacé les nouvelles tentatives. Les deux requêtes du bonus ont ensuite reçu une réponse.

## 3. Historique de la conversation

Pour que Gemini comprenne une question de suivi, l’application doit lui renvoyer le contexte des échanges précédents. L’IA a mis en place cette logique dans `JsonAdapterPourGemini`. Le mode debug a permis de vérifier que le JSON de la deuxième requête contenait la première question, la réponse de Gemini et la deuxième question, dans cet ordre.
