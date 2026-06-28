from fastapi import FastAPI
from app.api.v1.auth_routes import router as auth_router

app = FastAPI(
    title="Assessment Portal API",
    description="Backend APIs for the Assessment Portal capstone project",
    version="1.0.0",
)

# this gives us POST /auth/register and POST /auth/login
app.include_router(auth_router)


@app.get("/")
def health_check():
    # just to check server is up, visit localhost:8000/
    return {"message": "Assessment Portal API is running"}