package com.kateryna_zabazna.musicmanager.application.observer;

import com.kateryna_zabazna.musicmanager.domain.song.Song;

import java.util.List;

public interface SongListObserver {
    void onSongListChanged(List<Song> songList);
}
