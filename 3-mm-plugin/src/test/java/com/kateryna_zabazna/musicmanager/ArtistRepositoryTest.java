package com.kateryna_zabazna.musicmanager;

import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.artist.ArtistRepository;
import com.kateryna_zabazna.musicmanager.plugin.repository.ArtistRepositoryImpl;
import com.kateryna_zabazna.musicmanager.utils.CSVHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.kateryna_zabazna.musicmanager.domain.AbstractRepository.AUTO_INC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ArtistRepositoryTest {

    @Mock
    private CSVHelper csvHelper;

    @BeforeEach
    void prepareTest() {
        MockitoAnnotations.openMocks(this);

        // CSVHelper read()
        List<String[]> mockedReadResult = List.of(
                new String[]{"0", "Arch Enemy", "", "0"},
                new String[]{"1", "Amorphis", "", "0"},
                new String[]{"2", "Amaranthe", "", "0"}
        );
        when(csvHelper.read()).thenReturn(Optional.of(mockedReadResult));

        // CSVHelper write()
        doNothing().when(csvHelper).write(any(String[].class), any());
    }

    @Test
    void findArtistById() {
        // Test data
        ArtistRepository artistRepository = new ArtistRepositoryImpl(csvHelper);

        // Execute
        Optional<Artist> optionalArtist = artistRepository.findArtistById(2);

        // Verify
        verify(csvHelper, times(1)).read();

        // Assert
        assertTrue(optionalArtist.isPresent());
        Artist artist = optionalArtist.get();
        assertEquals("Amaranthe", artist.getFirstName());
    }

    @Test
    void findAllArtists() {
        // Test data
        ArtistRepository artistRepository = new ArtistRepositoryImpl(csvHelper);

        // Execute
        List<Artist> artists = artistRepository.findAllArtists();

        // Verify
        verify(csvHelper, times(1)).read();

        // Assert
        assertEquals(3, artists.size());
        assertEquals("Amorphis", artists.get(1).getFirstName());
    }

    @Test
    void save() {
        // Test data
        ArtistRepository artistRepository = new ArtistRepositoryImpl(csvHelper);
        Artist newArtist = new Artist(AUTO_INC, "Northlane", "", new Date(0));

        // Execute
        artistRepository.save(newArtist);

        // Verify
        verify(csvHelper, times(2)).read();
        verify(csvHelper, times(1)).write(any(), any());

        // Assert
        assertEquals(4, artistRepository.findAllArtists().size());
    }

    @Test
    void delete() {
        // Test data
        ArtistRepository artistRepository = new ArtistRepositoryImpl(csvHelper);

        // Execute
        artistRepository.delete(1);

        // Verify
        verify(csvHelper, times(1)).read();
        verify(csvHelper, times(1)).write(any(), any());

        // Assert
        assertEquals(2, artistRepository.findAllArtists().size());
    }
}
