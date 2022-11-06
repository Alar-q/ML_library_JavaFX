package com.example.demo.utils;

import com.ml.lib.core.Core;
import com.ml.lib.linear_algebra.operations.Conv;
import com.ml.lib.linear_algebra.operations.self_operation.Threshold;
import com.ml.lib.tensor.Tensor;

import static com.example.demo.utils.ConvertColor.gray;
import static com.ml.lib.tensor.Tensor.tensor;

public class Utils {

    public static Tensor edge(Tensor image, float threshold){
        Tensor gradient = derivative(image);

        Tensor thresholdT = new Threshold(threshold).apply(gradient.mul(tensor(255f)));

        return thresholdT;
    }

    public static Tensor derivative(Tensor image){
        Tensor gray = gray(image);

        Tensor k_x = tensor(new float[][]{
                {-1f, 0f, 1f},
                {-2f, 0f, 2f},
                {-1f, 0f, 1f}
        });
        Tensor k_y = tensor(new float[][]{
                {-1f, -2f, -1f},
                {0f, 0f, 0f},
                {1f, 2f, 1f}
        });

        /* ??
        Tensor k_diagonal1 = tensor(new float[][]{
                {-2f, -1f, 0f},
                {-1f, 0f, 1f},
                {0f, 1f, 2f}
        });
        Tensor k_diagonal2 = tensor(new float[][]{
                {0f, 1f, 2f},
                {-1f, 0f, 1f},
                {-2f, -1f, 0f}
        });
        */

        Tensor Gx = gray.conv(k_x, 1, Conv.Type.SUM).pow(2);
        Tensor Gy = gray.conv(k_y, 1, Conv.Type.SUM).pow(2);

        Tensor sum = Gx.add(Gy);

        Tensor sqrt = sum.sqrt();

        Tensor max = Core.max(sqrt, 2);

        Tensor result = sqrt.div(max);

        return result;
    }


}
