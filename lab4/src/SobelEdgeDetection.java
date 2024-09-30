import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SobelEdgeDetection {

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("input.jpg"));
        BufferedImage resultImage = applySobelEdgeDetection(image);
        ImageIO.write(resultImage, "jpg", new File("edges_output.jpg"));
    }

    private static BufferedImage applySobelEdgeDetection(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] grayImage = convertToGrayscale(image);
        int[][] edgeImage = computeSobelEdges(grayImage, width, height);

        return createEdgeImage(edgeImage, width, height);
    }

    private static int[][] convertToGrayscale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] grayImage = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int gray = (red + green + blue) / 3;
                grayImage[x][y] = gray;
            }
        }
        return grayImage;
    }

    private static int[][] computeSobelEdges(int[][] grayImage, int width, int height) {
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
        return edgeImage;
    }

    private static BufferedImage createEdgeImage(int[][] edgeImage, int width, int height) {
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gray = edgeImage[x][y];
                Color newColor = new Color(gray, gray, gray);
                resultImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return resultImage;
    }
}
