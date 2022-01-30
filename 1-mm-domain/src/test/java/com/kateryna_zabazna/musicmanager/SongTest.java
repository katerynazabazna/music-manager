package com.kateryna_zabazna.musicmanager;

import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.genre.Genre;
import com.kateryna_zabazna.musicmanager.domain.song.Song;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SongTest {

    @Test
    void getFieldContents() {
        // Test data
        List<Artist> artists = List.of(new Artist(4, "Powerwolf", "", new Date(0)));
        Genre genre = new Genre(65, "Power Metal");
        com.kateryna_zabazna.musicmanager.domain.bartype.BarType barType = new com.kateryna_zabazna.musicmanager.domain.bartype.BarType(0, 4, 4);
        Song testSong = new Song(9, 12, "Demons Are A Girl's Best Friend", artists, genre, 143, barType);

        // Execute
        String[] actualResult = testSong.getFieldContents();

        // Assert
        String[] expectedResult = new String[]{"9", "12", "Demons Are A Girl's Best Friend", "65", "143.0", "0"};
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    void getCSVHeader() {
        // Execute
        String[] actualResult = Song.getCSVHeader();

        // Assert
        String[] expectedResult = new String[]{"Id", "UserId", "Title", "GenreId", "Bpm", "BarTypeId"};
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    void toStringNormal() {
        // Test data
        List<Artist> artists = List.of(new Artist(1, "Arch Enemy", "", new Date(0)));
        Genre genre = new Genre(61, "Melodic Death Metal");
        com.kateryna_zabazna.musicmanager.domain.bartype.BarType barType = new com.kateryna_zabazna.musicmanager.domain.bartype.BarType(0, 4, 4);
        Song testSong = new Song(13, 0, "Avalanche", artists, genre, 187, barType);

        // Execute
        String actualResult = testSong.toString();

        // Assert
        assertEquals("Avalanche", actualResult);
    }
}
