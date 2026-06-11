import os

from flask import Flask, jsonify
from config import Config
from models import db
import threading

app = Flask(__name__)
app.config.from_object(Config)
app.config['SQLALCHEMY_DATABASE_URI'] = Config.DATABASE_URL
app.config['KAFKA_BOOTSTRAP_SERVERS'] = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'kafka:9092')
app.config['TOPIC_DEVICE_EVENTS'] = os.getenv('TOPIC_DEVICE_EVENTS', 'device-events')
db.init_app(app)

@app.route('/health')
def health():
    return jsonify({'status': 'ok'})

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    from consumer import start_consumer
    threading.Thread(target=start_consumer, daemon=True).start()
    app.run(host='0.0.0.0', port=5000)