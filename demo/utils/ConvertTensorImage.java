package com.example.demo.utils;

import com.ml.lib.tensor.Tensor;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import static com.ml.lib.tensor.Tensor.tensor;
import static javafx.scene.image.PixelFormat.Type.BYTE_BGRA_PRE;
import static javafx.scene.image.PixelFormat.Type.BYTE_RGB;

public class ConvertTensorImage {
    /**
     * Предполагаем, что всегда rgb, пока что...
     * */
    public static Tensor img2tensor(Image image){
//        PixelFormat.Type type = image.getPixelReader().getPixelFormat().getType();

        int     imageWidth = (int) image.getWidth(),
                imageHeight = (int) image.getHeight();

        // Image.width = Tensor.cols,
        // Image.height = Tensor.rows
        Tensor tensor = new Tensor(3, imageHeight, imageWidth);

        PixelReader pixelReader = image.getPixelReader();

        for(int r = 0; r < imageHeight; r++){
            for(int c = 0; c < imageWidth; c++){
                Color color = pixelReader.getColor(c, r);
                // RGB
                tensor.set(tensor((float) color.getRed()), 0, r, c);
                tensor.set(tensor((float) color.getGreen()), 1, r, c);
                tensor.set(tensor((float) color.getBlue()), 2, r, c);
            }
        }
        return tensor;
    }

    public static WritableImage tensor2img(Tensor tensor){
        if(tensor.rank() < 2)
            return null;

        int[] dims = tensor.dims();
        int l = dims.length;

        int     channels = dims[l-3],
                rows = dims[l-2],
                cols = dims[l-1];

        // Image.width = Tensor.cols,
        // Image.height = Tensor.rows
        WritableImage writableImage = new WritableImage(cols, rows);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        if(channels == 1){
            for(int r=0; r<rows; r++){
                for(int c=0; c<cols; c++){
                    float gray = tensor.get(0, r, c).getScalar();

                    Color color = new Color(gray, gray, gray, 1);

                    pixelWriter.setColor(c, r, color);
                }
            }
        }
        else if(channels == 3){
            for(int r=0; r<rows; r++){
                for(int c=0; c<cols; c++){
                    float   red = tensor.get(0, r, c).getScalar(),
                            green = tensor.get(1, r, c).getScalar(),
                            blue = tensor.get(2, r, c).getScalar();

                    Color color = new Color(red, green, blue, 1);

                    pixelWriter.setColor(c, r, color);
                }
            }
        }

        return writableImage;
    }
}
