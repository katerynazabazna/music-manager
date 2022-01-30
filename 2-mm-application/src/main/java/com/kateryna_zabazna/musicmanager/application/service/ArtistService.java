package com.kateryna_zabazna.musicmanager.application.service;

import com.kateryna_zabazna.musicmanager.domain.artist.Artist;

import java.util.List;

public interface ArtistService {

    List<Artist> getAllArtists();

    void createArtist(Artist artist);

}
