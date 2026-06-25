from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    # these map to .env automatically, case doesnt matter
    mongo_uri: str
    database_name: str
    jwt_secret_key: str
    jwt_algorithm: str = "HS256"  # default if not in .env
    access_token_expire_minutes: int = 60  # token validity in mins

    class Config:
        env_file = ".env"
        case_sensitive = False


# single instance, everyone imports this instead of making their own
settings = Settings()