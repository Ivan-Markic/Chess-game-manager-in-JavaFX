/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.markic.utilities;

import hr.markic.ChessGameApplication;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 *
 * @author ivanm
 */
public class SceneUtils {
    
    public static void loadNewScene(String nameOfScene){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ChessGameApplication.class.getResource(nameOfScene));
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            ChessGameApplication.mainStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
