from fastapi import APIRouter, status
from app.schemas.user_schema import UserRegisterRequest, UserResponse
from app.schemas.auth_schema import LoginRequest, TokenResponse, RefreshRequest, RefreshResponse
from app.services.auth_service import AuthService

# all auth related routes go here, mounted with /auth prefix in main.py
router = APIRouter(prefix="/auth", tags=["Authentication"])

# reusing the same service instance for every request
auth_service = AuthService()


@router.post(
    "/register",
    response_model=UserResponse,
    status_code=status.HTTP_201_CREATED,
)
async def register(request: UserRegisterRequest):
    # creates a new user, admin or student
    # service takes care of checking duplicate emails (400 if found)
    result = await auth_service.register_user(request)
    return result


@router.post(
    "/login",
    response_model=TokenResponse,
    status_code=status.HTTP_200_OK,
)
async def login(request: LoginRequest):
    # logs the user in and returns a jwt + role
    # wrong email/password gets handled as 401 inside the service
    result = await auth_service.login_user(request)
    return result

@router.post(
    "/refresh",
    response_model=RefreshResponse,
    status_code=status.HTTP_200_OK,
)
async def refresh(request: RefreshRequest):
    # client sends refresh token here when access token expires, gets a new access token back
    result = await auth_service.refresh_access_token(request.refresh_token)
    return result


from app.middleware.auth_middleware import get_current_user, require_admin
from fastapi import Depends


@router.get("/me")
async def get_my_profile(current_user: dict = Depends(get_current_user)):
    # temp route to check if the token is working
    # just dumps whatever is inside the jwt (email + role)
    return {"message": "You are authenticated!", "your_data": current_user}


@router.get("/admin-only")
async def admin_only_test(current_user: dict = Depends(require_admin)):
    # same idea but only admins should be able to hit this
    # students should get a 403 here
    return {"message": "You are an admin, welcome!", "your_data": current_user}