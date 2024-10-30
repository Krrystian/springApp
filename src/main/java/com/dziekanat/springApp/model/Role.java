package com.dziekanat.springApp.model;

public enum Role {
    STUDENT,
    PRACOWNIK,
    ADMIN
}

/*
    STUDENT -> SZARY CZŁOWIEK
    PRACOWNIK -> wykładowca, gość od ćwiczeń i laborek
        * Stanowisko
        * Wydział
        * Id
        * Tytuł naukowy
    ADMIN -> pracownik dziekanatu, dziekan, i inni dziekanopodobni
        * Id
        * Stanowisko
 */