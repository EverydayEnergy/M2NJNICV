//
// Created by ctang on 2017/2/5.
//

#include <jni.h>

#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc_c.h>
//#include "opencv2/highgui/highgui.hpp"
using namespace cv;
using namespace std;
//Mat * mCanny = NULL;
//Mat * mGaussian = NULL;




//JNIEXPORT jintArray JNICALL Java_tw_com_everydayenergy_m2njnicv_MainActivity_grayProc(JNIEnv *env, jclass obj, jintArray buf, jint w, jint h){
/*JNIEXPORT jintArray JNICALL Java_tw_com_everydayenergy_m2njnicv_MainActivity_grayProc(JNIEnv *env, jclass obj, jintArray buf, jint w, jint h){
    jboolean ptfalse = false;
    jint* srcBuf = env->GetIntArrayElements(buf, &ptfalse);
    if(srcBuf == NULL){
        return 0;
    }
    int size=w * h;

    Mat srcImage(h, w, CV_8UC4, (unsigned char*)srcBuf);
    Mat grayImage;
    cvtColor(srcImage, grayImage, COLOR_BGRA2GRAY);
    cvtColor(grayImage, srcImage, COLOR_GRAY2BGRA);

    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, srcBuf);
    env->ReleaseIntArrayElements(buf, srcBuf, 0);
    return result;
}*/



extern "C" JNIEXPORT jboolean JNICALL Java_tw_com_everydayenergy_m2njnicv_CameraPreview_ImageProcessing(
    JNIEnv* env, jobject thiz,
    jint width, jint height,
    jbyteArray NV21FrameData, jintArray outPixels) {
    jbyte * pNV21FrameData = env->GetByteArrayElements(NV21FrameData, 0);
    jint * poutPixels = env->GetIntArrayElements(outPixels, 0);
    static jint w = 0;
    static jint h = 0;

    //if ( mCanny == NULL ) {
        w = width;
        h = height;
        //mCanny = new Mat(height, width, CV_8UC1);
        //mGaussian = new Mat(height, width, CV_8UC1);
    //}
    /*
    else if(w != width || h != height) {
    w = width;
    h = height;
    delete mCanny;
    mCanny = new Mat(height, width, CV_8UC1);
    }*/

    Mat mGray(height, width, CV_8UC1, (unsigned char *)pNV21FrameData);
    Mat mResult(height, width, CV_8UC4, (unsigned char *)poutPixels);
    //IplImage srcImg = mGray;
    //IplImage CannyImg = *mCanny;
    //IplImage GaussImg = *mGaussian;
    //IplImage ResultImg = mResult;

    /*Obtain grayscale of image.
    Perform canny edge detection on grayscale image.
    Apply gaussian blur on grayscale image(store in seperate matrix)
    Input matrices from steps 2 & 3 into SWT algorithm
    Binarize(threshhold) resulting image.
    Feed image to tesseract.
    SWT --> https://sites.google.com/site/roboticssaurav/strokewidthnokia
    MSER --> http://yaronvazana.com/2016/02/02/android-text-detection-using-opencv/
    */
    //Ptr<FeatureDetector> detector = FeatureDetector::create( "MSER" );
    cv::Ptr<cv::Feature2D> detector;
    detector = cv::MSER::create();
    vector<KeyPoint> keypoints;
    detector->detect(mGray, keypoints);

    //cvCanny(&srcImg, &CannyImg, 80, 100, 3);
    //cv::Canny(mGray, *mCanny, 80, 100, 3);
    //cv::GaussianBlur(mGray, *mGaussian, Size(5,5), 1.5);
    //cvCvtColor(&CannyImg, &ResultImg, CV_GRAY2BGRA);
    //cv::cvtColor(*mCanny, mResult, CV_GRAY2BGRA);
    cv::cvtColor(mGray, mResult, CV_GRAY2BGRA);

    env->ReleaseByteArrayElements(NV21FrameData, pNV21FrameData, 0);
    env->ReleaseIntArrayElements(outPixels, poutPixels, 0);
    return true;


}