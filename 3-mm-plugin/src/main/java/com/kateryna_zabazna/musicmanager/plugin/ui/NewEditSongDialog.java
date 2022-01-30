package com.kateryna_zabazna.musicmanager.plugin.ui;

import com.kateryna_zabazna.musicmanager.application.builder.SongBuilder;
import com.kateryna_zabazna.musicmanager.application.observer.ArtistListObserver;
import com.kateryna_zabazna.musicmanager.application.observer.BarTypeListObserver;
import com.kateryna_zabazna.musicmanager.application.observer.GenreListObserver;
import com.kateryna_zabazna.musicmanager.application.service.impl.ArtistServiceImpl;
import com.kateryna_zabazna.musicmanager.application.service.impl.BarTypeServiceImpl;
import com.kateryna_zabazna.musicmanager.application.service.impl.GenreServiceImpl;
import com.kateryna_zabazna.musicmanager.application.service.impl.SongServiceImpl;
import com.kateryna_zabazna.musicmanager.domain.artist.Artist;
import com.kateryna_zabazna.musicmanager.domain.artist.ArtistRepository;
import com.kateryna_zabazna.musicmanager.domain.bartype.BarType;
import com.kateryna_zabazna.musicmanager.domain.bartype.BarTypeRepository;
import com.kateryna_zabazna.musicmanager.domain.genre.Genre;
import com.kateryna_zabazna.musicmanager.domain.genre.GenreRepository;
import com.kateryna_zabazna.musicmanager.domain.song.Song;
import com.kateryna_zabazna.musicmanager.domain.user.User;
import com.kateryna_zabazna.musicmanager.plugin.repository.ArtistRepositoryImpl;
import com.kateryna_zabazna.musicmanager.plugin.repository.BarTypeRepositoryImpl;
import com.kateryna_zabazna.musicmanager.plugin.repository.GenreRepositoryImpl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.kateryna_zabazna.musicmanager.domain.AbstractRepository.AUTO_INC;

public class NewEditSongDialog extends JFrame implements ArtistListObserver, GenreListObserver, BarTypeListObserver {

    // UI Components
    private JTextField songTitle;
    private JList<Artist> songArtists;
    private JComboBox<Genre> songGenre;
    private JTextField songBpm;
    private JComboBox<com.kateryna_zabazna.musicmanager.domain.bartype.BarType> songBarType;

    // Members
    private final User user;
    private final SongServiceImpl songService;
    private final Song selectedSong;

    // Data collections
    private List<Artist> artists;
    private List<Genre> genres;
    private List<com.kateryna_zabazna.musicmanager.domain.bartype.BarType> barTypes;

    public NewEditSongDialog(SongServiceImpl songService, User user, Song selectedSong) {
        this.user = user;
        this.songService = songService;
        this.selectedSong = selectedSong;

        // Setup UI
        setupUI(selectedSong);

        // Create repositories
        ArtistRepository artistRepository = new ArtistRepositoryImpl();
        GenreRepository genreRepository = new GenreRepositoryImpl();
        BarTypeRepository barTypeRepository = new BarTypeRepositoryImpl();

        // Create services
        new ArtistServiceImpl(artistRepository, this);
        new GenreServiceImpl(genreRepository, this);
        new BarTypeServiceImpl(barTypeRepository, this);

        if (selectedSong != null) {
            fillUI(selectedSong);
        }
    }

    private void setupUI(Song selectedSong) {
        // Setup window
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(0, 0, 350, 460);
        setTitle(selectedSong != null ? "Music Manager - Edit song" : "Music Manager - New song");
        setResizable(false);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);

        // Set root layout
        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(rootPanel);

        // Title label
        JLabel songTitleLabel = new JLabel("Song title:");
        UIHelper.placeUIComp(rootPanel, songTitleLabel, 0, 0, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Title text field
        songTitle = new JTextField();
        UIHelper.placeUIComp(rootPanel, songTitle, 0, 1, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Artists label
        JLabel songArtistsLabel = new JLabel("Artists:");
        UIHelper.placeUIComp(rootPanel, songArtistsLabel, 0, 2, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Artists
        songArtists = new JList<>();
        songArtists.setVisibleRowCount(4);
        JScrollPane songScrollPane = new JScrollPane(songArtists);
        songScrollPane.setMinimumSize(new Dimension(100, 80));
        UIHelper.placeUIComp(rootPanel, songScrollPane, 0, 3, 5, 1, 5.0, 0.0, GridBagConstraints.BOTH);

        // Genre label
        JLabel songGenreLabel = new JLabel("Genre:");
        UIHelper.placeUIComp(rootPanel, songGenreLabel, 0, 7, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Genre
        songGenre = new JComboBox<>();
        UIHelper.placeUIComp(rootPanel, songGenre, 0, 8, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Bpm label
        JLabel bpmLabel = new JLabel("Bpm:");
        UIHelper.placeUIComp(rootPanel, bpmLabel, 0, 9, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Bpm text field
        songBpm = new JTextField();
        UIHelper.placeUIComp(rootPanel, songBpm, 0, 10, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Bar type label
        JLabel barTypeLabel = new JLabel("Bar type:");
        UIHelper.placeUIComp(rootPanel, barTypeLabel, 0, 11, 5, 1, 5.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Bar type
        songBarType = new JComboBox<>();
        UIHelper.placeUIComp(rootPanel, songBarType, 0, 12, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Create song button
        JButton buttonCreate = new JButton(selectedSong == null ? "Create song" : "Update song");
        buttonCreate.addActionListener(actionEvent -> createOrUpdateSong());
        UIHelper.placeUIComp(rootPanel, buttonCreate, 0, 13, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);

        // Cancel button
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(actionEvent -> dispose());
        UIHelper.placeUIComp(rootPanel, buttonCancel, 0, 14, 5, 1, 5.0, 0.0, GridBagConstraints.HORIZONTAL);
    }

    private void fillUI(Song selectedSong) {
        // Set title
        songTitle.setText(selectedSong.getTitle());
        // Set artists
        // Search for item with this particular song id and get its index
        List<Artist> selectedArtists = selectedSong.getArtists();
        List<Integer> selectedIndices = new ArrayList<>();
        for (int index = 0; index < artists.size(); index++) {
            for (Artist selectedArtist : selectedArtists) {
                if (artists.get(index).getId() == selectedArtist.getId()) {
                    selectedIndices.add(index);
                }
            }
        }
        songArtists.setSelectedIndices(selectedIndices.stream().mapToInt(Integer::intValue).toArray());
        // Set genre
        for (int index = 0; index < genres.size(); index++) {
            if (genres.get(index).getId() == selectedSong.getGenre().getId()) {
                songGenre.setSelectedIndex(index);
            }
        }
        // Set bpm
        songBpm.setText(String.valueOf(selectedSong.getBpm()));
        // Set bar type
        for (int index = 0; index < barTypes.size(); index++) {
            if (barTypes.get(index).getId() == selectedSong.getBarType().getId()) {
                songBarType.setSelectedIndex(index);
            }
        }
    }

    private void createOrUpdateSong() {
        // Get title
        String title = songTitle.getText().trim();

        // Get artists
        List<Artist> selectedArtists = new ArrayList<>();
        int[] selectedIndices = songArtists.getSelectedIndices();
        for (int index : selectedIndices) {
            selectedArtists.add(artists.get(index));
        }

        long songId = selectedSong != null ? selectedSong.getId() : AUTO_INC;
        SongBuilder songBuilder = new SongBuilder(songId, user, title, selectedArtists);

        // Get genre
        int selectedGenreIndex = songGenre.getSelectedIndex();
        if (selectedGenreIndex != -1) {
            Genre genre = genres.get(selectedGenreIndex);
            songBuilder.withGenre(genre);
        }

        // Get Bpm
        if (!songBpm.getText().trim().isEmpty()) {
            float bpm = Float.parseFloat(songBpm.getText().trim());
            songBuilder.withBpm(bpm);
        }

        // Get bar type
        int selectedBarTypeIndex = songBarType.getSelectedIndex();
        if (selectedBarTypeIndex != -1) {
            BarType barType = barTypes.get(selectedBarTypeIndex);
            songBuilder.withBarType(barType);
        }

        // Build and save song
        if (selectedSong != null) {
            songService.update(songBuilder.build());
        } else {
            songService.create(songBuilder.build());
        }

        // Close dialog
        dispose();
    }

    @Override
    public void onArtistListChanged(List<Artist> artists) {
        this.artists = artists;
        songArtists.setListData(artists.toArray(Artist[]::new));
    }

    @Override
    public void onGenreListChanged(List<Genre> genres) {
        this.genres = genres;
        DefaultComboBoxModel<Genre> newModel = new DefaultComboBoxModel<>();
        newModel.addAll(genres);
        songGenre.setModel(newModel);
    }

    @Override
    public void onBarTypeListChanged(List<BarType> barTypes) {
        this.barTypes = barTypes;
        DefaultComboBoxModel<BarType> newModel = new DefaultComboBoxModel<>();
        newModel.addAll(barTypes);
        songBarType.setModel(newModel);
    }
}
