import time
import traceback
import logging

from kafka import KafkaConsumer, errors
import json
from app import app, db
from models import Device
logging.basicConfig(level=logging.INFO)




def start_consumer():
    logging.info("Starting Kafka consumer thread...")
    while True:  # внешний цикл для переподключения при ошибках
        consumer = None
        try:
            bootstrap = app.config['KAFKA_BOOTSTRAP_SERVERS']
            topic = app.config['TOPIC_DEVICE_EVENTS']
            logging.info(f"Connecting to Kafka at {bootstrap}, topic {topic}")

            consumer = KafkaConsumer(
                topic,
                bootstrap_servers=bootstrap,
                value_deserializer=lambda m: json.loads(m.decode('utf-8')),
                auto_offset_reset='earliest',
                group_id='device-service',
                enable_auto_commit=True,
                max_poll_records=100,
                session_timeout_ms=30000,  # увеличиваем таймаут сессии
                heartbeat_interval_ms=10000,  # интервал heartbeat
                consumer_timeout_ms=5000
            )
            logging.info("Kafka consumer connected successfully")

            # Основной цикл получения сообщений
            for msg in consumer:
                try:
                    logging.info(f"Received message: {msg.value}")
                    event = msg.value
                    with app.app_context():
                        if event.get('event') == 'device.created':
                            existing = Device.query.get(event['id'])
                            if existing:
                                logging.info(f"Device {event['id']} already exists, skipping")
                                continue
                            device = Device(
                                id=event['id'],
                                name=event['name'],
                                type=event['type'],
                                location=event.get('location', ''),
                                unit=event.get('unit', ''),
                                status=event.get('status', 'inactive')
                            )
                            db.session.add(device)
                            db.session.commit()
                            logging.info(f"Device {device.id} created successfully")
                        elif event.get('event') == 'device.updated':
                            device = Device.query.get(event['id'])
                            if device:
                                device.name = event.get('name', device.name)
                                device.location = event.get('location', device.location)
                                device.status = event.get('status', device.status)
                                db.session.commit()
                                logging.info(f"Device {device.id} updated")
                        else:
                            logging.info(f"Unknown event type: {event.get('event')}")
                except Exception as e:
                    logging.info(f"Error processing message: {e}")
                    traceback.print_exc()
                    # продолжаем слушать дальше

        except errors.NoBrokersAvailable as e:
            logging.info(f"Kafka not ready: {e}, retrying in 5 seconds...")
            time.sleep(5)
        except Exception as e:
            logging.info(f"Consumer error: {e}")
            traceback.print_exc()
            logging.info("Recreating consumer in 5 seconds...")
            time.sleep(5)
        finally:
            if consumer:
                try:
                    consumer.close()
                except:
                    pass