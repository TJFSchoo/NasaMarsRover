package com.s2032257.tomschoonbeek.nasamarsrover.data_access;

import com.s2032257.tomschoonbeek.nasamarsrover.domain.MarsRoverPhoto;

/**
 * Zorgt voor het gebruik van listeners binnen de classes MainActivity en ApiAsyncTask voor doorgeven gegevens. Methode staat uitgeschreven in MainActivity voor callback van ApiAsyncTask naar MainActivity, en toevoegen van foto's aan database die geïnstantieerd is in MainActivity.
 * Gekozen om hier een interface voor te gebruiken ivm toekomstig gebruik (program to an interface) en eventuele verschillende implementaties van methode. Had in principe ook class kunnen worden.
 */

public interface OnTaskCompleted {
    void onTaskCompleted(MarsRoverPhoto photo);
}
