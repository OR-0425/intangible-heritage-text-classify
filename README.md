# 基于Transformer的少数民族非遗文本智能分类系统
> 课程课题作业，SpringBoot + FastAPI(BERT‑Transformer) + MySQL Web系统

## 📖项目简介
本系统实现非遗文本智能分类，一共10个非遗类别：民间文学、传统音乐、传统舞蹈、传统戏剧、曲艺、传统体育、游艺与杂技、传统美术、传统技艺、传统医药、民俗。
- 后端：SpringBoot + Mybatis‑Plus + JWT登录鉴权
- AI推理服务：Python FastAPI，基于bert‑base‑chinese（Transformer）微调，数据集400条，训练准确率0.8875
- 前端：原生HTML+CSS+JavaScript + ECharts图表
- 数据库：MySQL

## ✨功能
1. 用户注册、登录，JWT权限隔离：管理员/普通用户
2. 非遗文本AI识别，返回分类+置信度
3. 识别历史记录：搜索、分类筛选、时间筛选、分页、查看详情、删除、导出CSV
4. 数据概览：统计卡片、近7天识别趋势图
5. 数据统计：类别饼图、置信度分布柱状图

## 🗂项目结构
