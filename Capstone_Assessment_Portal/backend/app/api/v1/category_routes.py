# HTTP endpoints for category management.
# create/update/delete are admin-only (require_admin), list is open to any logged-in user.

from fastapi import APIRouter, Depends, status
from app.schemas.category_schema import (
    CategoryCreateRequest,
    CategoryUpdateRequest,
    CategoryResponse,
)
from app.services.category_service import CategoryService
from app.middleware.auth_middleware import get_current_user, require_admin

router = APIRouter(prefix="/categories", tags=["Categories"])
category_service = CategoryService()


@router.post("", response_model=CategoryResponse, status_code=status.HTTP_201_CREATED)
async def create_category(
    request: CategoryCreateRequest,
    current_user: dict = Depends(require_admin),
):
    # current_user["sub"] is the admin's email - used as created_by for now
    return await category_service.create_category(request, admin_id=current_user["sub"])


@router.get("", response_model=list[CategoryResponse])
async def list_categories(current_user: dict = Depends(get_current_user)):
    # any logged in user (admin or student) can view categories
    return await category_service.get_all_categories()


@router.put("/{category_id}", response_model=CategoryResponse)
async def update_category(
    category_id: str,
    request: CategoryUpdateRequest,
    current_user: dict = Depends(require_admin),
):
    return await category_service.update_category(category_id, request)


@router.delete("/{category_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_category(
    category_id: str,
    current_user: dict = Depends(require_admin),
):
    # 204 means success with no response body - standard for deletes
    await category_service.delete_category(category_id)