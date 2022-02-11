/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.markic.controller;

import hr.markic.ChessGameApplication;
import hr.markic.dao.RepositoryFactory;
import hr.markic.models.ChessGame;
import hr.markic.models.Person;
import hr.markic.utilities.FileUtils;
import hr.markic.utilities.ImageUtils;
import hr.markic.utilities.SceneUtils;
import hr.markic.utilities.ValidationUtils;
import hr.markic.viewmodel.ChessGameViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author ivanm
 */
public class ChessGameController implements Initializable {

    @FXML
    private Tab tabChessGames;
    @FXML
    private TableView<ChessGameViewModel> tvChessGames;
    @FXML
    private TableColumn<ChessGameViewModel, String> tcGameLocation;
    @FXML
    private TableColumn<ChessGameViewModel, String> tcFirstPlayerName;
    @FXML
    private TableColumn<ChessGameViewModel, String> tcSecondPlayerName;
    @FXML
    private Tab tabEdit;
    @FXML
    private TabPane tpContent;
    @FXML
    private TextField tfFirstNameFirst;
    @FXML
    private TextField tfLastNameFirst;
    @FXML
    private TextField tfAgeFirst;
    @FXML
    private TextField tfEmailFirst;
    @FXML
    private ImageView ivImageFirst;
    @FXML
    private Button btnUploadFirstPlayer;
    @FXML
    private TextField tfGameLocation;
    @FXML
    private ImageView ivImageSecond;
    @FXML
    private Button btnUploadSecondPlayer;
    @FXML
    private TextField tfFirstNameSecond;
    @FXML
    private TextField tfLastNameSecond;
    @FXML
    private TextField tfAgeSecond;
    @FXML
    private TextField tfEmailSecond;
    @FXML
    private Label lblFirstNameFirstError;
    @FXML
    private Label lblLastNameFirstError;
    @FXML
    private Label lblAgeFirstError;
    @FXML
    private Label lblEmailFirstError;
    @FXML
    private Label lblImageFirstError;
    @FXML
    private Label lblFirstNameSecondError;
    @FXML
    private Label lblLastNameSecondError;
    @FXML
    private Label lblAgeSecondError;
    @FXML
    private Label lblEmailSecondError;
    @FXML
    private Label lblImageSecondError;
    @FXML
    private Label lblGameLocationError;

    //My variables
    private final ObservableList<ChessGameViewModel> chessGames = FXCollections.observableArrayList();

    private Map<TextField, Label> validationMap;

    private ChessGameViewModel selectedChessGameViewModel;
    
    private boolean edit = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initValidation();
        initChessGames();
        initTable();
        addIntegerMask(tfAgeFirst);
        addIntegerMask(tfAgeSecond);
        setupListeners();
    }

    private void initChessGames() {
        try {
            RepositoryFactory.getRepository().getChessGames().forEach(chessGame -> chessGames.add(new ChessGameViewModel(chessGame)));
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Exiting...").show();
        }
    }

    private void initTable() {
        tcGameLocation.setCellValueFactory(chessGame -> chessGame.getValue().getGameLocationProperty());
        tcFirstPlayerName.setCellValueFactory(chessGame -> chessGame.getValue().getFirstPlayerNameProperty());
        tcSecondPlayerName.setCellValueFactory(chessGame -> chessGame.getValue().getSecondPlayerNameProperty());

        tvChessGames.setItems(chessGames);
    }

    @FXML
    private void UploadImage(ActionEvent event) {
        File file = FileUtils.uploadFileDialog(tfGameLocation.getScene().getWindow(), "jpg", "jpeg", "png");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            try {
                String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                if (event.getSource().toString().equals(btnUploadFirstPlayer.toString())) {
                    setImage(selectedChessGameViewModel.getChessGame().
                            getFirstPlayerID(),
                            image, ext,
                            ivImageFirst);
                } else {
                    setImage(selectedChessGameViewModel.getChessGame().
                            getSecondPlayerID(),
                            image, ext,
                            ivImageSecond);
                }
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void commitGame(ActionEvent event) {

        if (formValid()) {
            try {
                setPeoples(selectedChessGameViewModel.getChessGame());
                selectedChessGameViewModel.getChessGame().setGameLocation(tfGameLocation.getText().trim());

                if (selectedChessGameViewModel.getChessGame().getIDChessGame() == 0) {
                    chessGames.add(selectedChessGameViewModel);
                } else {
                        RepositoryFactory.getRepository().updateChessGame(selectedChessGameViewModel.getChessGame());
                        tvChessGames.refresh();
                }
            } catch (Exception ex) {
                Logger.getLogger(ChessGameController.class.getName()).log(Level.SEVERE, null, ex);
            }
             selectedChessGameViewModel = null;
                tpContent.getSelectionModel().select(tabChessGames);
            resetForm();
        }

    }

    private void addIntegerMask(TextField tf) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("\\d*")) {
                return change;
            }
            return null;
        };

        tf.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    }

    private void setupListeners() {
        tabEdit.setOnSelectionChanged(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabEdit) && !edit) {
                bindChessGame(null);
            } else
                edit = false;
        });
        chessGames.addListener((ListChangeListener.Change<? extends ChessGameViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(cgvm -> {
                        try {
                            RepositoryFactory.getRepository().deleteChessGame(cgvm.getChessGame());
                        } catch (Exception ex) {
                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(cgvm -> {
                        try {
                            int idChessGame = RepositoryFactory.getRepository().addChessGame(cgvm.getChessGame());
                            cgvm.getChessGame().setIDChessGame(idChessGame);
                        } catch (Exception ex) {
                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvChessGames.getSelectionModel().getSelectedItem() != null) {
            chessGames.remove(tvChessGames.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvChessGames.getSelectionModel().getSelectedItem() != null) {
            edit = true;
            bindChessGame(tvChessGames.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEdit);
        }
    }

    private void bindChessGame(ChessGameViewModel viewModel) {
        resetForm();

        selectedChessGameViewModel = viewModel != null ? viewModel : new ChessGameViewModel(null);

        tfGameLocation.setText(selectedChessGameViewModel.getChessGame().getGameLocation());

        bindPlayers(selectedChessGameViewModel);
    }

    private void resetForm() {
        validationMap.values().forEach(lb -> lb.setVisible(false));
        lblImageFirstError.setVisible(false);
        lblImageSecondError.setVisible(false);
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfGameLocation, lblGameLocationError),
                new AbstractMap.SimpleImmutableEntry<>(tfFirstNameFirst, lblFirstNameFirstError),
                new AbstractMap.SimpleImmutableEntry<>(tfLastNameFirst, lblLastNameFirstError),
                new AbstractMap.SimpleImmutableEntry<>(tfAgeFirst, lblAgeFirstError),
                new AbstractMap.SimpleImmutableEntry<>(tfEmailFirst, lblEmailFirstError),
                new AbstractMap.SimpleImmutableEntry<>(tfFirstNameSecond, lblFirstNameSecondError),
                new AbstractMap.SimpleImmutableEntry<>(tfLastNameSecond, lblLastNameSecondError),
                new AbstractMap.SimpleImmutableEntry<>(tfAgeSecond, lblAgeSecondError),
                new AbstractMap.SimpleImmutableEntry<>(tfEmailSecond, lblEmailSecondError))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private void bindPlayers(ChessGameViewModel chessViewModel) {
        
        tfFirstNameFirst.setText(chessViewModel.getChessGame().getFirstPlayerID().getFirstName());
        tfLastNameFirst.setText(chessViewModel.getChessGame().getFirstPlayerID().getLastName());
        tfAgeFirst.setText(String.valueOf(chessViewModel.getChessGame().getFirstPlayerID().getAge()));
        tfEmailFirst.setText(chessViewModel.getChessGame().getFirstPlayerID().getEmail());
        ivImageFirst.setImage(chessViewModel.getChessGame().getFirstPlayerID().getPicture() != null ? new Image(new ByteArrayInputStream(chessViewModel.getChessGame().getFirstPlayerID().getPicture())) : new Image(new File("src/assets/no_image.png").toURI().toString()));

        tfFirstNameSecond.setText(chessViewModel.getChessGame().getSecondPlayerID().getFirstName());
        tfLastNameSecond.setText(chessViewModel.getChessGame().getSecondPlayerID().getLastName());
        tfAgeSecond.setText(String.valueOf(chessViewModel.getChessGame().getSecondPlayerID().getAge()));
        tfEmailSecond.setText(chessViewModel.getChessGame().getSecondPlayerID().getEmail());
        ivImageSecond.setImage(chessViewModel.getChessGame().getSecondPlayerID().getPicture() != null ? new Image(new ByteArrayInputStream(chessViewModel.getChessGame().getSecondPlayerID().getPicture())) : new Image(new File("src/assets/no_image.png").toURI().toString()));

    }

    private void setImage(Person player, Image image, String ext, ImageView ivImage) throws IOException {

        player.setPicture(ImageUtils.imageToByteArray(image, ext));
        ivImage.setImage(image);

    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty() || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText().trim())) {
                validationMap.get(tf).setVisible(true);
                ok.set(false);
            } else {
                validationMap.get(tf).setVisible(false);
            }
        });

        if (selectedChessGameViewModel.getChessGame().getFirstPlayerID().getPicture() == null) {
            lblImageFirstError.setVisible(true);
            ok.set(false);
        } else {
            lblImageFirstError.setVisible(false);
        }

        if (selectedChessGameViewModel.getChessGame().getSecondPlayerID().getPicture() == null) {
            lblImageSecondError.setVisible(true);
            ok.set(false);
        } else {
            lblImageSecondError.setVisible(false);
        }

        return ok.get();
    }

    private void setPeoples(ChessGame chessGame) throws Exception {

        chessGame.getFirstPlayerID().setFirstName(tfFirstNameFirst.getText().trim());
        chessGame.getFirstPlayerID().setLastName(tfLastNameFirst.getText().trim());
        chessGame.getFirstPlayerID().setAge(Integer.valueOf(tfAgeFirst.getText().trim()));
        chessGame.getFirstPlayerID().setEmail(tfEmailFirst.getText().trim());

        chessGame.getSecondPlayerID().setFirstName(tfFirstNameSecond.getText().trim());
        chessGame.getSecondPlayerID().setLastName(tfLastNameSecond.getText().trim());
        chessGame.getSecondPlayerID().setAge(Integer.valueOf(tfAgeSecond.getText().trim()));
        chessGame.getSecondPlayerID().setEmail(tfEmailSecond.getText().trim());

        //Ovjde bi trebalo napraviti da selectedPersonView radi
        //Treba imati dva ta kako bi mogli pratiti oba igraca unutar saha
        /*if (chessGame.getFirstPlayerID().getIDPerson() == 0) {
            RepositoryFactory.getRepository().addPerson(chessGame.getFirstPlayerID());
        } else{
            RepositoryFactory.getRepository().updatePerson(chessGame.getFirstPlayerID());
        }
        
        if (chessGame.getFirstPlayerID().getIDPerson() == 0) {
            RepositoryFactory.getRepository().addPerson(chessGame.getSecondPlayerID());
        } else{
            RepositoryFactory.getRepository().updatePerson(chessGame.getSecondPlayerID());
        }*/
    }

    @FXML
    private void showPlayersList(ActionEvent event) {

        SceneUtils.loadNewScene("view/People.fxml");
        ChessGameApplication.mainStage.setTitle("Player Manager");
    }
}
