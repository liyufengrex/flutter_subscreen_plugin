package com.rex.flutter_subscreen_plugin

/**
 * @Description:    常量
 * @Author:         liyufeng
 * @CreateDate:     2022/6/29 9:31 上午
 */

abstract class Constant {
    companion object {
        const val METHOD_SUPPORT_DOUBLE_SCREEN = "supportDoubleScreen" //是否支持多屏
        const val METHOD_CHECK_OVERLAY_PERMISSION = "checkOverlayPermission" //校验overlay权限
        const val METHOD_REQUEST_OVERLAY_PERMISSION = "requestOverlayPermission" //请求overlay权限
        const val METHOD_DOUBLE_SCREEN_SHOW = "doubleScreenShow" //显示副屏
        const val METHOD_DOUBLE_SCREEN_CANCEL = "doubleScreenCancel" //关闭副屏
    }
}