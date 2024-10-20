## Flutter 双屏通信引擎

![flutter_subscreen_plugin_icon](https://github.com/user-attachments/assets/5de5ab17-c9d6-4686-852e-4874a4810892)

### 引言

支持收银应用的**双屏交互场景**，如：主屏（操作屏）+ 副屏（客显屏）

支持双屏安卓设备，主副屏均使用 flutter 进行开发，提供方法实现双屏间的通信交互。

<br/>

### 新老方案对比

新方案统一技术栈，主副屏都使用 flutter 进行开发，降低开发及后期维护成本，通过创新的双引擎通信机制，确保了主副屏之间的高效交互。

![image](https://github.com/liyufengrex/flutter_subscreen_plugin/assets/48038749/df1cca6a-4596-46bf-b40f-11f83331770a)

### 接入依赖

在pubspec.yaml文件中进行引用：

<br/>

```dart
dependencies:
  flutter:
    sdk: flutter
  flutter_subscreen_plugin: ^1.0.8
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

### 整体调用关系架构如下：
![image](https://github.com/liyufengrex/flutter_subscreen_plugin/assets/48038749/c01ad8a8-49a9-4ecf-bbd3-76287caf6350)

### 运行效果图

完成上述步骤，简单的demo就做好了，如下是demo在实体设备的运行效果图：

![111](https://github.com/user-attachments/assets/272cb346-1000-49c5-94c7-dd4d6e8a9bdd)
（主屏）

![112](https://github.com/user-attachments/assets/42fd0fe4-16f1-473b-aef7-81c3be92aa90)
（副屏）

![113](https://github.com/user-attachments/assets/fad949a8-fb39-42be-8446-196208469ee2)
（点击按键，接收主屏数据的副屏）

<br/>

> 以上使用方式，完整样例可参照插件中的example

<br/>

> [点击查看原理文档](https://juejin.cn/post/7007678468020240414)
