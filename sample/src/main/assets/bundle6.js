var console
var global = {
    main : function(a, b) {
        return 'js function callback <main> called ' + a + '  ' + b;
    },

    BridgeBatch: {
         AppInfo : {
             init: function() {
                 console = global.NativeModules.Console
                 console.log("init .... " + global.BridgeBatch[global.pages[0]].data.show_text)
             },
             getPages: function() {
                 return JSON.stringify(global.pages)
             },
             getWindowStyle: function() {
                 return JSON.stringify(global.window)
             }
         },
         App: {
               onLaunch: function () {
                   console.log("onLaunch");
               },
               onHide: function () {
                   console.log("onHide");
               },
             },
         Page: {
               onLoad: function (options, index) {
                   return global.BridgeBatch[global.pages[index]].onLoad(options)
               },

               onReady: function (index) {
                   global.BridgeBatch[global.pages[index]].onReady()
               },

               onShow: function (index) {
                   global.BridgeBatch[global.pages[index]].onShow()
               },

               onHide: function (index) {
                   global.BridgeBatch[global.pages[index]].onHide()
               },

               onUnload: function (index) {
                   global.BridgeBatch[global.pages[index]].onUnload()
               },

               onTabItemTap: function (index, id) {
                   global.BridgeBatch[global.pages[index]].onTabItemTap(id)
               }
              },

         //page for sample
         Login: {
               data: {
                   show_text:"login page"
               },

               onLoad: function (options) {
                   return '<view class="container"><view class="usermotto"height="800px" width="800px" color="#000000"></view><button bindtap="bindViewTap" height="140px" width="240px" >下一页</button></view>'
               },

               onReady: function () {
               },

               onShow: function () {
                   console.log("Login onShow")
               },

               onHide: function () {
               },

               onUnload: function () {
               },
               onTabItemTap: function (id, name) {
                   //name为需要调用的函数名，为了简单，先不实现
                   console.log(id + " clicked " + name)
                   global.NativeModules.Navigator.toNext("Show")
               }
              },
         Show: {
               data: {
                   show_text:"show page"
               },

               onLoad: function (options) {
                   return '<view class="container"><view class="usermotto"height="800px" width="800px" color="#33000000"></view><button bindtap="bindViewTap" height="140px" width="240px" >上一页</button></view>'
               },

               onReady: function () {
               },

               onShow: function () {
               },

               onHide: function () {
               },

               onUnload: function () {
               },
               onTabItemTap: function (id, name) {
                   //name为需要调用的函数名，为了简单，先不实现
                   console.log(id + " clicked " + name)
                   global.NativeModules.Navigator.toBack();
               }
              },
    },
    //for samples
    pages: [
        "Login",
        "Show"
    ],
    //for samples
    window: {
        "navigationBarTitleText":"测试小程序6",
        "backgroundTextStyle": "light",
        "navigationBarTextStyle": "#ffff00",
    },
}