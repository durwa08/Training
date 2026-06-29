# Category document in MongoDB

from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime, timezone


class CategoryModel(BaseModel):
    id: Optional[str] = Field(default=None, alias="_id")
    name: str
    created_by: str  # admin's user id who created this category
    created_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))

    class Config:
        populate_by_name = True