# request/response shapes for category APIs

from pydantic import BaseModel, Field


class CategoryCreateRequest(BaseModel):
    # what admin sends to create a category
    name: str = Field(min_length=2, max_length=100)


class CategoryUpdateRequest(BaseModel):
    # what admin sends to update a category
    name: str = Field(min_length=2, max_length=100)


class CategoryResponse(BaseModel):
    # what we send back - matches CategoryModel but as plain strings
    id: str
    name: str
    created_by: str

    class Config:
        from_attributes = True