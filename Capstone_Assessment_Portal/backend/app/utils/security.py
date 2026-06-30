import bcrypt
from datetime import datetime, timedelta, timezone

from jose import JWTError, jwt

from app.config.settings import settings
from app.exceptions.custom_exceptions import InvalidTokenException


def hash_password(plain_password: str) -> str:
    """
    Hash a plain text password using bcrypt.
    """
    password_bytes = plain_password.encode("utf-8")
    salt = bcrypt.gensalt()
    hashed_bytes = bcrypt.hashpw(password_bytes, salt)
    return hashed_bytes.decode("utf-8")


def verify_password(plain_password: str, hashed_password: str) -> bool:
    """
    Verify a plain text password against its hashed value.
    """
    password_bytes = plain_password.encode("utf-8")
    hashed_bytes = hashed_password.encode("utf-8")
    return bcrypt.checkpw(password_bytes, hashed_bytes)


def create_access_token(data: dict) -> str:
    """
    Create a short-lived JWT access token.
    """
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
    """
    Create a long-lived JWT refresh token.
    """
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


def decode_token(token: str) -> dict:
    """
    Decode a JWT token and return its payload.

    Raises:
        InvalidTokenException: If the token is invalid or expired.
    """
    payload = None

    try:
        payload = jwt.decode(
            token,
            settings.jwt_secret_key,
            algorithms=[settings.jwt_algorithm],
        )
    except JWTError as exc:
        raise InvalidTokenException("Invalid or expired token.") from exc

    return payload


def decode_access_token(token: str) -> dict:
    """
    Decode and validate an access token.
    """
    payload = decode_token(token)

    if payload.get("type") != "access":
        raise InvalidTokenException("Invalid access token.")

    return payload


def decode_refresh_token(token: str) -> dict:
    """
    Decode and validate a refresh token.
    """
    payload = decode_token(token)

    if payload.get("type") != "refresh":
        raise InvalidTokenException("Invalid refresh token.")

    return payload