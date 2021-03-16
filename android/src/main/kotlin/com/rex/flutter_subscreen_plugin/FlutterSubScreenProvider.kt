package com.rex.flutter_subscreen_plugin

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build

/**
 * @Description:    描述
 * @Author:         liyufeng
 * @CreateDate:     2021/3/16 10:53 AM
 */

class FlutterSubScreenProvider {

    //设置显示副屏幕, 在壳工程的MainActivity初始化
    fun configSecondDisplay(plugin: FlutterSubscreenPlugin, context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val manager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
                val displays = manager.displays
                if (displays.size > 1) {
                    val display = displays[1]
                    val handler = FlutterSubScreenPresentation(context, display)
                    handler.show()
                    plugin.onCreateViceChannel(handler.flutterEngine.dartExecutor)
                }
            }
        } catch (e: Throwable) {
            println(e.message)
            e.printStackTrace()
        }
    }

}