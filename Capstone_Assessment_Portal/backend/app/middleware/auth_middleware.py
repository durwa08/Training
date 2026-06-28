from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from app.utils.security import decode_access_token

# using HTTPBearer instead of OAuth2PasswordBearer just for a cleaner
# "paste token" box in swagger, does the same job either way
bearer_scheme = HTTPBearer()


async def get_current_user(
        credentials: HTTPAuthorizationCredentials = Depends(bearer_scheme),
) -> dict:
    # pulls the token out, decodes it, 401 if its bad or expired
    token = credentials.credentials  # "Bearer " already stripped by fastapi
    payload = decode_access_token(token)
    if payload is None:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid or expired token.",
            headers={"WWW-Authenticate": "Bearer"},
        )
    return payload


async def require_admin(current_user: dict = Depends(get_current_user)) -> dict:
    # same check as above but also makes sure role is admin
    # use this on routes that only admins should access
    if current_user.get("role") != "admin":
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="This action requires admin privileges.",
        )
    return current_user