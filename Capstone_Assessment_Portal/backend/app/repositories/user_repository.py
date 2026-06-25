from app.config.database import get_database
from app.models.user_model import UserModel
from bson import ObjectId

# only this file talks to the users collection directly, everything else
# goes through these functions
database = get_database()
user_collection = database["users"]


async def get_user_by_email(email: str) -> dict | None:
    # used for checking duplicates on register and finding user on login
    user = await user_collection.find_one({"email": email})
    return user


async def create_user(user: UserModel) -> dict:
    # password should already be hashed before it gets here
    # excluding id since mongo generates its own _id on insert
    user_dict = user.model_dump(by_alias=True, exclude={"id"})

    result = await user_collection.insert_one(user_dict)

    # fetching it back so we return the final doc with the real _id
    created_user = await user_collection.find_one({"_id": result.inserted_id})
    return created_user


def serialize_user(user: dict) -> dict:
    # mongo's _id is an ObjectId, but UserResponse expects a plain string
    return {
        "id": str(user["_id"]),
        "username": user["username"],
        "email": user["email"],
        "role": user["role"],
    }