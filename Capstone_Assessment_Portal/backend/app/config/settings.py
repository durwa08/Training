from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    mongo_uri: str
    database_name: str
    jwt_secret_key: str
    jwt_algorithm: str = "HS256"
    access_token_expire_minutes: int = 60
    refresh_token_expire_days: int = 7

    class Config:
        env_file = ".env"
        case_sensitive = False


settings = Settings()