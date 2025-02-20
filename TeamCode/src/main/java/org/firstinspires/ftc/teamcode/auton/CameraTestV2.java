package org.firstinspires.ftc.teamcode.auton;

import android.graphics.Color;
import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.subsystems.OpenCVInformation;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@Autonomous(name = "Pipeline Test", group = "Testing OpModes")
public class CameraTestV2 extends OpMode {
    static final int STREAM_WIDTH = 640; // modify for your camera
    static final int STREAM_HEIGHT = 360; // modify for your camera
    OpenCvWebcam webcam;

    @Override
    public void init() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        webcam.setPipeline(new ImageProcessingV2(telemetry));
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened() {
                webcam.startStreaming(STREAM_WIDTH, STREAM_HEIGHT, OpenCvCameraRotation.UPRIGHT, OpenCvWebcam.StreamFormat.MJPEG);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Camera Failed", "");
            }
        });
    }

    @Override
    public void loop() {
    }
}

class ImageProcessingV2 extends OpenCvPipeline {
    //TODO: make this accurate
    int[] BLUE_RANGE = {200, 260};
    int[] RED_RANGE = {347, 12};
    int[] YELLOW_RANGE = {20, 65};

    double[] BLUE_COLOR = new double[]{0, 0, 255, 255};
    double[] RED_COLOR = new double[]{255, 0, 0, 255};
    double[] YELLOW_COLOR = new double[]{255, 255, 0, 255};
    double[] BACKGROUND_COLOR = new double[]{0, 0, 0, 255};

    Telemetry telemetry;

    public ImageProcessingV2(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        Mat resizedImg = new Mat(90, 160, CvType.CV_8UC3);
        Imgproc.resize(input, resizedImg, resizedImg.size());

        resizedImg = assignColors(resizedImg);

        double[] color = YELLOW_COLOR;
        OpenCVInformation connectedComponentInfo = connectedComponentAnalysis(color, resizedImg);
        OpenCVInformation objectInfo = objectSelection(connectedComponentInfo.getComponentMat(), connectedComponentInfo.getObjectIndexes(), color);

        // put to full size
        Size originalSize = input.size();
        input = new Mat();
        Imgproc.resize(objectInfo.getComponentMat(), input, originalSize);
        return input;
    }

    public Mat assignColors(Mat img) {
        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                double[] rgbValues = img.get(y, x);
                float[] hsvValues = new float[3];

                Color.RGBToHSV((int) rgbValues[0], (int) rgbValues[1], (int) rgbValues[2], hsvValues);
                hsvValues[1] *= 255;
                hsvValues[2] *= 255;

                double[] assignedColor = BACKGROUND_COLOR;

                if (hsvValues[0] > BLUE_RANGE[0] && hsvValues[0] < BLUE_RANGE[1]) {
                    if (hsvValues[1] > 125 && hsvValues[2] > 125) {
                        assignedColor = BLUE_COLOR;
                    }
                } else if (hsvValues[0] > YELLOW_RANGE[0] && hsvValues[0] < YELLOW_RANGE[1]) {
                    if (hsvValues[1] > 50 && hsvValues[2] > 100) {
                        assignedColor = YELLOW_COLOR;
                    }
                } else if (hsvValues[0] > RED_RANGE[0] || hsvValues[0] < RED_RANGE[1]) {
                    if (hsvValues[1] > 50 && hsvValues[2] > 100) {
                        assignedColor = RED_COLOR;
                    }
                }

                img.put(y, x, assignedColor);
            }
        }

        return img;
    }

    private OpenCVInformation connectedComponentAnalysis(double[] color, Mat img) {
        Mat connectedComponentMat = Mat.zeros(img.size(), CvType.CV_8UC1);
        ArrayList<int[]> indexesToCombine = new ArrayList<>();
        int currentHighestIndex = 0;

        int width = img.cols();
        int height = img.rows();

        // img.get(y, x) is the order... obviously– why would we use the universal (x, y) format that would just be silly
        // note: apparently it's something to do with matrix math but still
        int pixelCount = 0;
        int y = 0, x = 0;

        while(pixelCount < img.rows()*img.cols() - 1) {
            x = pixelCount % width;
            y = pixelCount / width;

            if (!Arrays.equals(img.get(y, x), color)) {
                while (pixelCount < img.rows()*img.cols() - 1 && !Arrays.equals(img.get(y, x), color)) {
                    pixelCount ++;
                    x = pixelCount % width;
                    y = pixelCount/width;
                }
            } else {
                currentHighestIndex ++;

                while (y < height && Arrays.equals(img.get(y, x), color)) {
                    connectedComponentMat.put(y, x, currentHighestIndex);

                    // But, importantly, if it matches the color of the pixel above it, this means it has 2 neighbors which are considered different components
                    // Of course, we don't need to worry about this if it's the first row
                    if (y != 0 && Arrays.equals(img.get(y - 1, x), color)) {
                        int[] currentCombinedIndexes = new int[] {(int) connectedComponentMat.get(y, x)[0], (int) connectedComponentMat.get(y - 1, x)[0]};
                        if (!indexesToCombine.contains(currentCombinedIndexes)) {
                            indexesToCombine.add(new int[] {(int) connectedComponentMat.get(y, x)[0], (int) connectedComponentMat.get(y - 1, x)[0]});
                        }

                        while(x != img.cols() - 1 && Arrays.equals(img.get(y, x + 1), color) && Arrays.equals(img.get(y - 1, x), color)) {
                            pixelCount++;
                            x = pixelCount % width;
                            y = pixelCount / width;

                            connectedComponentMat.put(y, x, connectedComponentMat.get(y, x - 1)[0]);
                        }
                    }

                    pixelCount ++;
                    x = pixelCount % width;
                    y = (int) pixelCount / width;

                    if (x == 0) {
                        break;
                    }
                }
            }
        }

        // combine indexes
        ArrayList<ArrayList<Integer>> objectIndexes = new ArrayList<>();

        while (!indexesToCombine.isEmpty()) {
            ArrayList<Integer> currentObjectIndexes = new ArrayList<>();
            currentObjectIndexes.add(indexesToCombine.get(0)[0]);
            currentObjectIndexes.add(indexesToCombine.get(0)[1]);
            indexesToCombine.remove(0);

            for (int i = 0; i < currentObjectIndexes.size(); i++) { // currentObjectIndexes will grow throughout the loop
                int index = currentObjectIndexes.get(i);

                for (int listIndex = indexesToCombine.size() - 1; listIndex >= 0; listIndex--) {
                    int[] indexList = indexesToCombine.get(listIndex);

                    if (indexList[0] == index) {
                        currentObjectIndexes.add(indexList[1]);
                        indexesToCombine.remove(indexList);
                    } else if (indexesToCombine.get(listIndex)[1] == index) {
                        currentObjectIndexes.add(indexList[0]);
                        indexesToCombine.remove(indexList);
                    }
                }
            }
            objectIndexes.add(currentObjectIndexes);
        }

        Mat coloredObjectMap = Mat.zeros(img.size(), CvType.CV_8UC1);
        double increment = 255.0 / (objectIndexes.size() + 1); // keep it from being both 0 or 255, so we can use 0 for background and 255 for annotations
        for (int colorY = 0; colorY < coloredObjectMap.rows(); colorY++) {
            for (int colorX = 0; colorX < coloredObjectMap.cols(); colorX++) {
                if (connectedComponentMat.get(colorY, colorX)[0] != 0) {
                    for (int i = 0; i < objectIndexes.size(); i ++) {
                        if (objectIndexes.get(i).contains((int) connectedComponentMat.get(colorY, colorX)[0])) {
                            coloredObjectMap.put(colorY, colorX, Math.floor(increment * (i + 1))); // note that this can only go up to 255 total obj's, since the stored data must be 0-255

                            while (colorX + 1 < coloredObjectMap.cols() && connectedComponentMat.get(colorY, colorX + 1)[0] == connectedComponentMat.get(colorY, colorX)[0]) {
                                colorX ++;
                                coloredObjectMap.put(colorY, colorX, Math.floor(increment * (i + 1))); // note that this can only go up to 255 total obj's, since the stored data must be 0-255
                            }
                            break;
                        }
                    }
                } else {
                    coloredObjectMap.put(colorY, colorX, 0);
                }
            }
        }
        return new OpenCVInformation(coloredObjectMap, objectIndexes);
    }

    private OpenCVInformation objectSelection(Mat componentMat, ArrayList<ArrayList<Integer>> objectIndexes, double[] color) {
        double increment = 255.0 / (objectIndexes.size() + 1); // keeping 0 and 255 clear

        for (double index = 255 - increment; index >= increment; index -= increment) {
            int[] bottomPixel = {-1, -1};
            int[] topPixel = {-1, -1};

            for (int y = 0; y < componentMat.rows(); y ++) {
                int yFromBottom = componentMat.rows() - y - 1;

                for (int x = 0; x < componentMat.cols(); x++) {
                    if (topPixel[0] == -1 && componentMat.get(y, x)[0] == Math.floor(index)) {
                        topPixel[0] = y;
                        topPixel[1] = x;
                    }
                    if (bottomPixel[0] == -1 && componentMat.get(yFromBottom, x)[0] == Math.floor(index)) {
                        bottomPixel[0] = yFromBottom;
                        bottomPixel[1] = x;
                    }
                }
                if (bottomPixel[0] != -1 && topPixel[0] != -1) {
                    break;
                }
            }

            int objectHeight = bottomPixel[0] - topPixel[0] + 1;

            if (objectHeight > 10 && objectHeight < 50) {
                int[] leftPixel = {-1, -1};
                int[] rightPixel = {-1, -1};

                for (int x = 0; x < componentMat.cols(); x++) {
                    int xFromRight = componentMat.cols() - x - 1;

                    for (int y = 0; y < componentMat.rows(); y++) {
                        if (leftPixel[0] == -1 && componentMat.get(y, x)[0] == Math.floor(index)) {
                            leftPixel[0] = y;
                            leftPixel[1] = x;
                        }
                        if (rightPixel[0] == -1 && componentMat.get(y, xFromRight)[0] == Math.floor(index)) {
                            rightPixel[0] = y;
                            rightPixel[1] = xFromRight;
                        }
                    }
                    if (leftPixel[0] != -1 && rightPixel[0] != -1) {
                        break;
                    }
                }

                int objectWidth = rightPixel[1] - leftPixel[1] + 1;

                if (objectWidth > 15 && objectWidth < 50) {
                    int middleY = (bottomPixel[0] - topPixel[0]) / 2 + topPixel[0];

                    int leftMiddleX = -1, rightMiddleX = -1;
                    for (int x = 0; x < componentMat.cols(); x++) {
                        int xFromRight = componentMat.cols() - x - 1;
                        if (leftMiddleX == -1 && componentMat.get(middleY, x)[0] == Math.floor(index)) {
                            leftMiddleX = x;
                        }
                        if (rightMiddleX == -1 && componentMat.get(middleY, xFromRight)[0] == Math.floor(index)) {
                            rightMiddleX = xFromRight;
                        }
                        if (rightMiddleX != -1 && leftMiddleX != -1) {
                            break;
                        }
                    }

                    if (leftMiddleX != -1 && rightMiddleX != -1) {
                        int middleX = (rightMiddleX - leftMiddleX) / 2 + leftMiddleX;
                        Imgproc.circle(componentMat, new Point(middleX, middleY), 3, new Scalar(255), 1);
                        return new OpenCVInformation(componentMat);
                    }
                }
            }
        }

        telemetry.addLine("No valid block found.");
        return new OpenCVInformation(componentMat);
    }
}

