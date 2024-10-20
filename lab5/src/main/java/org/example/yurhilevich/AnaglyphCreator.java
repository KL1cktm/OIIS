package org.example.yurhilevich;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class AnaglyphCreator {
    private BufferedImage leftImage;
    private BufferedImage rightImage;

    public AnaglyphCreator(String leftImagePath, String rightImagePath) {
        try {
            leftImage = ImageIO.read(new File(leftImagePath));
            rightImage = ImageIO.read(new File(rightImagePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        saveAnaglyphImage("anaglyph_output.png");
    }

    private void saveAnaglyphImage(String outputPath) {
        if (leftImage != null && rightImage != null) {
            int width = Math.min(leftImage.getWidth(), rightImage.getWidth());
            int height = Math.min(leftImage.getHeight(), rightImage.getHeight());
            BufferedImage anaglyphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color leftColor = new Color(leftImage.getRGB(x, y));
                    Color rightColor = new Color(rightImage.getRGB(x, y));

                    int leftRed = leftColor.getRed();
                    int rightGreen = rightColor.getGreen();
                    int rightBlue = rightColor.getBlue();

                    Color anaglyphColor = new Color(leftRed, rightGreen, rightBlue);

                    anaglyphImage.setRGB(x, y, anaglyphColor.getRGB());
                }
            }

            try {
                ImageIO.write(anaglyphImage, "png", new File(outputPath));
                System.out.println("Анаглифическое изображение сохранено в: " + outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final String LEFT_IMAGE_PATH = "image1.jpg";
        final String RIGHT_IMAGE_PATH = "image2.jpg";

        new AnaglyphCreator(LEFT_IMAGE_PATH, RIGHT_IMAGE_PATH);
    }
}
