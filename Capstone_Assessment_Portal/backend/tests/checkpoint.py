import asyncio
from app.repositories.user_repository import create_user, get_user_by_email, serialize_user
from app.models.user_model import UserModel
from app.utils.security import hash_password


async def test():
    # quick manual test, not part of the actual app
    new_user = UserModel(
        username="testuser",
        email="testuser@example.com",
        hashed_password=hash_password("mypassword123"),
        role="student",
    )
    created = await create_user(new_user)
    print("Created user:", serialize_user(created))

    found = await get_user_by_email("testuser@example.com")
    print("Found user:", serialize_user(found))


asyncio.run(test())