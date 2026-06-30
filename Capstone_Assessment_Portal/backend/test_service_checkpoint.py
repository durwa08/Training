import asyncio

from app.schemas.auth_schema import LoginRequest
from app.schemas.user_schema import UserRegisterRequest
from app.services.auth_service import AuthService
from app.exceptions.custom_exceptions import InvalidCredentialsException

service = AuthService()


async def test():
    register_data = UserRegisterRequest(
        username="janedoe",
        email="janedoe@example.com",
        password="janepass123",
        role="student",
    )

    registered = await service.register_user(register_data)

    assert registered["username"] == "janedoe"
    assert registered["email"] == "janedoe@example.com"
    assert registered["role"] == "student"

    login_data = LoginRequest(
        email="janedoe@example.com",
        password="janepass123",
    )

    token_response = await service.login_user(login_data)

    assert token_response["access_token"] is not None
    assert token_response["refresh_token"] is not None
    assert token_response["token_type"] == "bearer"
    assert token_response["role"] == "student"

    try:
        bad_login = LoginRequest(
            email="janedoe@example.com",
            password="wrongpassword",
        )
        await service.login_user(bad_login)
        assert False, "Expected InvalidCredentialsException"
    except InvalidCredentialsException:
        assert True


asyncio.run(test())