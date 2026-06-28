from pydantic import BaseModel, EmailStr, Field
from typing import Literal


class UserRegisterRequest(BaseModel):
    # what client sends when registering
    username: str = Field(min_length=3, max_length=50)
    email: EmailStr
    password: str = Field(min_length=6)  # plain for now, gets hashed before saving

    # only admin or student allowed, defaults to student so no one
    # can accidentally register as admin
    role: Literal["admin", "student"] = "student"


class UserResponse(BaseModel):
    # what we send back, no password field even hashed
    id: str
    username: str
    email: str
    role: str

    class Config:
        from_attributes = True 