package com.example.brightCorrect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BrightCorrection {
    public void startProcess(String filename1, String filename2) {
        try {
            File file1 = new File(filename1);
            File file2 = new File(filename2);
            BufferedImage bufferedImage1 = ImageIO.read(file1);
            BufferedImage bufferedImage2 = ImageIO.read(file2);
            brightnessEqualization(bufferedImage1,bufferedImage2);
            ImageIO.write(bufferedImage1, "jpg", new File("image_adjusted1.jpg"));
            ImageIO.write(bufferedImage2, "jpg", new File("image_adjusted2.jpg"));
            System.out.println("Программа завершена успешно!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void brightnessEqualization(BufferedImage image1,BufferedImage image2) {
        int brightLevel1 = getBrightnessLevel(image1);
        int brightLevel2 = getBrightnessLevel(image2);
        int brightDifference = Math.abs(brightLevel1 - brightLevel2)/2;
        int brightDif1, brightDif2;
        if (brightLevel1 > brightLevel2) {
            brightDif1 = brightDifference * -1;
            brightDif2 = brightDifference;
        } else {
            brightDif2 = brightDifference * -1;
            brightDif1 = brightDifference;
        }
        brightnessCorrect(image1,brightDif1);
        brightnessCorrect(image2,brightDif2);
    }

    private void brightnessCorrect(BufferedImage image, int brightDifference) {
        for (int x=0;x<image.getWidth();x++){
            for (int y=0;y<image.getHeight();y++) {
                int rgb = image.getRGB(x,y);
                Color color = new Color(rgb);
                int red = Math.min(Math.max(color.getRed()+brightDifference,0),255);
                int green = Math.min(Math.max(color.getGreen()+brightDifference,0),255);
                int blue = Math.min(Math.max(color.getBlue()+brightDifference,0),255);
                Color newColor = new Color(red,green,blue);
                image.setRGB(x,y,newColor.getRGB());
            }
        }
    }
    private int getBrightnessLevel(BufferedImage image) {
        int x = image.getWidth();
        int y = image.getHeight();
        int brightLevel = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                int rgb = image.getRGB(i,j);
                Color color = new Color(rgb);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                brightLevel += (int) (0.299 * red + 0.587 * green + 0.114 * blue);
            }
        }
        brightLevel = brightLevel/(x*y);
        return brightLevel;
    }
}
