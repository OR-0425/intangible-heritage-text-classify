import os

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch

from schema import PredictRequest, PredictResponse

app = FastAPI(title="少数民族非遗智能文本分类服务", version="1.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 模型存放路径
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
model_dir = os.path.normpath(os.path.join(BASE_DIR, "..", "model"))

# 加载模型（标签由训练时写进 config.json，这里不再手动改，避免标签错位）
tokenizer = AutoTokenizer.from_pretrained(model_dir, local_files_only=True)
model = AutoModelForSequenceClassification.from_pretrained(model_dir, local_files_only=True)
model.eval()


@app.post("/predict", response_model=PredictResponse)
def predict(req: PredictRequest):
    inputs = tokenizer(
        req.text,
        max_length=128,
        truncation=True,
        padding="max_length",
        return_tensors="pt"
    )
    with torch.no_grad():
        out = model(**inputs)
        probs = torch.softmax(out.logits, dim=-1)[0]
        pred_idx = int(torch.argmax(probs).item())
        confidence = float(probs[pred_idx].item())
        label = model.config.id2label[pred_idx]
    return PredictResponse(label=label, confidence=round(confidence, 4))


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="127.0.0.1", port=8001, reload=False)
