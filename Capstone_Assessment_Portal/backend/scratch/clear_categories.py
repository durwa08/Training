import asyncio
from app.config.database import get_database


async def clear_categories():
    # wipes everything in the categories collection - only for cleaning up test data
    database = get_database()
    result = await database["categories"].delete_many({})
    print(f"Deleted {result.deleted_count} category documents.")


asyncio.run(clear_categories())