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

    companion object {

        //过滤品牌
        private fun filterBrand(context: Context): Boolean {
            val brands = context.resources.getStringArray(R.array.filterBrands)
            if (brands.contains(Build.BRAND.toLowerCase()) || brands.contains(Build.MANUFACTURER.toLowerCase())) {
                return true
            }
            return false
        }

        //是否支持双屏
        fun supportViceScreen(context: Context): Boolean {
            if (filterBrand(context)) {
                return false
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    val manager =
                        context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
                    val displays = manager.displays
                    return displays.size > 1
                }
            }
            return false
        }

        //设置显示副屏幕, 在壳工程的MainActivity初始化
        fun configSecondDisplay(plugin: FlutterSubscreenPlugin, context: Context) {
            if (!supportViceScreen(context)) {
                return
            }
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
    }


}