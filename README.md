# OkHttpPan

OkHttpPan 是一个 封装了 OkHttp 和 FastJson 的网络请求层，遵循建造者设计模式。

## Example

1. 在 Application 中 初始化并设置 OkHttpPan
    ```java
        // 初始化 OkHttpPan
        OkHttpPan.initialization();
    ```

    或者

    ```java
       // 配置Response解析器、timeout信息，配置信息 均可选
       OkHttpPanConfig config = new OkHttpPanConfig.Builder()
           .responseParser(new ResponseParser)
           .readTimeout(15)
           .readTimeout(25)
           .writeTimeout(35)
           .Build();
      // 初始化 OkHttpPan
      OkHttpPan.initialization(config);
      // 打开 debug log
       OkHttpPan.setDebug(true);
    ```

2.调用

  * 共同参数优先级高于 OkHttpPanConfig 中的配置

    ```
    .responseParser(responseParser)
    .connectTimeout(int)
    .readTimeout(int)
    .writeTimeout(int)
    .build().enqueue(<$Entity>.class, callback);
    ```

  * get 请求

    ```java
    OkHttpPan.get()
        .url(URL)
        .params("XXO", xxo)
        .build().execute(Object.class, callback);
    ```

  * post 请求

    ```java
    OkHttpPan.post()
        .url(URL)
        .params("XXO", xxo)
        .build().execute(Object.class, callback);
    ```

  * upload 请求

    ```java
    OkHttpPan.upload()
        .url(URL)
        .files("Key", Map<String, File> files)
        .build().execute(Object.class, callback);
    ```

  * download 请求

    ```java
    OkHttpPan.download()
        .url(URL)
        .savePath(String savePath)
        .saveFileName(String saveFileName)
        .build().execute(Object.class, callback);
    ```

详细的例子查看源码 >> OkHttpPanDemo

## License

```
Copyright 1999-2017 Shper.cn Holding Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```