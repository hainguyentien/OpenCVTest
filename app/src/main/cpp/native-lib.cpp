#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
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
}
