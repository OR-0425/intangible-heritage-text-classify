from pydantic import BaseModel, Field

# 请求体：待分类的非遗文本
class PredictRequest(BaseModel):
    text: str = Field(..., description="待分类的少数民族非遗文本")

# 响应体：AI返回分类标签和置信度
class PredictResponse(BaseModel):
    label: str = Field(..., description="非遗分类标签")
    confidence: float = Field(..., description="预测置信度")
