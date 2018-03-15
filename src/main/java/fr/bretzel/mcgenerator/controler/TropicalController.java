package fr.bretzel.mcgenerator.controler;

import fr.bretzel.mcgenerator.MCGenerator;
import fr.bretzel.mcgenerator.util.Color;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
    private BorderPane BORDER_PANE;

    private int PATTERN = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        for (Color c : Color.values())
        {
            BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
            for (int width = 0; width < 32; width++)
            {
                for (int height = 0; height < 32; height++)
                {
                    image.setRGB(width, height, getPixelColor(c, 24));
                }
            }

            Label label = new Label(c.getDisplayName());
            label.setGraphic(new ImageView(SwingFXUtils.toFXImage(image, null)));

            PRIMARY_COLOR.getItems().add(label);

            label = new Label(c.getDisplayName());
            label.setGraphic(new ImageView(SwingFXUtils.toFXImage(image, null)));

            SECONDARY_COLOR.getItems().add(label);
        }

        PATTERN_BUTTON.setText("Selected: 1");

        for (int i = 1; i <= 6; i++)
        {
            MenuItem item = new MenuItem(String.valueOf(i));
            item.setOnAction(event -> {
                PATTERN_BUTTON.setText("Selected: " + item.getText());
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

        CENTERED_IMAGE.fitWidthProperty().bind(BORDER_PANE.widthProperty());
        CENTERED_IMAGE.fitWidthProperty().bind(BORDER_PANE.widthProperty());

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

        CENTERED_IMAGE.setImage(getTropicalFishImage(new Image(path.toExternalForm()), Color.getColorByDisplayName(pdisplay), Color.getColorByDisplayName(sdisplay)));
    }

    public Image getTropicalFishImage(Image i, Color primary, Color secondary)
    {
        BufferedImage image = new BufferedImage((int) i.getWidth(), (int) i.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < i.getWidth(); x++)
        {
            for (int y = 0; y < i.getHeight(); y++)
            {
                int color = i.getPixelReader().getArgb(x, y);

                if (!((color >> 24) == 0x00))
                {
                    // extract each color component
                    int alpha = (color >> 24) & 0xff;
                    int red = (color >>> 16) & 0xFF;
                    int green = (color >>> 8) & 0xFF;
                    int blue = (color >>> 0) & 0xFF;

                    // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                    float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;

                    if (luminance >= 0.45f)
                    {
                        image.setRGB(x, y, getPixelColor(secondary, alpha));
                        continue;
                    } else if (luminance >= 0.25f)
                    {
                        image.setRGB(x, y, getPixelColor(primary, alpha));
                        continue;
                    } else
                    {
                        image.setRGB(x, y, color);
                        continue;
                    }
                } else
                {
                    java.awt.Color c = new java.awt.Color(0, 0, 0, 0);
                    image.setRGB(x, y, c.getRGB());
                    continue;
                }
            }
        }

        return SwingFXUtils.toFXImage(image, null);
    }

    public int getPixelColor(Color color, int alpha)
    {
        return new java.awt.Color(color.getR(), color.getG(), color.getB(), alpha).getRGB();
    }
}
