package com.kateryna_zabazna.musicmanager.service;

import com.kateryna_zabazna.musicmanager.application.service.YTLinkGeneratorService;
import com.kateryna_zabazna.musicmanager.application.service.impl.YTLinkGeneratorServiceImpl;
import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.bartype.BarType;
import com.kateryna_zabazna.musicmanager.domain.genre.Genre;
import com.kateryna_zabazna.musicmanager.domain.song.Song;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YTLinkGeneratorServiceTest {

    @Test
    void generateYouTubeLink() {
        // Test data
        YTLinkGeneratorService linkGeneratorService = new YTLinkGeneratorServiceImpl();
        List<Artist> artists = List.of(new Artist(0, "Slipknot", "", new Date(0)));
        Genre genre = new Genre(0, "Metal");
        BarType barType = new BarType(0, 4, 4);
        Song song = new Song(12, 3, "Psychosocial", artists, genre, 135, barType);

        // Execute
        String actualResult = linkGeneratorService.generateYouTubeLink(song);

        // Assert
        assertEquals("https://www.youtube.com/results?search_query=Slipknot+Psychosocial", actualResult);
    }
}
