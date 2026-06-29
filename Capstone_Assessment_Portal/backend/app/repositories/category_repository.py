# all direct MongoDB queries for the "categories" collection live here.
# other files (service, routes) should never query mongo directly - they call these functions instead.

from app.config.database import get_database
from app.models.category_model import CategoryModel
from bson import ObjectId
from bson.errors import InvalidId

database = get_database()
category_collection = database["categories"]


async def get_category_by_name(name: str) -> dict | None:
    # used to block duplicate category names before creating a new one (SRS CAT-002)
    return await category_collection.find_one({"name": name})


async def get_category_by_id(category_id: str) -> dict | None:
    # mongo needs a real ObjectId, not a plain string, so we convert it first.
    # if the string isnt even a valid id shape, we just return None instead of crashing
    try:
        obj_id = ObjectId(category_id)
    except InvalidId:
        return None
    return await category_collection.find_one({"_id": obj_id})


async def create_category(category: CategoryModel) -> dict:
    # convert pydantic model to plain dict, insert it, then read it back
    # so we return the full saved doc including its new mongo-generated id
    category_dict = category.model_dump(by_alias=True, exclude={"id"})
    result = await category_collection.insert_one(category_dict)
    created = await category_collection.find_one({"_id": result.inserted_id})
    return created


async def update_category(category_id: str, new_name: str) -> dict | None:
    # $set only changes the "name" field, leaves rest of the document untouched
    try:
        obj_id = ObjectId(category_id)
    except InvalidId:
        return None

    await category_collection.update_one(
        {"_id": obj_id},
        {"$set": {"name": new_name}},
    )
    return await category_collection.find_one({"_id": obj_id})


async def delete_category(category_id: str) -> bool:
    # deleted_count tells us if something actually got deleted or not (CAT-007)
    try:
        obj_id = ObjectId(category_id)
    except InvalidId:
        return False

    result = await category_collection.delete_one({"_id": obj_id})
    return result.deleted_count > 0


async def list_categories() -> list[dict]:
    # find() with no filter returns everything, loop through the stream and collect it
    categories = []
    async for category in category_collection.find():
        categories.append(category)
    return categories


def serialize_category(category: dict) -> dict:
    # mongo gives _id as an ObjectId object, convert to plain string for the API response
    return {
        "id": str(category["_id"]),
        "name": category["name"],
        "created_by": category["created_by"],
    }