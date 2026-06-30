import asyncio

from app.models.user_model import UserModel
from app.repositories.user_repository import (
    create_user,
    get_user_by_email,
    serialize_user,
)
from app.utils.security import hash_password


async def test():
    new_user = UserModel(
        username="testuser",
        email="testuser@example.com",
        hashed_password=hash_password("mypassword123"),
        role="student",
    )

    created = await create_user(new_user)
    serialized_created = serialize_user(created)

    assert serialized_created["username"] == "testuser"
    assert serialized_created["email"] == "testuser@example.com"
    assert serialized_created["role"] == "student"

    found = await get_user_by_email("testuser@example.com")
    serialized_found = serialize_user(found)

    assert serialized_found == serialized_created


asyncio.run(test())