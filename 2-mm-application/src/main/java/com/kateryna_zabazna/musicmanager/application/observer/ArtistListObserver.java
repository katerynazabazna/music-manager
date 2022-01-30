package com.kateryna_zabazna.musicmanager.application.observer;

import com.kateryna_zabazna.musicmanager.domain.artist.Artist;

import java.util.List;

public interface ArtistListObserver {
    void onArtistListChanged(List<Artist> artistList);
}
