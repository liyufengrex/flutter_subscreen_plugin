## Flutter 双屏支持插件

该插件支持双屏安卓设备，主副屏使用Flutter进行绘制，使用channel实现双屏间的通信交互。

### 使用方式

在pubspec.yaml文件中进行引用：
```
dependencies:
  flutter:
    sdk: flutter
  flutter_subscreen_plugin: ^0.0.1
```
### 注意：
使用该插件，需要在各自 flutter-application 的 android 目录下找到 MainActivity，在 Activity 的 onCreate 方法中添加副屏的初始化方法，如下：
```
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import com.rex.flutter_subscreen_plugin.FlutterSubScreenProvider

class MainActivity: FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
	//副屏初始化
        FlutterSubScreenProvider.initSubScreen(context, flutterEngine)
    }
}
```

完成以上前置工作，就可以开始使用插件能力了。
使用flutter进行主副屏的绘制，以及使用封装能力进行主副屏交互通信：

#### 1. 在main入口区分主副屏：
```
void main() {
  var defaultRouteName = window.defaultRouteName;
  if ("subMain" == defaultRouteName) {
    viceScreenMain(); 
  } else {
    defaultMain();
  }
}

//主屏ui
void defaultMain() {
  runApp(MainApp());
}

//副屏ui
void viceScreenMain() {
  runApp(SubApp());
}

```
#### 2. 示例：主屏发送数据给副屏
```
SubScreenPlugin.sendMsgToViceScreen("data", params: {"params": "123"});
```
#### 3. 示例：副屏接收主屏数据
```
SubScreenPlugin.viceStream.listen((event) {
      print(event.arguments.toString());
    });
```

##### #完整样例可参照插件中的example
