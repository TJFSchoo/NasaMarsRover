package com.s2032257.tomschoonbeek.nasamarsrover.application_logic;

/**
 * Interface die geïmplementeerd wordt door de MainActivity.java om de methode onItemClick() te gebruiken .
 * Voor interface gekozen om te voorkomen dat classes verkeerde implementaties overnemen van andere classes, en voor toekomstig gebruik en aanpassingen (program to an interface).
 */

public interface OnItemClickListener {
    void onItemClick(int position);
}
