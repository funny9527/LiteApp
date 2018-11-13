# LiteApp
Lite app engine for Android like wechat
一个android平台类似微信小程序引擎

This is a lite engine for running javascript base app on Android.  However, Component and Css are mapped into native View and Styles. 

一个android平台的javascript小程序执行引擎，组建和样式都会映射到本地平台对应的视图和风格。

What to do next:
1. The virtual dom currently is faked and need to be implemented.
2. Support common documents and views.
3. Threads between native and javascript should be Optimzed.
4. Javascript environment should be completed.
5. implementation for wechat apis

目前还没有实现的功能：
1. 实现虚拟dom
2. 支持w3c规范
3. 优化本地线程和js线程的交互
4. 支持小程序运行和调试环境
5. 兼容微信小程序api
