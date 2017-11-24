//package com.ths.plt.cordova.utils;
//
//
//import es.dmoral.toasty.Toasty;
//
///**
// * 吐司提示消息
// * github地址: https://github.com/GrenderG/Toasty
// */
//public class ToastUtils {
//
//    public static final int LENGTH_SHORT = 2;
//    public static final int LENGTH_LONG = 4;
//
//    public static void shortMsg(String msg) {
//        if (StringUtils.isEmpty(msg)) {
//            return;
//        }
//        Toasty.custom(EchatAppUtil.getAppContext(), msg,
//                null, 0, LENGTH_SHORT, false, false).show();
//    }
//
//    public static void longMsg(String msg) {
//        if (StringUtils.isEmpty(msg)) {
//            return;
//        }
//        Toasty.custom(EchatAppUtil.getAppContext(), msg,
//                null, 0, LENGTH_LONG, false, false).show();
//    }
//
//    public static void shortMsg(int resId) {
//        Toasty.custom(EchatAppUtil.getAppContext(),
//                EchatAppUtil.getAppContext().getString(resId),
//                null, 0, LENGTH_SHORT, false, false).show();
//    }
//
//    public static void longMsg(int resId) {
//        Toasty.custom(EchatAppUtil.getAppContext(),
//                EchatAppUtil.getAppContext().getString(resId),
//                null, 0, LENGTH_LONG, false, false).show();
//    }
//}
