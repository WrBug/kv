# KV 一款Android端0反射的持久化存储封装

> 告别需要定义key的烦恼，几行到没轻松接入使用，支持 **sharedpreference** **mmkv** 等持久化方案，支持跨模块调用

## 快速接入

### 依赖接入

KV最新版本： [![Maven Central](https://img.shields.io/maven-central/v/com.wrbug.kv/kv.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.wrbug.kv)

工程根目录build.gradle添加

``` groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.wrbug.kv:kv-gradle:${kvVersion}")
    }
}

```

宿主工程和module build.gradle添加

``` groovy
//plugin仅宿主工程启用即可
apply plugin: 'com.wrbug.kv.gradle'

dependencies {
    implementation "com.wrbug.kv:kv:${kvVersion}"
    kapt "com.wrbug.kv:kv-compile:${kvVersion}"
}

```

### 初始化

``` kotlin
@KVMultiModule
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KV.init(this)
    }
}
```

## 快速使用

### 新增Interface

``` kotlin
@KV
interface AccountLocalSource {
    fun setUsername(username:String)
    fun getUsername():String
    
    fun setAge(age:Int)
    fun getAge(defaultAge:Int):Int
    
    fun removeUsername()
    
    fun clear()
}
```


#### 调用方式

``` kotlin

val source = KV.get(AccountLocalSource::class.java)

//存储id
source?.setUsername("WrBug")

//获取id
val username=source?.getUsername()

//删除username
source?.removeUsername()

```


