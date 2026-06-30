"""
Pydantic model representing a category document stored in MongoDB.
"""

from datetime import datetime, timezone
from typing import Optional

from pydantic import BaseModel, Field


class CategoryModel(BaseModel):
    """
    Represents a category document in the database.
    """

    id: Optional[str] = Field(default=None, alias="_id")
    name: str
    created_by: str
    created_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))

    class Config:
        """Pydantic configuration for MongoDB field mapping."""

        populate_by_name = True