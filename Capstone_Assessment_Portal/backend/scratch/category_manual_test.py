import asyncio
from app.repositories.category_repository import (
    create_category,
    get_category_by_name,
    list_categories,
    serialize_category,
)
from app.models.category_model import CategoryModel
from app.services.category_service import CategoryService
from app.schemas.category_schema import CategoryCreateRequest, CategoryUpdateRequest

service = CategoryService()


async def test_repository_layer():
    # quick sanity check that the repository functions actually save and read from mongo
    print("--- testing repository layer ---")
    new_category = CategoryModel(name="Mathematics", created_by="dummy_admin_id_123")
    created = await create_category(new_category)
    print("Created:", serialize_category(created))

    found = await get_category_by_name("Mathematics")
    print("Found by name:", serialize_category(found))

    all_categories = await list_categories()
    print("Total categories in db:", len(all_categories))


async def test_service_layer():
    # tests the actual business rules - duplicate blocking, 404s, etc
    print("--- testing service layer ---")
    created = await service.create_category(
        CategoryCreateRequest(name="Science"), admin_id="dummy_admin_id_123"
    )
    print("Created:", created)

    try:
        await service.create_category(
            CategoryCreateRequest(name="Science"), admin_id="dummy_admin_id_123"
        )
    except Exception as e:
        print("Duplicate correctly rejected:", e.detail)

    all_categories = await service.get_all_categories()
    print("All categories:", all_categories)

    updated = await service.update_category(
        created["id"], CategoryUpdateRequest(name="Science & Tech")
    )
    print("Updated:", updated)

    await service.delete_category(created["id"])
    print("Deleted successfully")

    try:
        await service.delete_category(created["id"])
    except Exception as e:
        print("Second delete correctly rejected:", e.detail)


async def main():
    await test_repository_layer()
    await test_service_layer()


asyncio.run(main())