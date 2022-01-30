package com.kateryna_zabazna.musicmanager.application.service;

import com.kateryna_zabazna.musicmanager.domain.bartype.BarType;

import java.util.List;

public interface BarTypeService {

    List<BarType> getAllBarTypes();

    void createBarType(BarType barType);

}
