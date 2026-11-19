import os

# 项目根路径
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# 数据集路径
DATA_RAW_PATH = os.path.join(BASE_DIR, "data", "raw")

# 模型保存路径
MODEL_SAVE_PATH = os.path.join(BASE_DIR, "model")

# 预训练模型名称，用中文 BERT
PRETRAIN_MODEL_NAME = "bert-base-chinese"
# 国内镜像
HF_ENDPOINT = "https://hf-mirror.com"

# 分类标签（顺序即模型输出的 0、1、2... 编号，训练与推理必须一致）
LABELS = [
    "民间文学",
    "传统音乐",
    "传统舞蹈",
    "传统戏剧",
    "曲艺",
    "传统体育、游艺与杂技",
    "传统美术",
    "传统技艺",
    "传统医药",
    "民俗"
]

# FastAPI 服务端口
SERVER_PORT = 8000
