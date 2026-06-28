import asyncio
from app.services.auth_service import AuthService
from app.schemas.user_schema import UserRegisterRequest
from app.schemas.auth_schema import LoginRequest

service = AuthService()


async def test():
    # Try registering a NEW user (use a fresh email so it doesn't clash
    # with the "testuser@example.com" we already created earlier)
    register_data = UserRegisterRequest(
        username="janedoe",
        email="janedoe@example.com",
        password="janepass123",
        role="student",
    )
    registered = await service.register_user(register_data)
    print("Registered:", registered)

    # Now try logging in with correct credentials
    login_data = LoginRequest(email="janedoe@example.com", password="janepass123")
    token_response = await service.login_user(login_data)
    print("Login success:", token_response)

    # Now try logging in with a WRONG password (should raise an error)
    try:
        bad_login = LoginRequest(email="janedoe@example.com", password="wrongpassword")
        await service.login_user(bad_login)
    except Exception as e:
        print("Login correctly rejected wrong password:", e.detail)


asyncio.run(test())