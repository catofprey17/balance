package ru.c17.balance.PeriodsScreen;

import android.view.View;

public interface StickyHeaderInterface {

    int getHeaderPositionForItem(int itemPos);

    // FIXME: 07.05.2020 
    int getHeaderLayout(int headerPos);
    void bindHeaderData(View header, int headerPos);
    boolean isHeader(int itemPos);

}
