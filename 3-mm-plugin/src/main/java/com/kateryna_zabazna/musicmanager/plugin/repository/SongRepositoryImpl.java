package com.kateryna_zabazna.musicmanager.plugin.repository;

import com.kateryna_zabazna.musicmanager.domain.AbstractRepository;
import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.bartype.BarTypeRepository;
import com.kateryna_zabazna.musicmanager.domain.exception.TransitiveDataException;
import com.kateryna_zabazna.musicmanager.domain.genre.Genre;
import com.kateryna_zabazna.musicmanager.domain.genre.GenreRepository;
import com.kateryna_zabazna.musicmanager.domain.relation.RelSongArtist;
import com.kateryna_zabazna.musicmanager.domain.relation.RelSongArtistRepository;
import com.kateryna_zabazna.musicmanager.domain.song.Song;
import com.kateryna_zabazna.musicmanager.domain.song.SongRepository;
import com.kateryna_zabazna.musicmanager.utils.CSVHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SongRepositoryImpl extends AbstractRepository implements SongRepository {

    private static final String FILE_PATH = "./data/songs.csv";
    private final CSVHelper csvHelper;
    private final List<Song> songs = new ArrayList<>();
    private final GenreRepository genreRepository;
    private final BarTypeRepository barTypeRepository;
    private final RelSongArtistRepository relSongArtistRepository;

    public SongRepositoryImpl() {
        this(
                new CSVHelper(FILE_PATH, ";"),
                new GenreRepositoryImpl(),
                new BarTypeRepositoryImpl(),
                new RelSongArtistRepositoryImpl(new ArtistRepositoryImpl())
        );
    }

    public SongRepositoryImpl(CSVHelper csvHelper, GenreRepository genreRepository,
                              BarTypeRepository barTypeRepository, RelSongArtistRepository relSongArtistRepository) {
        this.csvHelper = csvHelper;
        this.genreRepository = genreRepository;
        this.barTypeRepository = barTypeRepository;
        this.relSongArtistRepository = relSongArtistRepository;
        // Pre-fetch all songs at once
        reload();
    }

    @Override
    public Optional<Song> findSongById(long id) {
        return songs.stream()
                .filter(song -> song.getId() == id)
                .findFirst();
    }

    @Override
    public List<Song> findAllSongs() {
        return songs;
    }

    @Override
    public List<Song> findAllSongsByUserId(long userId) {
        return songs.stream()
                .filter(song -> song.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Song song) {
        // Load songs once again to reflect any potential changes
        reload();

        // Consider auto-increment ids
        if (song.getId() == AUTO_INC) {
            song.setId(songs.size());
        }

        // Save
        songs.add(song);

        // Save song list
        writeOut();
    }

    @Override
    public void update(Song song) {
        // Search for item with this particular song id and get its index
        OptionalInt indexOpt = IntStream.range(0, songs.size())
                .filter(i -> songs.get(i).getId() == song.getId())
                .findFirst();

        if (indexOpt.isPresent()) {
            // Update the item at this index
            songs.set(indexOpt.getAsInt(), song);
            writeOut();
        }
    }

    @Override
    public void delete(long id) {
        songs.removeIf(song -> song.getId() == id);
        writeOut();
    }

    @Override
    protected void writeOut() {
        // Write relations out
        List<RelSongArtist> relations = new ArrayList<>();
        songs.forEach(song ->
                song.getArtists().forEach(artist ->
                        relations.add(new RelSongArtist(relations.size(), song.getId(), artist.getId()))
                )
        );
        relSongArtistRepository.updateRelations(relations);

        // Write songs out
        List<String[]> serializedSongs = songs.stream()
                .map(Song::getFieldContents)
                .collect(Collectors.toList());
        csvHelper.write(Song.getCSVHeader(), serializedSongs);
    }

    @Override
    protected void reload() {
        songs.clear();

        // Load all songs
        Optional<List<String[]>> serializedSongs = csvHelper.read();
        serializedSongs.ifPresent(strings -> strings.forEach(serializedSong -> {
            // Get basic fields
            long songId = Long.parseLong(serializedSong[0]);
            long userId = Long.parseLong(serializedSong[1]);
            String title = serializedSong[2];
            float bom = Float.parseFloat(serializedSong[4]);

            // Load transitive artists
            List<Artist> artists = relSongArtistRepository.findAllArtistsBySongId(songId);

            // Load transitive genre
            long genreId = Long.parseLong(serializedSong[3]);
            Genre genre = null;
            if (genreId != -1) {
                Optional<Genre> optionalGenre = genreRepository.findGenreById(genreId);
                genre = optionalGenre.orElseThrow(() -> new TransitiveDataException("Genre not found"));
            }

            // Load transitive bar type
            long barTypeId = Long.parseLong(serializedSong[5]);
            com.kateryna_zabazna.musicmanager.domain.bartype.BarType barType = null;
            if (barTypeId != -1) {
                Optional<com.kateryna_zabazna.musicmanager.domain.bartype.BarType> optionalBarType = barTypeRepository.findBarTypeById(barTypeId);
                barType = optionalBarType.orElseThrow(() -> new TransitiveDataException("Bar type not found"));
            }

            // Create song object
            songs.add(new Song(songId, userId, title, artists, genre, bom, barType));
        }));
    }
}
