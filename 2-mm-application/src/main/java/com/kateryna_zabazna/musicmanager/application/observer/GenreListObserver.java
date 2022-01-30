package com.kateryna_zabazna.musicmanager.application.observer;

import com.kateryna_zabazna.musicmanager.domain.genre.Genre;

import java.util.List;

public interface GenreListObserver {
    void onGenreListChanged(List<Genre> genreList);
}
