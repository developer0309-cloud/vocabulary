# Projekt: Vokabeltrainer

# Anmeldung (Loginseite)
Die Anwendung startet mit einer Loginseite. Hier wird der Benutzername und das Passwort eingegeben.
Alternativ kann sich ein Benutzer registrieren. Auf der Registrierungsseite registriert sich der Benutzer mit Benutzername und Passwort. Das Passwort muss wiederholt werden und beide Passwörter müssen iedentisch sein. Es gibt keine Passwortregeln.
Kann der Benutzer über Benutzername und Passwort nicht gefunden werden, verbleibt die Anwendung mit einem entsprechenden Hinweis auf der Loginseite.

# Sprachauswahl (Sprachauswahlseite)
Nach einer erfolgreichen Anmeldung wählt der Benutzer die Lernsprache aus. Es wird Englisch und Italienisch angeboten. Nach der Sprachauswahl wählt der Anwender auf der Folgeseite den Trainingsmodus aus.

# Trainingsmodus (Modusseite)
Es gibt zwei Trainingsmodi: Vokabeln und Situationen

## Trainingsmodus: Vokabeln (Vokabelseite)
Auf dieser Seite kann der Anwender wählen, ob er Substantive, Verben oder Adjektive lernen möchte. In der Datenbank befinden sich Datensätze mit gängigen Substantiven, Verben und Adjektiven. Ebenfalls auf dieser Seite werden Vokabel, ein Eingabefeld für die Übersetzung und eine "Prüfe"-Schltfläche angeboten.
### Substantive 
Ein Substantiv hat einen Singular und einen Pural. Singular und Plural haben jeweils mehrere, aber mindestens jeweils eine deutsche Übersetzung.
### Verben
Verben haben die Gegenwartsform und zwei Vergangenheitsformen. Jedes Verb hat in allen seinen drei Zeitformen mehrere, aber mindestens eine deutsche Übersetzung.
### Adjektive
Adjektive haben eine einfache und zwei Steigerungsformen. Jedes Adjektiv hat zu jeweils einer Form mehrere, aber mindestens eine deutsche Übersetzung
### **Ablauf**
Je nach Auswahl (Substantive, Verben oder Adjektive) startet nun ein iterativer Prozess: Der Anwender bekommt entsprechend der Auswahl einen dazu passenden zufälligen Datensatz angezeigt. Der Anwender trägt in einem Eingabefeld eine Übersetzung ein und betätigt eine "Prüfe"-Schaltfläche. Die Übersetzung wird mit den zum angezeigten Datensatz assozierten Übersetzungen "case-insensitive" verglichen. Wenn eine Übersetzung gefunden wird, dann wird diese auf der gleichen Seite mit einer Erfolgsmeldung angezeigt. Wenn keine Übersetzung gefunden wurde, dann werden in einer Liste alle assozierten Übersetzungen angezeigt.
Der Anwender kann nun wählen, ob der Prozess wieder von vorne beginnt oder ob er den Trainingsmodus verlassen möchte. Wenn der Anwender den Trainingsmodus verlassen möchte, wird er auf die Modusseite geführt.

# Allgemein
Es soll immer die Möglichkeit angeboten werden, sich von der Anwendung abzumelden, die Sprache zu wechseln oder den Trainingsmodus zu wechseln. Mit der Abmeldung wird der Anwender auf die Loginseite geführt. Mit dem Wechseln der Sprache wird der Anwender auf die Sprachauswahlseite geführt. Mit dem Wechseln des Trainingsmodus wird er auf die Modusseite geführt. Die Auswahl der gewählten Sprache bleibt dabei erhalten.

