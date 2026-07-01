from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime, timezone


class UserModel(BaseModel):
    """
    Represents the user document stored in the MongoDB database.
    """

    id: Optional[str] = Field(default=None, alias="_id")
    username: str
    email: str
    hashed_password: str
    role: str = "student"
    created_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))

    class Config:
        """
        Configure model behavior for MongoDB field aliases.
        """

        populate_by_name = True