from fastapi import HTTPException, status
from app.repositories.user_repository import (
    get_user_by_email,
    create_user,
    serialize_user,
)
from app.models.user_model import UserModel
from app.schemas.user_schema import UserRegisterRequest
from app.schemas.auth_schema import LoginRequest
from app.utils.security import (
    hash_password,
    verify_password,
    create_access_token,
    create_refresh_token,
    decode_refresh_token,
)


class AuthService:

    async def register_user(self, request: UserRegisterRequest) -> dict:
        # check for duplicate email first
        existing_user = await get_user_by_email(request.email)
        if existing_user is not None:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="A user with this email already exists.",
            )

        # never store plain password
        hashed = hash_password(request.password)

        # build the db model and save
        new_user = UserModel(
            username=request.username,
            email=request.email,
            hashed_password=hashed,
            role=request.role,
        )
        created_user = await create_user(new_user)

        # only return safe public fields
        return serialize_user(created_user)

    async def login_user(self, request: LoginRequest) -> dict:
        # check user exists
        user = await get_user_by_email(request.email)
        if user is None:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid email or password.",
            )

        # same error message for wrong user/wrong password on purpose
        # dont want to leak which emails are registered
        if not verify_password(request.password, user["hashed_password"]):
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid email or password.",
            )

        # issue both tokens - access for normal use, refresh for getting new access tokens later
        token_data = {"sub": user["email"], "role": user["role"]}
        access_token = create_access_token(data=token_data)
        refresh_token = create_refresh_token(data=token_data)

        return {
            "access_token": access_token,
            "refresh_token": refresh_token,
            "token_type": "bearer",
            "role": user["role"],
        }
    async def refresh_access_token(self, refresh_token: str) -> dict:
        # takes a refresh token, gives back a fresh access token
        payload = decode_refresh_token(refresh_token)
        if payload is None:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid or expired refresh token.",
            )

        # make sure user still exists (in case account got deleted after token was issued)
        user = await get_user_by_email(payload["sub"])
        if user is None:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="User no longer exists.",
            )

        new_access_token = create_access_token(
            data={"sub": user["email"], "role": user["role"]}
        )

        return {
            "access_token": new_access_token,
            "token_type": "bearer",
        }