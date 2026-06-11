import os

class Config:
    KAFKA_BOOTSTRAP_SERVERS = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'kafka:9092')
    DATABASE_URL = os.getenv('DATABASE_URL', 'postgresql://postgres:postgres@postgres-dev:5432/devices')
    TOPIC_DEVICE_EVENTS = 'device-events'