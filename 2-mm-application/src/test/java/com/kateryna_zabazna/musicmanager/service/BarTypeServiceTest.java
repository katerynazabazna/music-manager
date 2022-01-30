package com.kateryna_zabazna.musicmanager.service;

import com.kateryna_zabazna.musicmanager.application.service.BarTypeService;
import com.kateryna_zabazna.musicmanager.application.service.impl.BarTypeServiceImpl;
import com.kateryna_zabazna.musicmanager.domain.bartype.BarType;
import com.kateryna_zabazna.musicmanager.domain.bartype.BarTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BarTypeServiceTest {

    @Mock
    private BarTypeRepository barTypeRepository;

    @BeforeEach
    void prepareTests() {
        MockitoAnnotations.openMocks(this);

        // BarTypeRepository findBarTypeById
        BarType mockBarType = new BarType(0, 5, 6);
        when(barTypeRepository.findBarTypeById(0)).thenReturn(Optional.of(mockBarType));

        // BarTypeRepository findAllBarTypes
        List<BarType> mockBarTypes = List.of(
                mockBarType,
                new BarType(1, 3, 4),
                new BarType(2, 4, 4)
        );
        when(barTypeRepository.findAllBarTypes()).thenReturn(mockBarTypes);
    }

    @Test
    void getAllBarTypes() {
        // Test data
        AtomicInteger changeCounter = new AtomicInteger();
        BarTypeService barTypeService = new BarTypeServiceImpl(barTypeRepository, barTypeList -> changeCounter.getAndIncrement());

        // Execute
        List<BarType> actualResult = barTypeService.getAllBarTypes();

        // Assert
        List<BarType> expectedResult = List.of(
                new BarType(0, 5, 6),
                new BarType(1, 3, 4),
                new BarType(2, 4, 4)
        );
        assertEquals(expectedResult, actualResult);
        assertEquals(1, changeCounter.get());
    }

    @Test
    void createBarType() {
        // Test data
        AtomicInteger changeCounter = new AtomicInteger();
        BarTypeService barTypeService = new BarTypeServiceImpl(barTypeRepository, barTypeList -> changeCounter.getAndIncrement());

        // Execute
        BarType newBarType = new BarType(3, 17, 8);
        barTypeService.createBarType(newBarType);

        // Assert
        assertEquals(2, changeCounter.get());
    }
}
