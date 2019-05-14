# PasswordView
开发PasswordView SDK（PasswordView是一款基于EidtView等类进行扩展开发的自定义组件（或控件），界面简洁雅观，支持多位字符输入）

PasswordView SDK开发步骤请进入本博主SDKDemo项目中详细浏览，链接为：https://github.com/HCLOO/SDKDemo

PasswordView使用步骤：

1.在project的build.gradle文件中添加以下代码： 
allprojects {
   repositories {
       ......
       maven { url "https://raw.githubusercontent.com/HCLOO/PasswordView/master" }
   }
}

在module的build.gradle文件中添加以下代码： 
dependencies { 
   ...... 
   implementation 'com.example:passwordview:1.0.1' 
}

2.在XML布局文件中引入：
<com.example.passworview.PasswordView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="10dp"
        app:Num="6"
        app:Title="密码输入框"
        app:titleSize="16sp"
        app:titleColor="@color/titleColor"
        app:textSize="28sp"
        app:textColor="@color/passwordColor"
        android:layout_centerInParent="true"/>
        
3.属性含义解析：
Num：密码字符个数
Title：密码输入框标题
titleSize：密码输入框标题字体大小
titleColor：密码输入框标题字体颜色
textSize：密码字符字体大小
textColor：密码字符字体颜色
