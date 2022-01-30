package com.kateryna_zabazna.musicmanager.plugin.repository;

import com.kateryna_zabazna.musicmanager.domain.AbstractRepository;
import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.artist.ArtistRepository;
import com.kateryna_zabazna.musicmanager.domain.exception.TransitiveDataException;
import com.kateryna_zabazna.musicmanager.domain.relation.RelSongArtist;
import com.kateryna_zabazna.musicmanager.domain.relation.RelSongArtistRepository;
import com.kateryna_zabazna.musicmanager.utils.CSVHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RelSongArtistRepositoryImpl extends AbstractRepository implements RelSongArtistRepository {

    private static final String FILE_PATH = "./data/rel_songs_artists.csv";
    private final CSVHelper csvHelper;
    private List<RelSongArtist> relSongArtists = new ArrayList<>();
    private final ArtistRepository artistRepository;

    public RelSongArtistRepositoryImpl(ArtistRepository artistRepository) {
        this(artistRepository, new CSVHelper(FILE_PATH, ";"));
    }

    public RelSongArtistRepositoryImpl(ArtistRepository artistRepository, CSVHelper csvHelper) {
        this.csvHelper = csvHelper;
        this.artistRepository = artistRepository;
        // Pre-fetch all bar types at once
        reload();
    }

    @Override
    public List<Artist> findAllArtistsBySongId(long songId) {
        List<RelSongArtist> relevantRelations = relSongArtists.stream()
                .filter(relation -> relation.getSongId() == songId)
                .collect(Collectors.toList());
        return relevantRelations.stream()
                .map(relation -> {
                    Optional<Artist> optionalArtist = artistRepository.findArtistById(relation.getArtistId());
                    return optionalArtist.orElseThrow(() -> new TransitiveDataException("Cannot load artist from relation"));
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateRelations(List<RelSongArtist> relations) {
        relSongArtists = relations;
        writeOut();
    }

    @Override
    protected void writeOut() {
        List<String[]> serializedRelations = relSongArtists.stream()
                .map(RelSongArtist::getFieldContents)
                .collect(Collectors.toList());
        csvHelper.write(RelSongArtist.getCSVHeader(), serializedRelations);
    }

    @Override
    protected void reload() {
        relSongArtists.clear();

        // Load all relations
        Optional<List<String[]>> serializedRelations = csvHelper.read();
        serializedRelations.ifPresent(strings -> strings.forEach(serializedRelation -> {
            // Get basic fields
            long relationId = Long.parseLong(serializedRelation[0]);
            long songId = Long.parseLong(serializedRelation[1]);
            long artistId = Long.parseLong(serializedRelation[2]);

            // Create relation object
            relSongArtists.add(new RelSongArtist(relationId, songId, artistId));
        }));
    }
}
