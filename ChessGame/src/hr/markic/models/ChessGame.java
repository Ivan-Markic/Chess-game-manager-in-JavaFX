/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.markic.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ivanm
 */
@Entity
@Table(name = "ChessGame")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChessGame.findAll", query = "SELECT c FROM ChessGame c"),
    @NamedQuery(name = "ChessGame.findByIDChessGame", query = "SELECT c FROM ChessGame c WHERE c.iDChessGame = :iDChessGame"),
    @NamedQuery(name = "ChessGame.findByGameLocation", query = "SELECT c FROM ChessGame c WHERE c.gameLocation = :gameLocation")})
public class ChessGame implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDChessGame")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iDChessGame;
    @Basic(optional = false)
    @Column(name = "GameLocation")
    private String gameLocation;
    @JoinColumn(name = "FirstPlayerID", referencedColumnName = "IDPerson")
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Person firstPlayerID;
    @JoinColumn(name = "SecondPlayerID", referencedColumnName = "IDPerson")
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Person secondPlayerID;

    public ChessGame() {
    }

    public ChessGame(Integer iDChessGame) {
        this.iDChessGame = iDChessGame;
    }

    public ChessGame(Integer iDChessGame, String gameLocation) {
        this.iDChessGame = iDChessGame;
        this.gameLocation = gameLocation;
    }
    
    public ChessGame(Integer iDChessGame, String gameLocation, Person firstPlayerID, Person secondPlayerID) {
        this.iDChessGame = iDChessGame;
        this.gameLocation = gameLocation;
        this.firstPlayerID = firstPlayerID;
        this.secondPlayerID = secondPlayerID;
    }
    
    public ChessGame(ChessGame data){
        updateDetails(data);
    }
    
    public void updateDetails(ChessGame data) {
        
        this.gameLocation = data.getGameLocation();
        this.firstPlayerID = data.getFirstPlayerID();
        this.secondPlayerID = data.getSecondPlayerID();
    }

    public Integer getIDChessGame() {
        return iDChessGame;
    }

    public void setIDChessGame(Integer iDChessGame) {
        this.iDChessGame = iDChessGame;
    }

    public String getGameLocation() {
        return gameLocation;
    }

    public void setGameLocation(String gameLocation) {
        this.gameLocation = gameLocation;
    }

    public Person getFirstPlayerID() {
        return firstPlayerID;
    }

    public void setFirstPlayerID(Person firstPlayerID) {
        this.firstPlayerID = firstPlayerID;
    }

    public Person getSecondPlayerID() {
        return secondPlayerID;
    }

    public void setSecondPlayerID(Person secondPlayerID) {
        this.secondPlayerID = secondPlayerID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDChessGame != null ? iDChessGame.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChessGame)) {
            return false;
        }
        ChessGame other = (ChessGame) object;
        if ((this.iDChessGame == null && other.iDChessGame != null) || (this.iDChessGame != null && !this.iDChessGame.equals(other.iDChessGame))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Location: " + gameLocation + "First Player: " + firstPlayerID + " Second Player: " + secondPlayerID;
    }
    
}
