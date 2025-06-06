package com.kateryna_zabazna.musicmanager.application.service.impl;

import com.kateryna_zabazna.musicmanager.application.observer.ArtistListObserver;
import com.kateryna_zabazna.musicmanager.application.service.ArtistService;
import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.artist.ArtistRepository;

import java.util.List;

public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistListObserver artistListObserver;

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistListObserver artistListObserver) {
        this.artistRepository = artistRepository;
        this.artistListObserver = artistListObserver;
        // Notify observers about initial data load
        artistListObserver.onArtistListChanged(getAllArtists());
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepository.findAllArtists();
    }

    @Override
    public void createArtist(Artist artist) {
        artistRepository.save(artist);
        artistListObserver.onArtistListChanged(getAllArtists());
    }
}
