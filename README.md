# KV 一款Android端0反射的持久化存储封装

[![license](https://img.shields.io/github/license/WrBug/kv.svg?style=flat-square)](https://github.com/WrBug/kv/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.wrbug.kv/kv.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.wrbug.kv)
[![](https://img.shields.io/github/last-commit/WrBug/kv.svg?style=flat-square)](https://github.com/WrBug/kv/commits)
[![](https://img.shields.io/travis/WrBug/kv.svg?style=flat-square)](https://travis-ci.org/WrBug/kv)

> 告别需要定义key的烦恼，几行到没轻松接入使用，支持 **sharedpreference** **mmkv** 等持久化方案，支持跨模块调用

## 快速接入

### 接入

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

`@KVMultiModule` 仅用于标记主工程，可以在主工程任意类添加该注解

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
定义规则参考[Interface方法名匹配规则](#Interface方法名匹配规则)

#### 调用方式

``` kotlin
//获取AccountLocalSource实例
val source = KV.get(AccountLocalSource::class.java)

//存储id
source?.setUsername("WrBug")

//获取id
val username=source?.getUsername()

//删除username
source?.removeUsername()

```

### 更多扩展

#### Interface方法名匹配规则

``` kotlin
//支持自定义key，scope，key默认为类名，scope默认为空字符串
@KV("key",scope="scope")
interface AccountLocalSource {

    //@KVAliasName可自定义持久化key
    @KVAliasName("uid")
    fun setUserId(uid:Long)
    
    //匹配set开头的方法，xxx为持久化的key
    //传参仅支持一个，可以为任意类型，复杂对象类型通过 ObjectConverter 转换为可存储类型
    fun setXXX(username:String)
    
    //匹配@KVPut注解
    @KVPut("xxx")
    fun saveXXX(obj:Any)
    
    
    //匹配get开头的方法，xxx为持久化的key，返回类型应与set方法传参一致
    fun getXXX():String
    
    //get方法支持默认值
    fun getXXX(defaultAge:Int):Int
    
    //匹配@KVGet注解
    @KVGet("xxx")
    fun fetchXXX():Any
    
    
    //匹配remove方法，移除该key
    fun removeXXX()
    @KVRemove("xxx")
    fun deleteXXX()
    
    //匹配clear方法，清空所有数据
    fun clear()
    @KVClear
    fun removeAll()
}
```

#### 自定义DataProvider

[`DataProvider`](/kv/src/main/java/com/wrbug/kv/DataProvider.kt)用于实现储存策略。默认[`SharedPreferenceDataProvider`](/kv/src/main/java/com/wrbug/kv/SharedPreferenceDataProvider.kt) 使用SharedPreference储存，业务方可以根据需要自行实现DataProvider，目前仅支持一个自定义DataProvider

``` kotlin
@KVDataProvider
class CustomDataProvider : DataProvider{
    //impl
}
```
Demo 提供了[`MMKVDataProvider`](/app/src/main/java/com/wrbug/kv/sample/dataprovider/MMKVDataProvider.kt)的实现可供参考
#### 自定义ObjectConverter

[`ObjectConverter`](/kv/src/main/java/com/wrbug/kv/ObjectConverter)用于对复杂对象存储转换，默认使用[`JsonObjectConverter`](/kv/src/main/java/com/wrbug/kv/JsonObjectConverter)转为json存储

``` kotlin
object CustomObjectConverter : ObjectConverter {
    //impl
}

//应在初始化完成后设置
KV.setObjectConverter(CustomObjectConverter)
```