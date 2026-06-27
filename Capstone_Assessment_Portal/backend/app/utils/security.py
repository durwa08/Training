# handles password hashing + JWT access/refresh tokens

import bcrypt
from jose import jwt, JWTError
from datetime import datetime, timedelta, timezone
from app.config.settings import settings


# ---------- password hashing ----------

def hash_password(plain_password: str) -> str:
    # bcrypt adds a random salt so same passwords dont look the same in db
    password_bytes = plain_password.encode("utf-8")
    salt = bcrypt.gensalt()
    hashed_bytes = bcrypt.hashpw(password_bytes, salt)
    return hashed_bytes.decode("utf-8")


def verify_password(plain_password: str, hashed_password: str) -> bool:
    # checks typed password against the stored hash on login
    password_bytes = plain_password.encode("utf-8")
    hashed_bytes = hashed_password.encode("utf-8")
    return bcrypt.checkpw(password_bytes, hashed_bytes)


# ---------- jwt tokens ----------

def create_access_token(data: dict) -> str:
    # short lived token (60 min), sent on every request
    # type=access so a refresh token cant be reused as this
    payload = data.copy()
    expire_time = datetime.now(timezone.utc) + timedelta(
        minutes=settings.access_token_expire_minutes
    )
    payload["exp"] = expire_time
    payload["type"] = "access"

    token = jwt.encode(
        payload,
        settings.jwt_secret_key,
        algorithm=settings.jwt_algorithm,
    )
    return token


def create_refresh_token(data: dict) -> str:
    # long lived token (7 days), only used to get a new access token
    payload = data.copy()
    expire_time = datetime.now(timezone.utc) + timedelta(
        days=settings.refresh_token_expire_days
    )
    payload["exp"] = expire_time
    payload["type"] = "refresh"

    token = jwt.encode(
        payload,
        settings.jwt_secret_key,
        algorithm=settings.jwt_algorithm,
    )
    return token


def decode_token(token: str) -> dict | None:
    # decodes either type of token, doesnt check which type
    try:
        payload = jwt.decode(
            token,
            settings.jwt_secret_key,
            algorithms=[settings.jwt_algorithm],
        )
        return payload
    except JWTError:
        return None


def decode_access_token(token: str) -> dict | None:
    # used by protected routes, only accepts access tokens
    payload = decode_token(token)
    if payload is None:
        return None
    if payload.get("type") != "access":
        return None
    return payload


def decode_refresh_token(token: str) -> dict | None:
    # used only by /auth/refresh, only accepts refresh tokens
    payload = decode_token(token)
    if payload is None:
        return None
    if payload.get("type") != "refresh":
        return None
    return payload