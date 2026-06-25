from motor.motor_asyncio import AsyncIOMotorClient
from app.config.settings import settings  # import the object, not individual values

# motor instead of pymongo since fastapi is async, dont want blocking calls
client = AsyncIOMotorClient(settings.mongo_uri)

# grabs the db, mongo creates it automatically if it doesnt exist yet
database = client[settings.database_name]


def get_database():
    # other files should call this instead of importing database directly
    # makes it easier to mock in tests later
    return database