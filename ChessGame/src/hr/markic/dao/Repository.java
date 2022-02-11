/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.markic.dao;

import hr.markic.models.ChessGame;
import hr.markic.models.Person;
import java.util.List;

/**
 *
 * @author ivanm
 */
public interface Repository {
    
    //for person
    int addPerson(Person data) throws Exception;
    void deletePerson(Person person) throws Exception;
    List<Person> getPeople() throws Exception;
    Person getPerson(int idPerson) throws Exception;
    void updatePerson(Person person) throws Exception;
    //for chess game
    int addChessGame(ChessGame data) throws Exception;
    void deleteChessGame(ChessGame chessGame) throws Exception;
    public List<ChessGame> getChessGames() throws Exception;
    ChessGame getChessGame(int idChessGame) throws Exception;
    void updateChessGame(ChessGame chessGame) throws Exception;
    
    default void release() throws Exception{};
    
}
