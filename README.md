## Flutter 双屏支持插件

该插件支持双屏安卓设备，主副屏使用Flutter进行绘制，使用channel实现双屏间的通信交互。

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

#### 5. 支持使用 res - value 文件进行品牌过滤（下面的示例表示，华为荣耀的设备不开启双屏）
```
android -> res -> values -> attrs.xml 

<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="filterBrands">
       <item>huawei</item>-->
       <item>honor</item>-->
    </string-array>
</resources>
```


> 以上使用方式，完整样例可参照插件中的example
