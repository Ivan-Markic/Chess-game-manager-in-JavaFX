/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.markic;

import hr.markic.utilities.SceneUtils;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author ivanm
 */
public class ChessGameApplication extends Application {
    
    public static Stage mainStage;
    
    @Override
    public void start(Stage stage) throws IOException {
        
        

        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setTitle("ChessGame Manager");
        mainStage = stage;
        SceneUtils.loadNewScene("view/ChessGame.fxml");
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
