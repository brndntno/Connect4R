import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Coin {
    private int xCoord;
    private int yCoord;
    private BufferedImage image;
    private Color color;

    public Coin(int x, int y, Color color) {
        xCoord = x;
        yCoord = y;
        this.color = color;
        if (this.color == Color.red) {
            try {
                image = ImageIO.read(new File("src/Redm&mChip.png"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                image = ImageIO.read(new File("src/Yellowm&mChip.png"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Coin(Color color) {
        this.color = color;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Color getColor() {
        return color;
    }

    public String getColor2() {
        if (getColor() == Color.red) {
            return "red";
        }
        if (getColor() == Color.yellow) {
            return "yellow";
        }
        return "blue";
    }

    // we use a "bounding Rectangle" for detecting collision
    public Rectangle coinRect() {
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle(xCoord, yCoord, imageWidth, imageHeight);
        return rect;
    }
}