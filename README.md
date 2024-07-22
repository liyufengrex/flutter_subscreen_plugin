## Flutter 双屏支持插件

本库方案统一技术栈，主副屏都使用 flutter 进行开发，降低开发及后期维护成本，通过创新的双引擎通信机制，确保了主副屏之间的高效交互。

![image](https://github.com/liyufengrex/flutter_subscreen_plugin/assets/48038749/df1cca6a-4596-46bf-b40f-11f83331770a)


### 使用方式

在pubspec.yaml文件中进行引用：
```
dependencies:
  flutter_subscreen_plugin: ^1.0.0
```
### 使用方法：

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

#### 4. 提供方法：获取当前设备环境是否支持双屏
```
SubScreenPlugin.isMultipleScreen((result) {
      print("是否支持双屏：$result");
    });
```

#### 5. 提供方法：判断当前应用是否具备 overlay 窗口权限
```
SubScreenPlugin.checkOverlayPermission((result) {
      print("是否支持 overlay：$result");
    });
```

#### 6. 提供方法：申请 overlay 窗口权限，可将副屏设置为持久窗口
```
SubScreenPlugin.requestOverlayPermission();
```

#### 7. 提供方法：开启，关闭副屏
```
SubScreenPlugin.doubleScreenShow();     //开启
SubScreenPlugin.doubleScreenCancel();   //关闭
```

#### 7. 支付设置初始化完成后直接显示副屏
```
android -> values -> attrs.xml 添加配置

<!-- 是否在初始化时自动显示副屏 -->
<bool name="autoShowSubScreenWhenInit">true</bool> 
```


#### 8. 支持对副屏engine进行三方插件扩展
```
android -> mainActivity -> onCreate 方法添加 

// 例如：在副屏引入了 camera: ^lastedVersion , 则需要在 onCreate super 方法后加入如下语句进行注册

FlutterSubscreenPlugin.registerThirdPlugins(
    arrayListOf(
        io.flutter.plugins.camera.CameraPlugin(), // 对应的三方库名
    ),
    this.flutterEngine!!.plugins
)

```

#### 架构调用关系如下：
![image](https://github.com/liyufengrex/flutter_subscreen_plugin/assets/48038749/c01ad8a8-49a9-4ecf-bbd3-76287caf6350)

> 以上使用方式，完整样例可参照插件中的example

> [点击查看原理文档](https://juejin.cn/post/7007678468020240414)
