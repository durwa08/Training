from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from app.api.v1.auth_routes import router as auth_router
from app.api.v1.category_routes import router as category_router
from app.exceptions.custom_exceptions import (
    UserAlreadyExistsException,
    InvalidCredentialsException,
    InvalidRefreshTokenException,
    UserNotFoundException,
)

app = FastAPI(
    title="Assessment Portal API",
    description="Backend APIs for the Assessment Portal capstone project",
    version="1.0.0",
)


@app.exception_handler(UserAlreadyExistsException)
async def user_already_exists_handler(request: Request, exc: UserAlreadyExistsException):
    return JSONResponse(
        status_code=400,
        content={"detail": "A user with this email already exists."},
    )


@app.exception_handler(InvalidCredentialsException)
async def invalid_credentials_handler(request: Request, exc: InvalidCredentialsException):
    return JSONResponse(
        status_code=401,
        content={"detail": "Invalid email or password."},
    )


@app.exception_handler(InvalidRefreshTokenException)
async def invalid_refresh_token_handler(request: Request, exc: InvalidRefreshTokenException):
    return JSONResponse(
        status_code=401,
        content={"detail": "Invalid or expired refresh token."},
    )


@app.exception_handler(UserNotFoundException)
async def user_not_found_handler(request: Request, exc: UserNotFoundException):
    return JSONResponse(
        status_code=401,
        content={"detail": "User no longer exists."},
    )


app.include_router(auth_router)
app.include_router(category_router)


@app.get("/")
def health_check():
    """
    Check if the Assessment Portal API is running.
    """
    return {"message": "Assessment Portal API is running"}