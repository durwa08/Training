# business logic for category management - the routes call these functions,
# all the actual decision-making (duplicate checks, 404s) happens here

from fastapi import HTTPException, status
from app.repositories.category_repository import (
    get_category_by_name,
    get_category_by_id,
    create_category,
    update_category,
    delete_category,
    list_categories,
    serialize_category,
)
from app.models.category_model import CategoryModel
from app.schemas.category_schema import CategoryCreateRequest, CategoryUpdateRequest


class CategoryService:

    async def create_category(self, request: CategoryCreateRequest, admin_id: str) -> dict:
        # block duplicate names before creating anything (SRS CAT-002)
        existing = await get_category_by_name(request.name)
        if existing is not None:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="A category with this name already exists.",
            )

        new_category = CategoryModel(name=request.name, created_by=admin_id)
        created = await create_category(new_category)
        return serialize_category(created)

    async def get_all_categories(self) -> list[dict]:
        categories = await list_categories()
        return [serialize_category(c) for c in categories]

    async def update_category(self, category_id: str, request: CategoryUpdateRequest) -> dict:
        # confirm it exists first, otherwise return 404 (SRS CAT-005)
        existing = await get_category_by_id(category_id)
        if existing is None:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Category not found.",
            )

        updated = await update_category(category_id, request.name)
        return serialize_category(updated)

    async def delete_category(self, category_id: str) -> None:
        # delete_category returns False if nothing matched that id (SRS CAT-007)
        deleted = await delete_category(category_id)
        if not deleted:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Category not found.",
            )