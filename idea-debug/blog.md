# 引言

本来通过问题引入，一步一步对问题进行分析，重点学习 IDEA Debug 的能力解决问题。阅读本文可以学习如何通过 IDEA 的 Debug 功能解决实际问题。本文适合学生和刚工作的朋友，把 IDEA 作为开发工具，并且有 Spring 和 JPA 的使用经验。

# 问题引入

最近看了 eclipse 开源的集合 [Eclipse Collections](https://github.com/eclipse/eclipse-collections)，觉得它的使用相比 JDK 集合更加简洁，想在实际项目中使用。部分 API 对比如下。

JDK API

```java
 //users is List<User> 
 users.stream.map(user -> user.getCity()).collect(Collectors.toList());
```

Eclipse Collections API

```java 
 //users is MutableList<User>
 users.collect(user -> user.getCity);
```
可以看到后者比前者要简洁不少。实际开发中集合数据大多还是来自数据库查询，使用 JPA 查询如下。

JDK API

```java
List<User> findByCity(String city);
```

我想改成 Eclipse Collections API

```java
MutableList<User> findByCity(String city);
```

然而报错了

```java
org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.util.ArrayList<?>] to type [org.eclipse.collections.api.list.MutableList<?>] for value '[]'; nested exception is java.lang.IllegalArgumentException: Unsupported Collection interface: org.eclipse.collections.api.list.MutableList
    at org.springframework.core.convert.support.ConversionUtils.invokeConverter(ConversionUtils.java:47)
    at org.springframework.core.convert.support.GenericConversionService.convert(GenericConversionService.java:192)
    at org.springframework.core.convert.support.GenericConversionService.convert(GenericConversionService.java:175)
```

如果不想看过过程，结论是改成如下代码或者升级 sping boot 到 2.7.0 及以上版本。

```java
FastList<User> findByCity(String city);
```

# Debug

## 对代码简单分析

- 查看类名称，方法名称。 有 `convert.ConversionFailedException`/`ConversionUtils.invokeConverter`/`convert.support.GenericConversionService.convert`等等，关键词 `convert`。我们应该联想到这段代码的功能是把某一个类型 `convert` 到某一个类型。
- 再看一眼报错信息，`Failed to convert from type [java.util.ArrayList<?>] to type [org.eclipse.collections.api.list.MutableList<?>]`，无法将 `ArrayList` 转换成 `MutableList`。
- 再分析报错的那一行`return converter.convert(source, sourceType, targetType)`，我们会更清晰一点。
  - `result` 是转换的结果，应该是 `MutableList` 的一个实例。
  - `convert` 方法是执行转换的核心逻辑，我们要的核心转换逻辑代码肯定在这里，如果你直接去看的话，它肯定是一个接口，这是面向接口编程。
  - `sourceType` 源类型，是 `ArrayList` 类型。
  - `targetType` 目标类型，是 `MutableList` 类型。

## 打断点

在 IDEA 控制台可以直接点击报错 class 定位到源文件，我们先点击 `ConversionFailedException` ，再点击 `ConversionUtils.java:47`，发现都是报错的异常，对我们没有帮助。最后我们点击 `GenericConversionService.java:192`，终于看到一行代码了。

```java
Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
```

## 断点分析

执行过程会停留在断点处，我们可以查看上下文变量类的实例。这里我们以 `converter` 为例。按照数字步骤点击，如下。

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/43d89ab63cf24194a5fe5ebe573261b9~tplv-k3u1fbpfcp-watermark.image?)

图中显示是 `Converter` 的 `toString` 方法的结果。
可能的 `converter` 如下：

```java
1. java.lang.String -> java.lang.Enum
2. NO_OP
3. java.lang.Boolean -> java.lang.String
// 等等。。。。。
```

由于是底层方法，被调用的次数很多，在这个断点停留的次数也很多。很多次不是我们想要的 `Converter`。

## 条件断点

顾名思义 IDEA 会通过我们添加的条件来判断这个断点是否需要被处理。

我们想要的 `Converter` 是什么呢？回到代码分析阶段，我们想要的 `Converter` 是 `sourceType` &rarr; `targetType`，通过上面分析 `targetType` 类型是 `MutableList` 类型。

下面添加条件断点：

<img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1dfaf40004c34ca684842ee6c0dd9271~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="70%" />

完整的条件如下：

```java
MutableList.class.isAssignableFrom(targetType.getType());
```

添加成功的标志如下，会在断点处显示问号。

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4759d23d217a47b29e2fbe8ab7acb656~tplv-k3u1fbpfcp-watermark.image?)

## 单步调试

Debug 模式启动程序，可以看到 IDEA 停留在我们的条件断点上，并且`targetType` 的类型正是 `MutableList`。

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/207bc716bd74496e90ceba0579c09a96~tplv-k3u1fbpfcp-watermark.image?)

单步调试代码，来到 `org.springframework.core.CollectionFactory#createCollection` 方法。部分代码如下：

```java
//省略的其他代码

// 判断集合类型是不是 ArrayList 或者 List，显然这里不是
else if (ArrayList.class == collectionType || List.class == collectionType) {
  return new ArrayList<>(capacity);
}
//省略的其他代码

else {
//如果是集合类型的接口 或者 不是集合类型抛出异常
  if (collectionType.isInterface() || !Collection.class.isAssignableFrom(collectionType)) {
    throw new IllegalArgumentException("Unsupported Collection type: " + collectionType.getName());
  }
  try {
  //如果是集合类型的类，直接通过反射实例化。
    return (Collection<E>) ReflectionUtils.accessibleConstructor(collectionType).newInstance();
  }
}
```

## 重回代码分析

上面的 `collectionType` 是 `MutableList`，而 `MutableList` 是接口，走读代码可以发现最终会执行下面的代码，最终导致抛出异常。

```java
if (collectionType.isInterface() || !Collection.class.isAssignableFrom(collectionType)) {
    throw new IllegalArgumentException("Unsupported Collection type: " + collectionType.getName());
  }
```

所以只需要我们的目标集合不是接口就行了，`FastList` 是 `MutableList` 的实现类。 修改代码为如下：

```java
FastList<User> findByCity(String city);
```

**翻看控制台找到了下面的异常信息，这也侧面反映我们之前找的报错位置不是很精确。我们寻找异常时应该选择最原始的异常信息。**

```java
Caused by: java.lang.IllegalArgumentException: Unsupported Collection type: org.eclipse.collections.api.list.MutableList
	at org.springframework.core.CollectionFactory.createCollection(CollectionFactory.java:205)
	at org.springframework.core.convert.support.CollectionToCollectionConverter.convert(CollectionToCollectionConverter.java:81)
```

继续分析源码可以发现，如果我们定义的类型不是接口，`JPA` 就会通过反射创建集合，即如下代码：

```java
return (Collection<E>) ReflectionUtils.accessibleConstructor(collectionType).newInstance();
```

# 总结

本来通过解决实际问题介绍了 IDEA Debug 功能的使用。还有以下几点需要注意。

- 查找异常时要定位到最初始的异常，这样往往能迅速处理问题。
- 本文的问题只有在 sping boot 2.7.0 以下才会出现，高版本已经修复此问题。参见提交 [spring data common](https://github.com/spring-projects/spring-data-commons/commit/deceb867c5afdc6d185171c4eb1f8e3582249a9c)。
- 使用非 Java 官方集合需要进行转换，有微小的性能损耗，对于常规内存操作来说影响很小，而且高版本中有优化。如果查询数据上千上万条时，应该避免转换，当然也要使用分页避免一次性查询成千上万的数据。

本文[源码](https://github.com/yoa1226/blogs/tree/main/idea-debug)