# Projekt: Vokabeltrainer

# Domänenmodell

Aggregatwurzel ist die **Vokabel**. Alle Vokabeln sind **disjunkt**: jede Instanz gehört zu genau einem Kontext, und die Identität `(Text, Sprache, Kontext, Ausprägung)` kommt höchstens einmal vor. Der Vergleich des Texts ist **case-insensitive** (`Haus` und `haus` sind dieselbe Vokabel). Derselbe Text in anderem Kontext oder anderer Ausprägung ist eine andere Vokabel.

## Vokabel
- **Text**: Zeichenkette, nicht leer und nicht nur Leerzeichen, höchstens 255 Zeichen. Eindeutigkeit case-insensitive.
- **Sprache**: genau eine
- **Kontext**: genau einer
- **Ausprägung**: genau eine, abhängig vom Kontext
- **Zuordnung**: jede englische Vokabel ist mindestens einer deutschen Vokabel zugeordnet; jede deutsche Vokabel ist mindestens einer englischen Vokabel zugeordnet. Das erzwingt die Datenbank: `required_association_id` ist `NOT NULL` und verweist auf eine Zuordnung. Zugeordnete Vokabeln haben denselben Kontext und dieselbe Ausprägung. Eine Vokabel kann mehreren Partnern der anderen Sprache zugeordnet sein (N:M). Es darf keine Vokabel ohne Partner geben.
- **Löschen**: Wird eine Vokabel gelöscht, werden ihre Zuordnungen mitgelöscht. Hat ein bisheriger Partner danach keine Zuordnung mehr, wird dieser Partner ebenfalls gelöscht (rekursiv, bis keine verwaiste Vokabel übrig ist).

## Sprache
- deutsch
- englisch

## Kontext
- Substantiv
- Verb
- Adjektiv

## Ausprägung

| Kontext | zulässige Ausprägungen |
|---|---|
| Substantiv | Singular, Plural |
| Verb | Gegenwart, 1. Vergangenheit, 2. Vergangenheit |
| Adjektiv | Grundform, 1. Steigerung, 2. Steigerung |
