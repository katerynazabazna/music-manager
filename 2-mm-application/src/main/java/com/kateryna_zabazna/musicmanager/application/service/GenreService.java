package com.kateryna_zabazna.musicmanager.application.service;

import com.kateryna_zabazna.musicmanager.domain.genre.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    void createGenre(Genre genre);

}
