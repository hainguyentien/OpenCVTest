#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <vector>

using namespace cv;
using namespace std;

extern "C"
{
void JNICALL Java_com_rambo_opencvtest_MainActivity_salt(JNIEnv *env, jobject instance,
                                                                           jlong matAddrGray,
                                                                           jint nbrElem) {
    Mat &mGr = *(Mat *) matAddrGray;
    for (int k = 0; k < nbrElem; k++) {
        int i = rand() % mGr.cols;
        int j = rand() % mGr.rows;
        mGr.at<uchar>(j, i) = 255;
    }
}

void JNICALL Java_com_rambo_opencvtest_MainActivity_blur(JNIEnv *env, jobject instance, jlong matAddrRgb){
    Mat &mRgb = *(Mat *) matAddrRgb;
    blur(mRgb, mRgb, Size(15,15));
}

void JNICALL Java_com_rambo_opencvtest_MainActivity_EdgeDetection (JNIEnv *env, jobject instance, jlong matAddrGray){
    Mat &mGray = *(Mat *) matAddrGray;
    Canny(mGray,mGray,50, 100, 3);
}

void JNICALL Java_com_rambo_opencvtest_MainActivity_Histogram (JNIEnv *env, jobject instance, jlong matAddrGray){
    Mat &mGray = *(Mat *) matAddrGray;
    equalizeHist(mGray,mGray);
}

void JNICALL Java_com_rambo_opencvtest_MainActivity_DrawOnFrame (JNIEnv *env, jobject instance, jlong matAddrRgba){
    Mat &mRgba = * (Mat *) matAddrRgba;
    rectangle(mRgba, cvPoint(200,200),cvPoint(600,600),CV_RGB(255,0,0),5,8);
}
}
