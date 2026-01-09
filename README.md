!![flutter_subscreen_plugin_icon](https://github.com/user-attachments/assets/5de5ab17-c9d6-4686-852e-4874a4810892)

# Flutter 双屏通信引擎

<div align="">
  <img src="https://img.shields.io/github/last-commit/liyufengrex/flutter_subscreen_plugin?style=flat-square&logo=github&logoSize=14" alt="Last Commit" height="20">
  <img src="https://img.shields.io/github/forks/liyufengrex/flutter_subscreen_plugin?style=flat-square&logo=github&color=blueviolet&logoSize=14" alt="Forks" height="20">
  <img src="https://gitcode.com/cashier/flutter_subscreen_plugin/star/badge.svg?style=flat-square&logoSize=14" alt="GitCode Star" height="20">
</div>
</br>


## 一、引言

本插件专为支持收银应用等**双屏交互场景**设计，适用于主屏（操作屏）+ 副屏（客显屏）的搭配模式。

核心优势：支持双屏安卓设备，主副屏均采用 Flutter 开发，提供简洁易用的 API 实现双屏间的高效通信交互。

## 二、新老方案对比

新方案通过统一技术栈（主副屏均使用 Flutter），大幅降低开发及后期维护成本；创新的双引擎通信机制，确保主副屏之间交互的高效性与稳定性。

![image](https://github.com/liyufengrex/flutter_subscreen_plugin/assets/48038749/df1cca6a-4596-46bf-b40f-11f83331770a)

## 三、接入依赖

在项目的 `pubspec.yaml` 文件中添加以下依赖配置：

```dart

dependencies:
  flutter:
    sdk: flutter
  flutter_subscreen_plugin: ^1.0.8
```

## 四、使用方法

通过本插件，可快速完成主副屏的 UI 绘制及交互通信功能开发，具体步骤如下：

### 4.1 主副屏区分（main 入口）

在应用入口处，根据路由名称区分主副屏，分别加载对应的 UI 页面：

```dart

void main() {
  var defaultRouteName = window.defaultRouteName;
  if ("subMain" == defaultRouteName) {
    viceScreenMain(); // 加载副屏 UI
  } else {
    defaultMain();    // 加载主屏 UI
  }
}

// 主屏 UI 入口
void defaultMain() {
  runApp(MainApp());
}

// 副屏 UI 入口
void viceScreenMain() {
  runApp(SubApp());
}
```

### 4.2 主副屏通信

#### 4.2.1 主屏发送数据到副屏

调用 `sendMsgToViceScreen` 方法，可向副屏发送数据，支持携带参数：

```dart

SubScreenPlugin.sendMsgToViceScreen("data", params: {"params": "123"});
```

#### 4.2.2 副屏接收主屏数据

通过监听`viceStream` 流，可实时接收主屏发送的数据：

```dart

SubScreenPlugin.viceStream.listen((event) {
  print(event.arguments.toString()); // 打印接收的主屏数据
});
```

### 4.3 设备及权限相关方法

#### 4.3.1 检查设备是否支持双屏

调用 `isMultipleScreen` 方法，获取当前设备是否支持双屏的结果：

```dart

SubScreenPlugin.isMultipleScreen((result) {
  print("是否支持双屏：$result");
});
```

#### 4.3.2 检查 overlay 窗口权限

调用 `checkOverlayPermission` 方法，判断应用是否具备 overlay 窗口权限（副屏显示需依赖此权限）：

```dart

SubScreenPlugin.checkOverlayPermission((result) {
  print("是否支持 overlay：$result");
});
```

#### 4.3.3 申请 overlay 窗口权限

若应用未获取 overlay 窗口权限，可调用 `requestOverlayPermission` 方法申请，获取权限后可将副屏设置为持久窗口：

```dart

SubScreenPlugin.requestOverlayPermission();
```

#### 4.3.4 开启/关闭副屏

通过以下方法可手动控制副屏的显示与隐藏：

```dart

SubScreenPlugin.doubleScreenShow();     // 开启副屏
SubScreenPlugin.doubleScreenCancel();   // 关闭副屏
```

### 4.4 副屏自动显示配置

若需在应用初始化完成后直接显示副屏，可在 Android 原生配置文件中添加如下配置：

路径：`android -> values -> attrs.xml`

```xml

<!-- 是否在初始化时自动显示副屏 -->
<bool name="autoShowSubScreenWhenInit">true</bool>
```

### 4.5 副屏引擎三方插件扩展

若副屏需使用三方 Flutter 插件（如相机、地图等），需在 Android 原生的 `MainActivity` 的 `onCreate` 方法中进行插件注册，示例如下：

```kotlin

// 在 onCreate 方法的 super 调用后添加
FlutterSubscreenPlugin.registerThirdPlugins(
    arrayListOf(
        io.flutter.plugins.camera.CameraPlugin(), // 需注册的三方插件
    ),
    this.flutterEngine!!.plugins
)
```

## 五、整体调用关系架构

插件的核心调用流程及组件关系如下：

![image](https://github.com/liyufengrex/flutter_subscreen_plugin/assets/48038749/c01ad8a8-49a9-4ecf-bbd3-76287caf6350)

## 六、运行效果图

完成上述配置后，即可实现基础的双屏交互功能，以下是实体设备上的运行效果：

主屏效果：[点击查看](https://github.com/user-attachments/assets/272cb346-1000-49c5-94c7-dd4d6e8a9bdd)

副屏初始效果：[点击查看](https://github.com/user-attachments/assets/42fd0fe4-16f1-473b-aef7-81c3be92aa90)

副屏接收主屏数据效果：[点击查看](https://github.com/user-attachments/assets/fad949a8-fb39-42be-8446-196208469ee2)

## 七、补充说明

- 完整的使用示例代码，可参考插件包中的 `example` 目录。

- 如需了解插件实现原理，可查看详细文档：[点击查看原理文档](https://juejin.cn/post/7007678468020240414)
