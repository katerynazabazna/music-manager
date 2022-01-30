package com.kateryna_zabazna.musicmanager.application.service.impl;

import com.kateryna_zabazna.musicmanager.application.observer.GenreListObserver;
import com.kateryna_zabazna.musicmanager.application.service.GenreService;
import com.kateryna_zabazna.musicmanager.domain.genre.Genre;
import com.kateryna_zabazna.musicmanager.domain.genre.GenreRepository;

import java.util.List;

public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreListObserver genreListObserver;

    public GenreServiceImpl(GenreRepository genreRepository, GenreListObserver genreListObserver) {
        this.genreRepository = genreRepository;
        this.genreListObserver = genreListObserver;
        // Notify observers about initial data load
        genreListObserver.onGenreListChanged(getAllGenres());
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAllGenres();
    }

    @Override
    public void createGenre(Genre genre) {
        genreRepository.save(genre);
        genreListObserver.onGenreListChanged(getAllGenres());
    }
}
