/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.markic.viewmodel;

import hr.markic.models.ChessGame;
import hr.markic.models.Person;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ivanm
 */
public class ChessGameViewModel {
    
    
    private final ChessGame chessGame;
    
    
    public ChessGameViewModel(ChessGame chessGame) {

        
        if (chessGame == null) {
            chessGame = new ChessGame(0 ,"", 
                    new Person(0, "", "", 0, ""), 
                    new Person(0, "", "", 0, ""));
        }
        this.chessGame = chessGame;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public IntegerProperty getIdChessGameProperty() {
        return new SimpleIntegerProperty(chessGame.getIDChessGame());
    }
    
    public StringProperty getGameLocationProperty() {
        return new SimpleStringProperty(chessGame.getGameLocation());
    }

    public StringProperty getFirstPlayerNameProperty() {
        return new SimpleStringProperty(chessGame.getFirstPlayerID().getFirstName());
    }

    public StringProperty getSecondPlayerNameProperty() {
        return new SimpleStringProperty(chessGame.getSecondPlayerID().getFirstName());
    }
    
}
