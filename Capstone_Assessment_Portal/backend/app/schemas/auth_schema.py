from pydantic import BaseModel, EmailStr


class LoginRequest(BaseModel):
    # what client sends to log in
    email: EmailStr
    password: str


class TokenResponse(BaseModel):
    # what we send back after login, client stores this and sends it
    # in the Authorization header on future requests
    access_token: str
    token_type: str = "bearer"
    role: str # so frontend knows admin or student right away