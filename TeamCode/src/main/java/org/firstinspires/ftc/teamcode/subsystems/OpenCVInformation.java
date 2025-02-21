package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Path;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.HashSet;

public class OpenCVInformation {
    Mat componentMat;
    ArrayList<ArrayList<Integer>> objectIndexes;
    Point center;
    double orientation;
    double distanceX;
    double distanceY;

    public OpenCVInformation(Mat componentMat) {
        this.componentMat = componentMat;
    }

    public OpenCVInformation(Mat componentMat, ArrayList<ArrayList<Integer>> objectIndexes) {
        this.objectIndexes = objectIndexes;
        this.componentMat = componentMat;
    }

    public OpenCVInformation(Point center, double orientation, double distanceX, double distanceY) {
        this.center = center;
        this.orientation = orientation;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
    }

    public Mat getComponentMat() {
        return componentMat;
    }

    public ArrayList<ArrayList<Integer>> getObjectIndexes() {
        return objectIndexes;
    }

    public void setObjectIndexes(ArrayList<ArrayList<Integer>> objectIndexes) {
        this.objectIndexes = objectIndexes;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public double getDistanceX() {
        return distanceX;
    }

    public void setDistanceX(double distanceX) {
        this.distanceX = distanceX;
    }

    public double getDistanceY() {
        return distanceY;
    }

    public void setDistanceY(double distanceY) {
        this.distanceY = distanceY;
    }

    public void setComponentMat(Mat componentMat) {
        this.componentMat = componentMat;
    }
}
