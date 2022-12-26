# Demo

一些工具类和通用代码库

## 通过Breakpad定位Native Crash

Breakpad的介绍和编译详见下面文章

[https://www.jianshu.com/p/0bfe7800bdef](https://www.jianshu.com/p/0bfe7800bdef)

### 1.集成Breakpad

```
1.implementation("io.github.superbigjian.libs:breakpad:1.0.1")

//在崩溃代码之前调用，一般来说，crash捕获初始化都会放到Application中。
2.BreakpadDumper.initBreakpad(externalReportPath.absolutePath)
```

### 2.解析dump文件

将步骤一生成的xxxx.dump文件push到电脑中，通过minidump_stackwalk进行解析。

注：AS中已经自带了该工具。

```
D:\01.WorkSpace\GitHub\breakpad\crashDump>C:\Program Files\Android\Android Studio\plugins\android-ndk\resources\lldb\bin\minidump_stackwalk.exe be5015dd-53dd-4538-8c341aaf-34f8e96a.dmp > crash.txt
```

解析出的文件如下：


![1](https://raw.githubusercontent.com/SuperBigJian/Images/main/breakpad/1.jpg)

红框中为报错的堆栈信息，以及报错的代码具体位置。

### 3.定位崩溃位置

调用ndk中的工具 llvm-addr2line 定位具体行号，结果如下


![2](https://raw.githubusercontent.com/SuperBigJian/Images/main/breakpad/2.png)
