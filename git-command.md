# 引言

本文介绍常用的命令行工具以及常用的 Git 命令，让读者熟练掌握命令行下 Git 的使用，享受命令行编程的乐趣。本文适合使用过 Git，最好熟悉 但是对其在命令行下使用不熟悉的读者，读者最好对 vim 操作有一定的了解。

# 软件安装

### Iterm2

[iTerm2 - macOS Terminal Replacement](https://iterm2.com/)是一个 Mac 下的终端工具。进入官网下载安装即可，下面是官网介绍。
> iTerm2 is a replacement for Terminal and the successor to iTerm. It works on Macs with macOS 10.14 or newer. iTerm2 brings the terminal into the modern age with features you never knew you always wanted.

### Oh My ZSH

[Oh My Zsh](https://ohmyz.sh/) 是一个轻量的、开源的、社区驱动管理 [zsh](https://www.zsh.org/) 配置的工具。下面是官网介绍。

> Oh My Zsh is a delightful, open source, community-driven framework for managing your Zsh configuration

在命令行中执行下面的命令安装 Oh My ZSH，其他安装方式见 [oh my zsh github](https://github.com/ohmyzsh/ohmyzsh) 或者 [Oh My Zsh](https://ohmyz.sh/#install)。

```shell
sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"
```

### Oh My ZSH 插件

安装好 Oh My ZSH 之后当前用户目录下会有一个配置文件叫做 `.zshrc`。

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a37275f7311d40468ce833b06daad852~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="50%" />

其中一行是我们的插件配置，使用的 `oh my zsh` 插件都必须添加进去，如下：

<img src="https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1a6a3c5645bc42d79b7fe201293a5092~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="70%" />

#### git 插件

默认打开，无需配置，git 是后面的重要主题。

#### Z 插件

[Z](https://github.com/agkozak/zsh-z) 目录跳转插件。注意安装插件后，只有再次访问过的目录才会被 Z 识别。添加到 `oh my zsh` 的配置文件 `～/.zshrc` 中，见插件配置。

<img src="https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/42ecb162b17a4b5e8cb9d5867f44e855~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="50%" />

如图，我们第一次使用 `z mytest` 没有进入 `mytest` 目录，当我们使用 `cd mytest` 以后再次尝试成功进入 `mytest` 。事实上我们使用 `z my` 和 `z test` 都是可以的。

#### zsh-autosuggestions

[zsh-autosuggestions](https://github.com/zsh-users/zsh-autosuggestions) 是一款命令行提示插件。使用下面的命令安装，并且添加到 `oh my zsh` 的配置文件 `～/.zshrc` 中，见插件配置。

```
git clone https://github.com/zsh-users/zsh-autosuggestions ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-autosuggestions
```

下图中用红色标出的灰色部分是提示部分。

<img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/9757a590fe96417ba6f6e013fc312736~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="30%" />

#### git-open

[git-open](https://github.com/paulirish/git-open) git-open 是一款快速在命令用浏览器快速打开当前 git 项目的的插件。使用下面命令进行安装，并且添加到 `oh my zsh` 的配置文件 `～/.zshrc` 中，见插件配置。

```shell
git clone https://github.com/paulirish/git-open.git $ZSH_CUSTOM/plugins/git-open
```
在仓库目录执行 `git-open` 浏览器就能帮你打开当前项目。

### Git 命令行 gui

[extrawurst/gitui](https://github.com/extrawurst/gitui) 是一款命令行 ui 工具。不是本文重点，作为推荐工具，感兴趣的朋友参见官网学习。效果图如下：

<img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/14e6f57432e74001868ab402acece9b2~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="80%" />

# Git

## 基本设置

### git config

`git config` 命令用于设置 git 仓库的配置。全局配置文件在 `~/.gitconfig`，仓库的配置文件在 `you-repo/.git/config`。通常将自己的个人配置设置为全局的，公司的配置按照仓库设置。

**配置用户名和邮箱**

```shell
# 仓库配置
git config  user.name "your name"
git config user.email 'yourname@gmail.cn'

# 全局配置
git config --global user.name "your name"
git config --global user.email 'yourname@gmail.cn'
```

**配置拉取代码的合并模式**，git默认使用 merge 模式，当前本地和远程都有修改时，这种模式会生成新的不必要的提交，如下为原来 commit：


![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/6c9a3294d5254726becd9fa4c85271a7~tplv-k3u1fbpfcp-watermark.image?)

执行 `git pull` 之后的 commit：

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b92ef25f29c04cd586e1366ee9ce929e~tplv-k3u1fbpfcp-watermark.image?)

可以看到我们多了一个合并代码的提交，因为在同一个分支，这个合并提交很可能是我们不需要的。为此我们可以用下面的命令设置，建议设置为全局：

```shell
git config --global pull.rebase true
```

## 重要命令

接下来结合 `oh my zsh` 的 git 插件，着重介绍开发最常用 git 命令。git 插件的配置文件在 `～/.oh-my-zsh/plugins/git/git.plugin.zsh`，主要是对常用 git 命令进行别名。我们通过下列命令查询相关命令的别名：

```shell
alias | grep "git commit"
```
效果如下：

<img src="https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/00b9321d67864e6eb76fc2200ff33c6c~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="50%" />

下面主要通过别名介绍相关命令。

### git status

查看当前仓库的状态。这个命令非常重要，每次执行操作前后都要看一下状态，当前状态是否适合执行命令和是否执行命令成功。

```shell
gst='git status'
```

### git commit

commit命令记录改变，主要掌握两个命令。

命令一：提交一个修改到新的提交
```shell
# 别名
gcam='git commit -a -m'

# 实例
gcam "feat: this is a commit messsage"
```
命令二：提交当前修改到前一个提交，并且不编辑提交消息。
```shell
# 别名
gcan!='git commit -v -a --no-edit --amend'

# 实例
gcan!
```

### git checkout

命令一：创建分支并切换

```shell
# 别名
gcb='git checkout -b'

# 实例
gcb feat/user_module_dev
```

命令二：切换到某个分支或者提交

```shell
# 别名
gco='git checkout'

# 实例
gc0 feat/user_module_dev
gco 31c98c8
gco - # 切回上一个分支
gcm #切换到主分支
gcd #切换到开发分支
```
### git log

命令一：查看当前分支的 log

```shell
# 别名
glol='git log --graph --pretty='\''%Cred%h%Creset -%C(auto)%d%Creset %s %Cgreen(%ar) %C(bold blue)<%an>%Creset'\'

# 实例
glol
```
命令二：查看当前分支的 log，并且显示每次修改的文件。

```shell
# 别名
glols='git log --graph --pretty='\''%Cred%h%Creset -%C(auto)%d%Creset %s %Cgreen(%ar) %C(bold blue)<%an>%Creset'\'' --stat'

# 实例
glols
```

### git diff

命令一： 查看工作区的修改

```shell
# 别名
gd='git diff'

# 实例
gd
```

命令二： 查看暂存区的修改

```shell
# 别名
gds='git diff --staged'

# 实例
gds
```

### git show

查看每次提交的内容

```shell
# 别名
gsh='git show'

# 实例
gsh # 查看当前最新提交
gsh HEAD # 同上，也可以用 gsh head

gsh HEAD～1 # 查看次新提交，也可以用 gsh head^
gsh HEAD～2 # 查看次次新提交，也可以用 gsh head^^
```

### git push

命令一：强制推送代码到远程，有冲突时则拒绝。

```shell
# 别名
gpf='git push --force-with-lease'

# 实例
gpf
```

命令二：将本地新分支推送到远程

```shell
# 别名
gpsup='git push --set-upstream origin $(git_current_branch)'

# 实例
gpsup
```

### git reset

命令一：git reset --hard
此命令会丢弃修改，慎用。

```shell
# 别名
grhh='git reset --hard'

# 实例
grhh # 丢弃工作区和暂存区的改动
grhh head^ # 丢弃最新的提交
grhh head^^ # 丢弃最新的和次新的提交

grhh 31c98 # 删除比 31c98 新的提交
```

命令一：git reset --soft

```shell
# 别名
grh='git reset'

# 实例
grh --soft 31c98 # 将比 31c98 新的所有改动移动到暂存区，适合将许多个提交合并成一个的场景。
```

### git rebase

前面介绍的命令都比较简单易学，下面开始介绍 `git rebase` 命令，它相对复杂，但是也相对重要很多。看看[Git - git-rebase Documentation](https://git-scm.com/docs/git-rebase)解释。

> git-rebase - Reapply commits on top of another base tip

一句话解释就是，将某些提交变基。看一个官方的例子。

<img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/44b16b9fbbb64f09a7b82b9acbcdf152~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="70%" />

topic 是基于 master 切出来的分支，已经有了 ABC 三个新的提交。现在 topic 分支想要更新 master 分支的代码，可以在 topic 上使用下面的命令。

```shel
# 别名
grb='git rebase'
grbm='git rebase $(git_main_branch)'

# 实例
grb master
grbm
```
下面执行完成功之后的样子。

<img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0b8a3c007d644b448fb9b88293bf22e0~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="70%" />

#### git rebase 有冲突的情况

topic 分支有多个领先 master 分支的提交，`git rebase` 是一个一个分别处理。比如上文所示，先处理 A 提交，没有冲突或者冲突处理完成以后再处理 B 提交，以此类推。下面介绍有冲突的情况。

有一个名称为 rebase_main 的分支，最新提交如下：

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/60615034cfcf4412a8ba7468ab20388b~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="70%" />

另外有一个名称为 rebase_feat 的分支，最新提交如下：

<img src="https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/623f8a4a926f4df097bb49d390395dfa~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="70%" />

接下来在 rebase_feat 分支执行 `grb rebase_main`，结果如下。

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e688c7b82e7048d9aaf4c877cc297b9a~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="85%" />

根据 Git 提示，我们知道以下几点：

- rebase 操作导致了文件内容冲突，文件名称为 README.md。
- 我们必须手动解决冲突，并且标记冲突已经解决。
- 解决冲突后，我们要使用 `git add` 表明已经解决了冲突，然后使用 `git rebase --continue` 命令告诉 Git 继续执行 rebase 操作。
- 使用 `git rebase --skip` 命令丢弃当前冲突的提交，即 rebase_feat 分支的最新提交。
- 使用 `git rebase --abort` 命令放弃本次 rebase 操作，回到之前的状态。

下面我们开始解决冲突：

第一步：查看冲突的文件，删除或者合并代码。

合并之前

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/92840dbd318246cea7de3ea471245442~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="70%" />

怎么合并代码按照你的需求决定，可以选择只留下 rebase_main 分支的内容，或者只留下 rebase_feat 分支的内容，或者二者皆留下，或者二者皆丢弃，或者手动修改添加。

合并以后

<img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/26c74d19606e40fea6e1d79662317bbb~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="70%" />

第二步：执行 `git add`，如下图。

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d6691bbbc52d4b34a88784ea53fb8cbc~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="100%" />

第三步：执行 `git rebase --continue`，执行此操作会让你编辑提交信息。执行成功后如下图。

<img src="https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b224c43a0962461883c2fa49653a4258~tplv-k3u1fbpfcp-watermark.image?" alt="image.png" width="100%" />

如果 rebase_feat 分支多个新的提交，每个提交都有冲突，需要按照上述方式一个一个的解决。

#### git rebase -i

对从某个提交开始到最新的提交进行编辑、合并、改变顺序、删除等操作。有两种形式。

第一种：`grbi 57cebe6`，不包含 `57cebe6` 提交。
第二种：`grbi HEAD~3`，表示对最新的三个提交进行操作，这是比较常用的形式，下面着重对其进行介绍。

首先查看一下目前的提交。

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d3c1b2a5598a4710981f5c2b6892f2d3~tplv-k3u1fbpfcp-watermark.image?)

然后执行 `grbi HEAD~3` 对最新的三个提交进行操作。


![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/6426cb4514bb45a2be9a92c79fbd867f~tplv-k3u1fbpfcp-watermark.image?)

对照列出命令依次说明

- pick，默认需要此提交，无变化。
- reword，需要此提交，但是编辑提交信息。
- edit，需要此提交，能编辑提交信息和提交内容，与 reword 差异见 [git rebase - what's the difference between 'edit' and 'reword' - Stack Overflow](https://stackoverflow.com/questions/30194796/git-rebase-whats-the-difference-between-edit-and-reword)
- sqush，将当前提交合并到前一个提交，需要编辑提交消息。
- fixup，和 sqush 类似，但是不需要编辑提交信息。
- drop，删除此提交
- 改变提交在文件中的位置，也即改变了提交在 git 中的位置。比如把提交 `150dc60` 和 `57cebe6` 在文件中的顺序就能改变它们在 git 中的顺序。

以上是常用的命令，其他命令可以对照官网进行学习，下面以 `sqush` 为例，演示使用流程。

第一步：编辑 `pick -> s`，然后保存。

![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/c2e551121b7f4d56b26e301b6b038584~tplv-k3u1fbpfcp-watermark.image?)

第二步：进入信息重新编辑页面，编辑信息，然后保存。如果过程中发生冲突，按照 rebase 有冲突的情况进行解决。

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b6c422810855427ca2d5360a8475b3aa~tplv-k3u1fbpfcp-watermark.image?)

下面是最新两个提交的结果

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/abe36a238fdf4b38b5d659ae046f73cc~tplv-k3u1fbpfcp-watermark.image?)

### git chery-pick

`git chery-pick` 用于将一个或者多个提交应用到目前分支。如果有冲突，和 `git rebase` 的处理流程一样。

主要有三个命令。

```shell
# 别名
gcp='git cherry-pick' 
gcpa='git cherry-pick --abort'
gcpc='git cherry-pick --continue'

#实例
gcp 150dc60 # 将提交 150dc60 应用于当前分支
gcpa # 放弃当前操作
gcpc # 解决冲突后，继续执行 cherry-pick 操作
```

# 总结
- 所有别名只需要简单的记忆，忘记了就用 `alias | grep 'git commit'` 查询相关的功能。
- `git rebase` 命令相对复杂，需要多体会，多实践。
- 上述命令基本可以满足日常开发需要，也可以尝试 [gitui](https://github.com/extrawurst/gitui) 和 [tig](https://github.com/jonas/tig) 等命令行 ui。
- 最好熟悉一下 `vim` 编辑文件的操作。