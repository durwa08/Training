from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime, timezone


class UserModel(BaseModel):
    # this is the db shape, not the request shape (thats in user_schema.py)

    # mongo generates _id on its own, optional here so we can reuse
    # this model when reading users back out too
    id: Optional[str] = Field(default=None, alias="_id")

    username: str
    email: str

    # password should already be hashed by the time it gets here
    hashed_password: str

    # default to student so role doesnt accidentally end up admin
    role: str = "student"

    # for sorting/auditing later
    created_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))

    class Config:
        # lets us use "_id" from mongo but still access it as .id in code
        populate_by_name = True