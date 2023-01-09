# blogs

This repo is for blog source code.

## Tools require

| tool   | version |
|--------|---------|
| Java   | 17+     |
| Maven  | 3.8+    |
| Spring | 2.7.0-  |

## How to get code.

```shell
git clone git@github.com:yoa1226/blogs.git
```

## Build and Run test

build and run test whole project

```shell
mvn clean package
```

build and run test specify module

```shell
mvn test -Dtest=testname -pl subproject
```

## Blog list

### 掘金

- [通过问题透析 IDEA Debug](https://juejin.cn/post/7185569129024192568)
- [Git 之命令行工具](https://juejin.cn/post/7186635257285148732)

### GitHub

- [通过问题透析 IDEA Debug](idea-debug/blog.md)
- [Git 之命令行工具](git-command.md)

