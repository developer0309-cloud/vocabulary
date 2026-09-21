# Storyboard

## Startseite
Auf der Startseite trägt der Anwender seinen obligatorischen Benutzernamen ein. Er dient nur der persönlichen Ansprache und wird nicht persistiert und wird nur bis zum Erreichen der Endeseite aufbewahrt. Nach der Anmeldung erfolgt die Weiterleitung auf die Seite zur Sprach- und Kontextauswahl.

## Seite zur Sprach- und Kontextauswahl
Hier erscheint eine Begrüßungsmeldung mit dem angegebenen Benutzernamen. Auf dieser Seite erfolgt die obligatorische Auswahl der Vokabelsprache und eines Kontext. Nach der Auswahl von Vokabelsprache und Kontext erfolgt die Weiterleitung auf die Lernseite.

## Lernseite
Hier findet ein sich wiederholender Prozess statt: gemäß der Auswahl von Vokabelsprache und Kontext wird aus Datenbank eine zufällige Vokabel gelesen, die gemäß Vokabelsprache und Kontext mit der Auswahl aus der Sprach- und Kontextauswahl übereinstimmt. Die Form ist ebenfalls zufällig.
Wird keine Vokabel gefunden, erfolgt die Weiterleitung auf die Seite "Keine Vokabeln vorhanden".
Die Vokabel wird gemeinsam mit ihrer zugeordneten Form angezeigt. In einem Eingabefeld kann der Anwender eine obligatorische Übersetzung eingeben.
Auf der Seite ist eine Schaltfläche "Anzeigen". Nach Betätigung dieser Schaltfläche werden alle assoziierten Vokabeln als Aufzählung angezeigt.
Die Übersetzung wird mit dem Text aller assoziierten Vokabeln "case-insensitiv" verglichen. Gibt es einen positiven Vergleich, wird eine Meldung "Korrekte Übersetzung" angezeigt. Andernfalls wird eine Meldung "Leider falsch" angezeigt.
Am Ende der Auflistung ist eine Schaltfläche "Neue Vokabel". Nach Betätigung dieser Schaltfläche wird der Prozess von vorne wiederholt.

## Endeseite
Auf dieser erfolgt die Abmeldung und es erscheint eine Verabschiedungsmeldung mit dem angegebenen Benutzernamen. Der Benutzername wird nach dem Aufbau der Seite verworfen. Hier befindet sich eine Schaltfläche "Zurück zum Start". Nach Betätigung dieser Schaltfläche erfolgt die Weiterleitung auf die Startseite.

## Fehlerseiten
### Keine Vokabeln vorhanden
Hier erscheint eine Meldung mit dem Hinweis, dass zur ausgewählten Sprache und zum ausgewählten Kontext keine Vokabeln gefunden werden konnten. Hier befindet sich auch eine Schaltfläche "Zur Sprach- und Kontextauswahl". Nach Betätigung dieser Schaltfläche erfolgt die Weiterleitung zur Sprach- und Kontextauswahl. 

## Allgemein
Außer auf der Startseite und der Endeseite soll es auf allen Seiten die Möglichkeit geben, auf die Seite zur Sprach- und Kontextauswahl und zur Endeseite weitergeleitet zu werden.

## Routing
### Seite zur Sprach- und Kontextauswahl
Diese Seite ist nur erreichbar, wenn ein Benutzername vorliegt.
### Lernseite
Diese Seite kann nur erreicht werden, wenn ein Benutzername vorliegt und eine Vokabelsprache und ein Kontext ausgewählt wurde.
### Endeseite
Diese Seite kann nur erreicht werden, wenn ein Benutzername vorliegt.
### Fehlerseiten
- Keine Vokabeln vorhanden. Diese Seite kann nur erreicht werden, wenn ein Benutzername vorliegt und eine Vokabelsprache und ein Kontext ausgewählt wurde.
### Default/ Failed guard
Wenn eine Seite nicht erreicht werden kann, wird die Startseite angezeigt
