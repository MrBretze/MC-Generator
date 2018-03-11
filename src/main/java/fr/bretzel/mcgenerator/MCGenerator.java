package fr.bretzel.mcgenerator;

import fr.bretzel.mcgenerator.controler.TropicalController;
import fr.bretzel.mcgenerator.util.Color;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MCGenerator extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tropical_fish.fxml"));
        loader.setController(new TropicalController());
        Scene scene = new Scene(loader.load());
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static int getVarient(Color a, Color b, int model, int pattern) {
        return (a.getId()<<24) + (b.getId()<<16) + (pattern<<8) + (model);
    }
}
