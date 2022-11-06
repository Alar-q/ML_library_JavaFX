package com.example.demo.utils;

import com.ml.lib.tensor.Tensor;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import static com.ml.lib.tensor.Tensor.tensor;

public class ConvertColor {
    private static WritableImage gray(Image image){
        int     imageWidth = (int) image.getWidth(),
                imageHeight = (int) image.getHeight();

        WritableImage clone = new WritableImage(image.getPixelReader(), imageWidth, imageHeight);

        PixelReader pixelReader = clone.getPixelReader();
        PixelWriter pixelWriter = clone.getPixelWriter();

        for(int r = 0; r < imageWidth; r++){
            for(int c = 0; c < imageHeight; c++){
//                Color o = pixelReader.getColor(r, c);
//                System.out.println(o.getRed()*255 + " " + o.getGreen()*255 + " " + o.getBlue()*255);
                /*
                 * isOpaque - непрозрачный, isRGB
                 * opacity - a in argb
                 * saturation - насыщенность
                 * brightness - яркость
                 * hue - оттенок
                 * */
//                System.out.println("rgb:" +o.isOpaque() +
//                        ", opacity: " + o.getOpacity() +
//                        ", saturation:" + o.getSaturation() +
//                        ", brightness:" + o.getBrightness() +
//                        ", hue:" + o.getHue()
//                );

                Color color = pixelReader.getColor(r, c).grayscale();
                pixelWriter.setColor(r, c, color);
            }
        }

        return clone;
    }
    /**
     * The method don't change the state of the argument.
     * */
    public static Tensor gray(Tensor tensor) {
        int l =  tensor.dims().length;
        int     channels = tensor.dims()[l - 3],
                rows = tensor.dims()[l - 2],
                cols = tensor.dims()[l - 1];

        if(channels == 1){
            return tensor.clone();
        }

        Tensor result = new Tensor(1, rows, cols);

        for(int r=0; r<rows; r++){
            for(int c=0; c<cols; c++){
                float   red = tensor.get(0, r, c).getScalar(),
                        green = tensor.get(1, r, c).getScalar(),
                        blue = tensor.get(2, r, c).getScalar();
//                float gray = (red+green+blue)/3f;
                float gray = red*0.299f + green*0.587f + blue*0.114f;

                result.set(tensor(gray), 0, r, c);
            }
        }

        return result;
    }

//    public static Tensor zero_one(Tensor tensor){
//        return null;
//    }
}
