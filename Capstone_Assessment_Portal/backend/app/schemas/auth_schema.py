from pydantic import BaseModel, EmailStr


class LoginRequest(BaseModel):
    # what client sends to log in
    email: EmailStr
    password: str


class TokenResponse(BaseModel):
    # sent back after login - now includes both tokens
    access_token: str
    refresh_token: str
    token_type: str = "bearer"
    role: str


class RefreshRequest(BaseModel):
    # what client sends to /auth/refresh to get a new access token
    refresh_token: str


class RefreshResponse(BaseModel):
    # what /auth/refresh sends back - just a new access token
    access_token: str
    token_type: str = "bearer"