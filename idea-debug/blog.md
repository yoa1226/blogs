# 引言
本来通过问题引入，透析 IDEA Debug，适合刚刚参加工作的工友。

# 问题引入
最近看了 eclipse 开源的集合 [Eclipse Collections](https://github.com/eclipse/eclipse-collections)，觉得它的 api 相比 JDK 集合 api 简洁，想在实际项目中使用，如下。

JDK api


```java
 // users is List<String> 
 users.stream.map(user -> user.getName()).collect(Collectors.toList());
```

Eclipse Collections api

```java 
 //users area MutableList
 users.collect(user -> user.getName);
```
但是项目中使用集合最多的地方还是数据库操作，如下。

JDK api

```java
List<User> findByAdress(String address);
```
我想改成

```java
MutableList<User> findByAdress(String address);
```
然而报错了

```java
    org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.util.ArrayList<?>] to type [org.eclipse.collections.api.list.MutableList<?>] for value '[]'; nested exception is java.lang.IllegalArgumentException: Unsupported Collection interface: org.eclipse.collections.api.list.MutableList
	at org.springframework.core.convert.support.ConversionUtils.invokeConverter(ConversionUtils.java:47)
	at org.springframework.core.convert.support.GenericConversionService.convert(GenericConversionService.java:192)
	at org.springframework.core.convert.support.GenericConversionService.convert(GenericConversionService.java:175)
```

# Debug

## 对代码简单分析

简单的分析过程，实际操作中心里分析即可。

- 报错的地方都是 `Spring` 的包，证明我们使用的 `Spring Data JPA` 访问数据库，事实上也是。
- 查看类名称，方法名称。 有 `convert.ConversionFailedException`/`convert.support.ConversionUtils.invokeConverter`/`convert.support.GenericConversionService.convert`等等，关键词 `convert`，我应该联想到这段代码的功能是把什么类型 `convert` 到什么类型。
- 再分析报错的那一行我们会更清晰一点。
    - `result` 是转换的结果。
    - `converter`是转换器，结合上面的结论，这个类肯定是真正执行转换的类，我们要的核心代码肯定在这里，如果你直接去看的话，它肯定是一个接口，面向接口编程。
    - `sourceType` 源类型，结合上述分析肯定是原始类型。
    - `targetType` 目标类型，同上不赘述。


## 打断点

IDEA 可以直接点击报错 class 定位到源文件，这里我们先点击 `ConversionFailedException` ，再点击 `ConversionUtils.java:47`，发现都是报错的异常，对我们没有帮助。最后我们点击 `GenericConversionService.java:192`，终于看到一行代码了。

```java
Object result = 
ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
```
## 断点分析

执行过程会停留在断点处，我们可以查看上下文变量类的实例。这里我们以 `converter` 为例。按照数字步骤点击，如下。

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/43d89ab63cf24194a5fe5ebe573261b9~tplv-k3u1fbpfcp-watermark.image?)

`converter` 如下：

```java
1. java.lang.String -> java.lang.Enum
2. NO_OP
3. java.lang.Boolean -> java.lang.String
等等。。。。。
```
我们发现 `converter` 很多点击了很多次都不是我们想要的 `converter`。

## 条件断点

顾名思义是通过条件判断这个断点是不是 IDEA 需要处理的断点。

我们想要的 `converter` 是什么呢？回到代码分析阶段，我们想要的 `converter` 是 `sourceType` &rarr; `targetType`，`targetType` 类型是什么呢？回到我们自己写的代码。

```java
MutableList<User> findByAdress(String address);
```
可以看到我们的 `targetType` 是 `MutableList`。

添加条件断点如下步骤：

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d61e5e61e5a046859f1353235fb994dc~tplv-k3u1fbpfcp-watermark.image?)

添加成功的标志如下。

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4759d23d217a47b29e2fbe8ab7acb656~tplv-k3u1fbpfcp-watermark.image?)

未完。。。。。


