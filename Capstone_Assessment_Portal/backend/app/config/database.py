from motor.motor_asyncio import AsyncIOMotorClient

from app.config.settings import settings

client = AsyncIOMotorClient(settings.mongo_uri)
database = client[settings.database_name]


def get_database():
    """
    Return the MongoDB database instance.

    Other modules should use this function instead of importing
    the database object directly. This keeps database access
    centralized and simplifies testing.
    """
    return database


async def ensure_indexes():
    """
    Create required MongoDB indexes during application startup.

    Creates a unique compound index on quiz title and category_id
    to prevent duplicate quiz titles within the same category.
    This acts as a database-level safeguard in addition to the
    application-level validation performed in the service layer.
    """
    quiz_collection = database["quizzes"]

    await quiz_collection.create_index(
        [("title", 1), ("category_id", 1)],
        unique=True,
        name="unique_title_per_category",
    )