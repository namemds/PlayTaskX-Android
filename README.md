# PlayTaskX-Android
一个基于 PlayTask 的 Android 应用，集成了 **智谱清言 GLM API** 的 Android 悬浮球对话应用，支持与 AI 助手进行对话，适合任务提醒、学习问答、闲聊娱乐等场景。
## 功能亮点

- 悬浮球全局唤起，点击即可与 AI 对话
- 支持多轮对话，自动保存历史记录
- 使用智谱清言 GLM 模型，响应快速，效果优秀
- 简洁美观的聊天界面

---
## 🚀 快速开始

### 1. 克隆项目
直接用Android Studio打开
### 2. 配置智谱 API Key
打开文件
```swift
/app/src/main/java/com/cdut/playtask/network/GLMService.java
```
找到
```Java
private static final String API_KEY = "替换为你的API Key";
```
将 "替换为你的API Key" 替换为你自己的智谱清言 API Key（登录 https://open.bigmodel.cn/usercenter/proj-mgmt/apikeys 获取）。
### 3.编译运行
如需使用悬浮球功能，请授予“悬浮窗权限”。
