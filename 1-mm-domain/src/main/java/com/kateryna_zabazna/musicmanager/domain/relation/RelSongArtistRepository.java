package com.kateryna_zabazna.musicmanager.domain.relation;

import com.kateryna_zabazna.musicmanager.domain.artist.Artist;

import java.util.List;

public interface RelSongArtistRepository {

    List<Artist> findAllArtistsBySongId(long songId);

    void updateRelations(List<RelSongArtist> relations);
}