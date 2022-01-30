package com.kateryna_zabazna.musicmanager.application.observer;

import com.kateryna_zabazna.musicmanager.domain.bartype.BarType;

import java.util.List;

public interface BarTypeListObserver {
    void onBarTypeListChanged(List<BarType> barTypeList);
}
