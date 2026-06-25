from fastapi import HTTPException, status
from app.repositories.user_repository import (
    get_user_by_email,
    create_user,
    serialize_user,
)
from app.models.user_model import UserModel
from app.schemas.user_schema import UserRegisterRequest
from app.schemas.auth_schema import LoginRequest
from app.utils.security import hash_password, verify_password, create_access_token


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

        # sub is the standard jwt field for "who this belongs to"
        token = create_access_token(
            data={"sub": user["email"], "role": user["role"]}
        )

        return {
            "access_token": token,
            "token_type": "bearer",
            "role": user["role"],
        }