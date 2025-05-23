package com.kateryna_zabazna.musicmanager.service;

import com.kateryna_zabazna.musicmanager.application.service.SongService;
import com.kateryna_zabazna.musicmanager.application.service.impl.SongServiceImpl;
import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.bartype.BarType;
import com.kateryna_zabazna.musicmanager.domain.genre.Genre;
import com.kateryna_zabazna.musicmanager.domain.song.Song;
import com.kateryna_zabazna.musicmanager.domain.song.SongRepository;
import com.kateryna_zabazna.musicmanager.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.kateryna_zabazna.musicmanager.domain.AbstractRepository.AUTO_INC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SongServiceTest {

    @Mock
    private User user;
    @Mock
    private SongRepository songRepository;

    @BeforeEach
    void prepareTests() {
        MockitoAnnotations.openMocks(this);

        // User getId()
        when(user.getId()).thenReturn(3L);

        // SongRepository findGenreById
        List<Artist> artists = List.of(new Artist(0, "Slipknot", "", new Date(0)));
        Genre genre = new Genre(0, "Metal");
        BarType barType = new BarType(0, 4, 4);
        Song mockSong = new Song(12, 3, "Unsainted", artists, genre, 101, barType);
        when(songRepository.findSongById(0)).thenReturn(Optional.of(mockSong));

        // SongRepository findAllSongsByUserId
        List<Song> mockSongs = List.of(mockSong);
        when(songRepository.findAllSongsByUserId(3)).thenReturn(mockSongs);

        // SongRepository create()
        doNothing().when(songRepository).save(any(Song.class));

        // SongRepository update()
        doNothing().when(songRepository).update(any(Song.class));

        // SongRepository delete()
        doNothing().when(songRepository).delete(anyLong());
    }

    @Test
    void getAllSongsForUser() {
        // Test data
        AtomicInteger changeCounter = new AtomicInteger();
        SongService songService = new SongServiceImpl(user, songRepository, songList -> changeCounter.getAndIncrement());

        // Execute
        List<Song> actualResult = songService.getAllSongsForUser();

        // Assert
        List<Artist> artists = List.of(new Artist(0, "Slipknot", "", new Date(0)));
        Genre genre = new Genre(0, "Metal");
        BarType barType = new BarType(0, 4, 4);
        Song expectedSong = new Song(12, 3, "Unsainted", artists, genre, 101, barType);
        assertEquals(List.of(expectedSong), actualResult);
        assertEquals(1, changeCounter.get());
    }

    @Test
    void searchSongsByTitle() {
        // ToDo
    }

    @Test
    void createSong() {
        // Test data
        AtomicInteger changeCounter = new AtomicInteger();
        SongService songService = new SongServiceImpl(user, songRepository, songList -> changeCounter.getAndIncrement());
        List<Artist> artists = List.of(new Artist(1, "Arch Enemy", "", new Date(0)));
        Genre genre = new Genre(0, "Melodic Death Metal");
        BarType barType = new BarType(0, 4, 4);
        Song newSong = new Song(AUTO_INC, 3, "No Gods, No Masters", artists, genre, 124, barType);

        // Execute
        songService.create(newSong);

        // Verify
        verify(songRepository, times(1)).save(newSong);

        // Assert
        assertEquals(2, changeCounter.get());
    }

    @Test
    void updateSong() {
        // Test data
        AtomicInteger changeCounter = new AtomicInteger();
        SongService songService = new SongServiceImpl(user, songRepository, songList -> changeCounter.getAndIncrement());
        List<Artist> artists = List.of(new Artist(0, "Slipknot", "", new Date(0)));
        Genre genre = new Genre(0, "Metal");
        BarType barType = new BarType(0, 4, 4);
        Song updatedSong = new Song(12, 3, "Psychosocial", artists, genre, 135, barType);

        // Execute
        songService.update(updatedSong);

        // Verify
        verify(songRepository, times(1)).update(updatedSong);

        // Assert
        assertEquals(2, changeCounter.get());
    }

    @Test
    void deleteSong() {
        // Test data
        AtomicInteger changeCounter = new AtomicInteger();
        SongService songService = new SongServiceImpl(user, songRepository, songList -> changeCounter.getAndIncrement());
        List<Artist> artists = List.of(new Artist(0, "Slipknot", "", new Date(0)));
        Genre genre = new Genre(0, "Metal");
        BarType barType = new BarType(0, 4, 4);
        Song deleteSong = new Song(12, 3, "Unsainted", artists, genre, 101, barType);

        // Execute
        songService.delete(deleteSong);

        // Verify
        verify(songRepository, times(1)).delete(12);
    }
}
