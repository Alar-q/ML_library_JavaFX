package com.example.demo.utils;

import com.ml.lib.tensor.Tensor;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.FileInputStream;
import java.io.IOException;

import static com.example.demo.utils.ConvertTensorImage.img2tensor;

public class IO {
    private static WritableImage loadWritableImage(String path){
        //"src/main/java/com/example/demo/assets/1.jpg"
        try(FileInputStream inputstream = new FileInputStream(path);) {
            Image image = new Image(inputstream);

            int     imageWidth = (int) image.getWidth(),
                    imageHeight = (int) image.getHeight();

            PixelReader pixelReader = image.getPixelReader();

            return new WritableImage(pixelReader, imageWidth, imageHeight);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Tensor loadLikeTensor(String path){
        Image img = loadWritableImage(path);
        return img2tensor(img);
    }
}
