package com.kateryna_zabazna.musicmanager.application.service.impl;

import com.kateryna_zabazna.musicmanager.application.service.YTLinkGeneratorService;
import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.song.Song;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class YTLinkGeneratorServiceImpl implements YTLinkGeneratorService {

    private static final String URL_TEMPLATE = "https://www.youtube.com/results?search_query=%s+%s";

    @Override
    public String generateYouTubeLink(Song song) {
        String songTitle = song.getTitle();
        Artist firstArtist = song.getArtists().get(0);
        String firstArtistName = firstArtist.getFirstName();
        if (!firstArtist.getLastName().isEmpty()) {
            firstArtistName += " " + firstArtist.getLastName();
        }
        return String.format(URL_TEMPLATE, encodeValue(firstArtistName), encodeValue(songTitle));
    }

    private String encodeValue(String value) {
      return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
