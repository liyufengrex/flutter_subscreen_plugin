package com.rex.flutter_subscreen_plugin

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

/**
 * @Description:    描述
 * @Author:         liyufeng
 * @CreateDate:     2021/3/16 10:53 AM
 */

class FlutterSubScreenProvider {

    companion object {
        //设置显示副屏幕, 在壳工程的MainActivity初始化
        fun configSecondDisplay(plugin: FlutterSubscreenPlugin, context: Context) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    val manager =
                        context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
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

        ///初始化副屏
        fun initSubScreen(context: Context, flutterEngine: FlutterEngine?) {
            flutterEngine?.plugins?.get(FlutterSubscreenPlugin::class.java)?.let { plugin ->
                val subScreenPlugin = plugin as FlutterSubscreenPlugin
                configSecondDisplay(subScreenPlugin, context)
            }
        }
    }


}