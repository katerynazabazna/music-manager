package com.kateryna_zabazna.musicmanager.plugin.ui;

import com.kateryna_zabazna.musicmanager.application.observer.LoginObserver;
import com.kateryna_zabazna.musicmanager.application.observer.SongListObserver;
import com.kateryna_zabazna.musicmanager.application.service.impl.SongServiceImpl;
import com.kateryna_zabazna.musicmanager.application.service.impl.YTLinkGeneratorServiceImpl;
import com.kateryna_zabazna.musicmanager.domain.song.Song;
import com.kateryna_zabazna.musicmanager.domain.song.SongRepository;
import com.kateryna_zabazna.musicmanager.domain.user.User;
import com.kateryna_zabazna.musicmanager.plugin.repository.SongRepositoryImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MusicManagerUI extends JFrame implements SongListObserver {

    // Constants
    private final String unknownString = "Unknown";

    // UI Components
    private JTable songTable;
    private JButton buttonEdit;
    private JButton buttonDelete;
    private JButton buttonPlay;

    // Members
    private final LoginObserver loginObserver;
    private final User user;
    private final SongServiceImpl songService;
    private final YTLinkGeneratorServiceImpl linkGeneratorService;
    private List<Song> songs = Collections.emptyList();
    private Song selectedSong = null;

    public MusicManagerUI(LoginObserver loginObserver, User user) {
        this.loginObserver = loginObserver;
        this.user = user;

        setupUI();

        // Initialize services
        SongRepository songRepository = new SongRepositoryImpl();
        songService = new SongServiceImpl(user, songRepository, this);
        linkGeneratorService = new YTLinkGeneratorServiceImpl();
    }

    private void setupUI() {
        // Setup window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0, 0, 900, 520);
        setTitle("Music Manager - Home");
        setResizable(false);
        setLocationRelativeTo(null);

        // Set root layout
        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(rootPanel);

        setupSongTable(rootPanel);

        setupButtons(rootPanel);

        // Add selection listener to song table
      assert songTable != null;
      songTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = songTable.getSelectedRow();
            boolean validIndex = selectedIndex != -1;
            selectedSong = validIndex ? songs.get(selectedIndex) : null;
            buttonEdit.setEnabled(validIndex);
            buttonDelete.setEnabled(validIndex);
            buttonPlay.setEnabled(validIndex);
        });
    }

    private void setupSongTable(JPanel rootPanel) {
        // Song list
        String[] columnNames = {"Song Title", "Artist", "Genre", "Bpm", "Bar type"};
        songTable = new JTable(new String[][]{}, columnNames);
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    editSong();
                }
            }
        });
        JScrollPane songScrollPane = new JScrollPane(songTable);
        UIHelper.placeUIComp(rootPanel, songScrollPane, 0, 0, 5, 4, 5.0, 1.0, GridBagConstraints.HORIZONTAL);
    }

    private void setupButtons(JPanel rootPanel) {
        // New button
        JButton buttonNew = new JButton("New Song");
        buttonNew.addActionListener(e -> newSong());
        UIHelper.placeUIComp(rootPanel, buttonNew, 0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Edit button
        buttonEdit = new JButton("Edit Song");
        buttonEdit.setEnabled(false);
        buttonEdit.addActionListener(e -> editSong());
        UIHelper.placeUIComp(rootPanel, buttonEdit, 1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Delete button
        buttonDelete = new JButton("Delete Song");
        buttonDelete.setEnabled(false);
        buttonDelete.addActionListener(e -> deleteSong());
        UIHelper.placeUIComp(rootPanel, buttonDelete, 2, 4, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Play button
        buttonPlay = new JButton("Play Song");
        buttonPlay.setEnabled(false);
        buttonPlay.addActionListener(e -> playSong());
        UIHelper.placeUIComp(rootPanel, buttonPlay, 3, 4, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Logout button
        JButton buttonLogout = new JButton("LogOut");
        buttonLogout.addActionListener(e -> triggerLogout());
        UIHelper.placeUIComp(rootPanel, buttonLogout, 4, 4, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);
    }

    private void newSong() {
        // Open JFrame to add new song
        JFrame newSongDialog = new NewEditSongDialog(songService, user, null);
        newSongDialog.setVisible(true);
    }

    private void playSong() {
        assert selectedSong != null;

        // Assemble YouTube URL
        String url = linkGeneratorService.generateYouTubeLink(selectedSong);

        // Open Website
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editSong() {
        assert selectedSong != null;
        // Open JFrame to edit the selected song
        JFrame newSongDialog = new NewEditSongDialog(songService, user, selectedSong);
        newSongDialog.setVisible(true);
    }

    private void deleteSong() {
        assert selectedSong != null;
        Song songToDelete = selectedSong;
        songTable.getSelectionModel().clearSelection();
        songService.delete(songToDelete);
    }

    private void triggerLogout() {
        dispose();
        loginObserver.onLogout();
    }

    @Override
    public void onSongListChanged(List<Song> songList) {
        this.songs = songList;

        // Convert the List into table rows and cells
        String[][] songData = songList.stream().map(song -> {
            // Get title
            String songTitle = song.getTitle();
            // Get artists
            String songArtists = song.getArtists().stream()
                    .map(artist -> artist.getFirstName() + " " + artist.getLastName())
                    .collect(Collectors.joining(", "));
            // Get genre
            String songGenre = unknownString;
            if (song.getGenre() != null) {
                songGenre = song.getGenre().getName();
            }
            // Get bpm
            String songBpm = song.getBpm() == 0 ? unknownString : String.valueOf(song.getBpm());
            // Get bar type
            String songBarType = unknownString;
            if (song.getBarType() != null) {
                songBarType = song.getBarType().getBeatCount() + "/" + song.getBarType().getBeatValue();
            }
            return new String[]{songTitle, songArtists, songGenre, songBpm, songBarType};
        }).toArray(String[][]::new);

        DefaultTableModel newModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        newModel.setColumnIdentifiers(new String[]{"Title", "Artists", "Genre", "Bpm", "Bar type"});
        for (String[] row : songData) {
            newModel.addRow(row);
        }
        songTable.setModel(newModel);
        newModel.fireTableDataChanged();
    }
}
