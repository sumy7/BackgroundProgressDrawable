BackgroundProgressDrawable
====================================
填充背景进度条

简易教程
--------
使用 Builder 创建进度条

    BackgroundProgressDrawable progressDrawable = new BackgroundProgressDrawable.Builder().setMax(1000).setAlpha(150).create();

将进度条赋值到控件背景上

    anyview.setBackground(progressDrawable);

设置进度

    progressDrawable.setProgressNative(0); // 无过渡动画
    progressDrawable.setProgress(1000); // 有过渡动画

更多内容可以参考例程

许可证
-------
    Copyright 2015 Sumy.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

联系我们
--------
Sumy < sunmingjian8@gmail.com > or Issues