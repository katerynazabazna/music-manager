package com.kateryna_zabazna.musicmanager.service;

import com.kateryna_zabazna.musicmanager.application.service.ArtistService;
import com.kateryna_zabazna.musicmanager.application.service.impl.ArtistServiceImpl;
import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.artist.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @BeforeEach
    void prepareTests() {
        MockitoAnnotations.openMocks(this);

        // ArtistRepository findArtistById
        Artist mockArtist = new Artist(1, "In Flames", "", new Date(0));
        when(artistRepository.findArtistById(1)).thenReturn(Optional.of(mockArtist));

        // ArtistRepository findAllArtists
        List<Artist> mockArtists = List.of(
                mockArtist,
                new Artist(2, "The Dark Side of the Moon", "", new Date(0)),
                new Artist(3, "Nightwish", "", new Date(0))
        );
        when(artistRepository.findAllArtists()).thenReturn(mockArtists);
    }

    @Test
    void getAllArtists() {
        // Test data
        AtomicInteger changeCounter = new AtomicInteger();
        ArtistService artistService = new ArtistServiceImpl(artistRepository, artistList -> changeCounter.getAndIncrement());

        // Execute
        List<Artist> actualResult = artistService.getAllArtists();

        // Assert
        List<Artist> expectedResult = List.of(
                new Artist(1, "In Flames", "", new Date(0)),
                new Artist(2, "The Dark Side of the Moon", "", new Date(0)),
                new Artist(3, "Nightwish", "", new Date(0))
        );
        assertEquals(expectedResult, actualResult);
        assertEquals(1, changeCounter.get());
    }

    @Test
    void createArtist() {
        // Test data
        AtomicInteger changeCounter = new AtomicInteger();
        ArtistService artistService = new ArtistServiceImpl(artistRepository, artistList -> changeCounter.getAndIncrement());

        // Execute
        Artist newArtist = new Artist(4, "Max", "Mustermann", new Date(1234567890L));
        artistService.createArtist(newArtist);

        // Assert
        assertEquals(2, changeCounter.get());
    }
}
