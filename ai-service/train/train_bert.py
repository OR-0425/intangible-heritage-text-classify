import os
os.environ["HF_ENDPOINT"] = "https://hf-mirror.com"
# 新增：关闭软链接警告
os.environ["HF_HUB_DISABLE_SYMLINKS_WARNING"] = "1"
import sys
sys.path.append(".")

import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from torch.optim import AdamW
from tqdm import tqdm
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

from src.config import DATA_RAW_PATH, MODEL_SAVE_PATH, PRETRAIN_MODEL_NAME, LABELS

# 标签和数字互相映射
label2id = {label: idx for idx, label in enumerate(LABELS)}
id2label = {idx: label for idx, label in enumerate(LABELS)}

# 自定义数据集类
class ICHDataset(Dataset):
    def __init__(self, texts, labels, tokenizer, max_len=128):
        self.texts = texts
        self.labels = labels
        self.tokenizer = tokenizer
        self.max_len = max_len

    def __len__(self):
        return len(self.texts)

    def __getitem__(self, index):
        text = str(self.texts[index])
        label = self.labels[index]
        encode = self.tokenizer(
            text,
            truncation=True,
            padding="max_length",
            max_length=self.max_len,
            return_tensors="pt"
        )
        return {
            "input_ids": encode["input_ids"].flatten(),
            "attention_mask": encode["attention_mask"].flatten(),
            "labels": torch.tensor(label, dtype=torch.long)
        }

def main():
    # 1. 读取数据集（后续我们会放csv在这里）
    csv_path = os.path.join(DATA_RAW_PATH, "data.csv")
    df = pd.read_csv(csv_path)

    # 文本、标签转数字
    df["label_id"] = df["label"].map(label2id)
    texts = df["text"].tolist()
    label_ids = df["label_id"].tolist()

    # 划分训练集、测试集
    train_text, test_text, train_label, test_label = train_test_split(
        texts, label_ids, test_size=0.2, random_state=42
    )

    tokenizer = AutoTokenizer.from_pretrained(PRETRAIN_MODEL_NAME)
    model = AutoModelForSequenceClassification.from_pretrained(
        PRETRAIN_MODEL_NAME,
        num_labels=len(LABELS),
        id2label=id2label,
        label2id=label2id
    )

    train_dataset = ICHDataset(train_text, train_label, tokenizer)
    test_dataset = ICHDataset(test_text, test_label, tokenizer)

    train_loader = DataLoader(train_dataset, batch_size=8, shuffle=True)
    test_loader = DataLoader(test_dataset, batch_size=8, shuffle=False)

    # 优化器
    optimizer = AdamW(model.parameters(), lr=2e-5)
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model.to(device)

    epoch_num = 3
    for epoch in range(epoch_num):
        model.train()
        train_loss = 0
        for batch in tqdm(train_loader):
            input_ids = batch["input_ids"].to(device)
            attention_mask = batch["attention_mask"].to(device)
            labels = batch["labels"].to(device)

            optimizer.zero_grad()
            out = model(input_ids=input_ids, attention_mask=attention_mask, labels=labels)
            loss = out.loss
            loss.backward()
            optimizer.step()
            train_loss += loss.item()

        # 验证
        model.eval()
        all_pred = []
        all_true = []
        with torch.no_grad():
            for batch in test_loader:
                input_ids = batch["input_ids"].to(device)
                attention_mask = batch["attention_mask"].to(device)
                labels = batch["labels"].to(device)
                out = model(input_ids=input_ids, attention_mask=attention_mask)
                pred = torch.argmax(out.logits, dim=1)
                all_pred.extend(pred.cpu().numpy())
                all_true.extend(labels.cpu().numpy())
        acc = accuracy_score(all_true, all_pred)
        print(f"Epoch {epoch+1}, loss:{train_loss:.4f}, acc:{acc:.4f}")

    # 训练完成，保存模型和分词器
    model.save_pretrained(MODEL_SAVE_PATH)
    tokenizer.save_pretrained(MODEL_SAVE_PATH)
    print("模型保存完成！路径：", MODEL_SAVE_PATH)

if __name__ == "__main__":
    main()
