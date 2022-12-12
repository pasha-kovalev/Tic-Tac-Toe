package by.bsu.tictactoe.resourses;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Resources {
    public static final String RES_PATH = "/img/";
    public static BufferedImage[] marks;

    private Resources(){

    }

    static {
        marks = new BufferedImage[2];
        marks[0] = loadImage(RES_PATH + "x.png");
        marks[1] = loadImage(RES_PATH + "o.png");
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(Class.class.getResource(path)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
