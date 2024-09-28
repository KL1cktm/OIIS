import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SobelEdgeDetection {

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("input.jpg"));
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] grayImage = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int gray = (red + green + blue) / 3;
                grayImage[x][y] = gray;
            }
        }

        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        int[][] edgeImage = new int[width][height];
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int gx = 0, gy = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        gx += sobelX[i + 1][j + 1] * grayImage[x + i][y + j];
                        gy += sobelY[i + 1][j + 1] * grayImage[x + i][y + j];
                    }
                }

                int g = (int) Math.min(255, Math.sqrt(gx * gx + gy * gy));
                edgeImage[x][y] = g;
            }
        }

        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gray = edgeImage[x][y];
                int newPixel = (gray << 16) | (gray << 8) | gray;
                resultImage.setRGB(x, y, newPixel);
            }
        }

        ImageIO.write(resultImage, "jpg", new File("edges_output.jpg"));
    }
}
