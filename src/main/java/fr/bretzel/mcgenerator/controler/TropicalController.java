package fr.bretzel.mcgenerator.controler;

import fr.bretzel.mcgenerator.MCGenerator;
import fr.bretzel.mcgenerator.util.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class TropicalController implements Initializable
{
    @FXML
    private TextField OUTPUT_COMMAND;

    @FXML
    private ImageView CENTERED_IMAGE;

    @FXML
    private RadioButton BUTTON_MODEL_A;

    @FXML
    private RadioButton BUTTON_MODEL_B;

    @FXML
    private ListView<Label> PRIMARY_COLOR;

    @FXML
    private ListView<Label> SECONDARY_COLOR;

    @FXML
    private MenuButton PATTERN_BUTTON;

    @FXML
    private Label PATTERN_LABEL;

    @FXML
    private GridPane GRID_PANE;

    private int PATTERN = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        for (Color c : Color.values())
        {
            BufferedImage image = new BufferedImage(32, 32, 1);
            for (int width = 0; width < 32; width++)
            {
                for (int height = 0; height < 32; height++)
                {
                    int r = c.getR();
                    int g = c.getG();
                    int b = c.getB();

                    int p = (24 << 24) | (r << 16) | (g << 8) | b;

                    image.setRGB(width, height, p);
                }
            }

            Label label = new Label(c.getDisplayName());
            label.setGraphic(new ImageView(SwingFXUtils.toFXImage(image, null)));

            PRIMARY_COLOR.getItems().add(label);

            label = new Label(c.getDisplayName());
            label.setGraphic(new ImageView(SwingFXUtils.toFXImage(image, null)));

            SECONDARY_COLOR.getItems().add(label);
        }

        PATTERN_LABEL.setText("Selected: 1");

        for (int i = 1; i <= 6; i++)
        {
            MenuItem item = new MenuItem(String.valueOf(i));
            item.setOnAction(event -> {
                PATTERN_LABEL.setText("Selected: " + item.getText());
                PATTERN = Integer.valueOf(item.getText());
                update();
            });
            PATTERN_BUTTON.getItems().add(item);
        }

        PRIMARY_COLOR.getSelectionModel().select(0);
        SECONDARY_COLOR.getSelectionModel().select(0);

        PRIMARY_COLOR.setOnMouseClicked(event -> update());
        SECONDARY_COLOR.setOnMouseClicked(event -> update());

        ToggleGroup model = new ToggleGroup();

        BUTTON_MODEL_A.setToggleGroup(model);
        BUTTON_MODEL_B.setToggleGroup(model);

        model.selectToggle(BUTTON_MODEL_A);

        model.selectedToggleProperty().addListener((observable, oldValue, newValue) -> update());

        update();
    }

    public int getModel()
    {
        return BUTTON_MODEL_A.isSelected() ? 0 : BUTTON_MODEL_B.isSelected() ? 1 : 0;
    }

    public void update()
    {
        String pdisplay = PRIMARY_COLOR.getSelectionModel().getSelectedItem().getText();
        String sdisplay = SECONDARY_COLOR.getSelectionModel().getSelectedItem().getText();
        int value = MCGenerator.getVarient(Color.getColorByDisplayName(pdisplay), Color.getColorByDisplayName(sdisplay), getModel(), PATTERN - 1);
        OUTPUT_COMMAND.setText("/summon minecraft:tropical_fish ~ ~ ~ {Variant:" + value + "}");

        URL path = MCGenerator.class.getResource("/models/tropical_fish/model_" + (BUTTON_MODEL_A.isSelected() ? "a" : "b") + "_" + (PATTERN - 1) + ".png");
        CENTERED_IMAGE.setImage(new Image(path.toExternalForm()));
    }
}
