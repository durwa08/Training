from pydantic import BaseModel, EmailStr


class LoginRequest(BaseModel):
    """Request model for user login."""

    email: EmailStr
    password: str


class TokenResponse(BaseModel):
    """Response model returned after successful login."""

    access_token: str
    refresh_token: str
    token_type: str = "bearer"
    role: str


class RefreshRequest(BaseModel):
    """Request model for generating a new access token."""

    refresh_token: str


class RefreshResponse(BaseModel):
    """Response model containing a newly generated access token."""

    access_token: str
    token_type: str = "bearer"