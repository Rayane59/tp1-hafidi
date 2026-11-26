# Utilisation de l’IA — TP 1

## 1. Configuration de la clé Gemini

- **Problème rencontré** — La variable Windows `GEMINI_KEY` était absente. L’application démarrait dans Payara, mais ne pouvait pas appeler Gemini.
- **Raison de l’utilisation de l’IA** — J’ai demandé de vérifier la configuration de la clé pour pouvoir lancer les essais depuis l’application.
- **Prompt utilisé** — « Aide moi a regle le probleme de Gemini Key», en réponse au message qui indiquait que les essais nécessitaient `GEMINI_KEY`.
- **Réponse de l’IA** — « J’ai trouvé une clé existante sur ton projet Google sans frais, je l’ai vérifiée et configurée sur Windows. » L’IA a vérifié la clé du projet Google AI Studio et l’a enregistrée dans la variable d’environnement.
- **Ce que j’ai appris** — Payara peut démarrer même si la clé manque. Le client Java a besoin de la variable `GEMINI_KEY` pour appeler Gemini sans écrire la clé dans le code.

## 2. Surcharge temporaire de Gemini

- **Problème rencontré** — Avec une clé valide, Gemini a renvoyé une erreur HTTP 503 lors du premier essai.
- **Raison de l’utilisation de l’IA** — Le 503 est apparu pendant les essais que j’avais demandé à l’IA de lancer. Elle est intervenue pour reprendre les essais après cette erreur.
- **Prompt utilisé** — « donne moi une solution pour cette erreur  ». C’est la même demande que pour la configuration de la clé.
- **Réponse de l’IA** — « Gemini a renvoyé une erreur 503 de surcharge au premier essai. Je vais réessayer. » L’IA a espacé les nouvelles tentatives. Les deux questions du bonus ont ensuite reçu une réponse.
- **Ce que j’ai appris** — Dans cet essai, le 503 signalait que Gemini était temporairement surchargé. Attendre un peu entre les tentatives a permis de terminer le test.

## 3. Historique de la conversation

- **Problème rencontré** — Une question de suivi dépend de ce qui a été dit avant. Il fallait conserver les échanges et les renvoyer à Gemini.
- **Raison de l’utilisation de l’IA** — J’ai demandé à l’IA de réaliser l’intégration Gemini et de vérifier la conservation du contexte avec le panneau de débogage.
- **Prompts utilisés** — « j ai pas compris aide moi a comprendre et en plus la solution  »
- **Réponse de l’IA** — L’IA a mis en place l’historique dans `JsonAdapterPourGemini`. Après les essais, elle a indiqué : « Les deux réponses Gemini ont été obtenues et le JSON du deuxième échange contient bien l’historique. »
- **Ce que j’ai appris** — L’API ne garde pas la conversation entre les appels. Dans le JSON de la deuxième requête, j’ai donc la première question, la réponse de Gemini et la deuxième question, dans cet ordre.
