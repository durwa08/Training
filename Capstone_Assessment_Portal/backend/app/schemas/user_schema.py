from typing import Literal

from pydantic import BaseModel, EmailStr, Field

from app.constants.constants import ADMIN_ROLE, STUDENT_ROLE


class UserRegisterRequest(BaseModel):
    """Request model for user registration."""

    username: str = Field(min_length=3, max_length=50)
    email: EmailStr
    password: str = Field(min_length=6)
    role: Literal[ADMIN_ROLE, STUDENT_ROLE] = STUDENT_ROLE


class UserResponse(BaseModel):
    """Response model containing public user information."""

    id: str
    username: str
    email: str
    role: str

    class Config:
        from_attributes = True