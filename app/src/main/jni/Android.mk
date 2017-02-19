
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
include /home/ctang/AndroidStudioProjects/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
#OPENCV_LIB_TYPE:=SHARED
OPENCV_LIB_TYPE:=STATIC

LOCAL_SRC_FILES := jnicv-process.cpp
LOCAL_LDLIBS += -llog -ldl
LOCAL_MODULE := jnicv-process

include $(BUILD_SHARED_LIBRARY)