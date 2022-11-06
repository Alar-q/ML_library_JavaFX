package com.example.demo;

import com.ml.lib.core.Core;
import com.ml.lib.linear_algebra.operations.Conv;
import com.ml.lib.linear_algebra.operations.morph_transform.Dilation;
import com.ml.lib.linear_algebra.operations.morph_transform.Erosion;
import com.ml.lib.linear_algebra.operations.self_operation.Mirror;
import com.ml.lib.linear_algebra.operations.self_operation.Rotate;
import com.ml.lib.linear_algebra.operations.self_operation.Threshold;
import com.ml.lib.tensor.Tensor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static com.example.demo.utils.ConvertColor.gray;
import static com.example.demo.utils.ConvertTensorImage.tensor2img;
import static com.example.demo.utils.IO.loadLikeTensor;
import static com.example.demo.utils.Utils.derivative;
import static com.example.demo.utils.Utils.edge;
import static com.ml.lib.core.Core.*;
import static com.ml.lib.tensor.Tensor.tensor;

public class Main extends Application {
    public static final int W = 1280, H = 960;

    public static void main(){
        Tensor tensor = loadLikeTensor("src/main/java/com/example/demo/assets/4.jpg");
//        tensor = tensor.conv(new Tensor(2, 2).fill(1), 2, Conv.Type.AVG);
//        blur_example(tensor);
//        rotate_example(tensor);
//        mirror_example(tensor);
//        gray_example(tensor);
//        darkened_example(tensor);

//        der_example(tensor);
//        der_diagonal(tensor);
//        edge_example(tensor, 50);

//        morh_transf(tensor);


        show(tensor, "Original");
    }

    private static void morh_transf(Tensor tensor){
        Tensor edge = edge(tensor, 50f);
        show(edge, "Edge");

        Tensor kernel2 = new Tensor(2, 2);
        Tensor kernel3 = new Tensor(3, 3);
        Tensor kernel5 = new Tensor(5, 5);

        Tensor erosion = erode(edge, kernel2);
        show(erosion, "Erosion");

        Tensor dilation = dilate(edge, kernel2);
        show(dilation, "Dilation");

        // Gradient
        show(dilation.sub(erosion), "Morphological Gradient");

        //Opening
        Tensor opening = dilate(erode(edge, kernel2), kernel5);
        show(opening, "Opening");

        // Closing
        Tensor closing = erode(dilate(edge, kernel5), kernel2);
        show(closing, "Closing");
    }

    private static void der_example(Tensor tensor){
        show(derivative(tensor), "Derivative");
    }
    private static void edge_example(Tensor tensor, float threshold) {
        show(edge(tensor, threshold), "Edge");
    }

    public static void der_diagonal(Tensor tensor){
        Tensor gray = gray(tensor);

        Tensor k_diagonal1 = tensor(new float[][]{
                {-2f, -1f, 0f},
                {-1f,  0f, 1f},
                { 0f,  1f, 2f}
        });
        Tensor k_diagonal2 = tensor(new float[][]{
                { 0f,  1f, 2f},
                {-1f,  0f, 1f},
                {-2f, -1f, 0f}
        });

        Tensor Gd1 = gray.conv(k_diagonal1, 1, Conv.Type.SUM).pow(2);
        Tensor Gd2 = gray.conv(k_diagonal2, 1, Conv.Type.SUM).pow(2);

        Tensor sum = Gd1.add(Gd2);

        Tensor sqrt = sum.sqrt();

        Tensor max = Core.max(sqrt, 2);

        Tensor result = sqrt.div(max);

        show(result, "DerivativeDiagonal");
    }

    private static void blur_example(Tensor tensor){
        Tensor blur_k = new Tensor(7, 7).fill(1);
//        for(int i=0; i<10; i++)
//            tensor = conv(tensor, kernel);
        Tensor blur = tensor.conv(blur_k, 1, Conv.Type.AVG);
        show(blur, "Blur");
    }

    private static void rotate_example(Tensor tensor){
        Tensor rotated = new Rotate(90).apply(tensor);
        show(rotated, "Rotated");
    }
    private static void mirror_example(Tensor tensor){
        Tensor mirrored = new Mirror().apply(tensor);
        show(mirrored, "Mirrored");
    }

    private static void gray_example(Tensor tensor){
        Tensor gray = gray(tensor);
        show(gray, "Gray");
    }

    private static void darkened_example(Tensor tensor){
        show(mul(tensor, tensor(0.5f)), "Darkened");
    }

    public static void show(Tensor tensor, String title){
        Image image = tensor2img(tensor);

        Stage stage = new Stage();
        ScrollPane sp = new ScrollPane();
        ImageView imageView = new ImageView();
        if (image != null) {
            imageView.setImage(image);
            if (image.getWidth() < W) {
                sp.setPrefWidth(image.getWidth() + 3);
            }
            else {
                sp.setPrefWidth(W + 3);
                imageView.setFitWidth(W);
            }

            if (image.getHeight() < H) {
                sp.setPrefHeight(image.getHeight() + 3);
            }
            else {
                sp.setPrefHeight(H + 3);
                imageView.setFitHeight(H);
            }
        }
        sp.setContent(imageView);
        sp.setPannable(false);
        BorderPane box = new BorderPane();
        box.setCenter(sp);

        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
    public static void show(Tensor tensor){
        show(tensor, "Default Title");
    }


    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {
        main();
    }
}