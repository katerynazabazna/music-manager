package com.kateryna_zabazna.musicmanager.application.service;

import com.kateryna_zabazna.musicmanager.domain.song.Song;

public interface YTLinkGeneratorService {

    String generateYouTubeLink(Song song);

}
