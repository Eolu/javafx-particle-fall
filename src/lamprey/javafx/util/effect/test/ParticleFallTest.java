package lamprey.javafx.util.effect.test;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.effect.ImageInput;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lamprey.javafx.util.effect.ParticleFall;
import lamprey.javafx.util.effect.ParticleUtils;

/**
 * Testing Entry-point
 * 
 * @param args unused
 */
public class ParticleFallTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        ParticleFall snow = new ParticleFall(new ImageInput(ParticleUtils.genSnowflake()), root.boundsInLocalProperty());
        snow.setSpinOrientation(Orientation.HORIZONTAL);
        root.setEffect(snow);
        snow.getAnimation().start();
        final var scene = new Scene(root,
                                    Screen.getPrimary().getVisualBounds().getWidth(),
                                    Screen.getPrimary().getVisualBounds().getHeight());
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("Snow Machine");
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
}