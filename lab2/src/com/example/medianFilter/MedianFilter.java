package com.example.medianFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MedianFilter {
    private final int HALF_WINDOW = 1;

    private String filename;
    public void startProcess(String filename) {
        this.filename = filename;
        try {
            File file = new File(filename);
            BufferedImage bufferedImage = ImageIO.read(file);
            useMedianFilter(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void useMedianFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, image.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int medianPixel = getMedianPixel(image,x,y);
                outputImage.setRGB(x,y,medianPixel);
            }
        }

        try {
            File output = new File("MedianFilterResult.png");
            ImageIO.write(outputImage, "png", output);
            System.out.println("Медианный фильтр применен к изображению: " + this.filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getMedianPixel(BufferedImage image, int x, int y) {
        List<Integer> pixels = new ArrayList<>();
        for (int i = -HALF_WINDOW;i<=HALF_WINDOW;i++) {
            for (int j = -HALF_WINDOW;j<=HALF_WINDOW;j++) {
                int pixel_x = Math.min(Math.max(x-i,0),image.getWidth()-1);
                int pixel_y = Math.min(Math.max(y-j,0),image.getHeight() - 1);
                pixels.add(image.getRGB(pixel_x,pixel_y));
            }
        }
        Collections.sort(pixels);
        int medianPixel = pixels.get(pixels.size()/2);
        return medianPixel;
    }

    public static void main(String[] args) {
        MedianFilter median = new MedianFilter();
        median.startProcess("StartImage.png");
    }
}
