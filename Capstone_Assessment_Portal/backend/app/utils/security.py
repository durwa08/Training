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
    # data is usually {"sub": email, "role": role}, just adding expiry on top
    payload = data.copy()
    expire_time = datetime.now(timezone.utc) + timedelta(
        minutes=settings.access_token_expire_minutes
    )
    payload["exp"] = expire_time

    token = jwt.encode(
        payload,
        settings.jwt_secret_key,
        algorithm=settings.jwt_algorithm,
    )
    return token


def decode_access_token(token: str) -> dict | None:
    # used on protected routes, returns the payload if valid else None
    try:
        payload = jwt.decode(
            token,
            settings.jwt_secret_key,
            algorithms=[settings.jwt_algorithm],
        )
        return payload
    except JWTError:
        return None