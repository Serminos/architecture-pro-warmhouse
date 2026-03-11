package kafka

import (
    "context"
    "encoding/json"
    "log"
    "os"
    "time"
    "fmt"

    "github.com/segmentio/kafka-go"
)

var writer *kafka.Writer

func Init() {
    brokers := []string{os.Getenv("KAFKA_BOOTSTRAP_SERVERS")}
    if len(brokers) == 0 || brokers[0] == "" {
        brokers = []string{"kafka:9092"} // default for docker-compose
    }
    writer = &kafka.Writer{
        Addr:         kafka.TCP(brokers...),
        Balancer:     &kafka.LeastBytes{},
        RequiredAcks: kafka.RequireOne,
        Async:        true,
    }
    log.Println("Kafka producer initialized with brokers:", brokers)
}

func Close() {
    if writer != nil {
        writer.Close()
    }
}

func Publish(topic string, key string, value interface{}) error {
    if writer == nil {
        return fmt.Errorf("kafka producer not initialized")
    }
    data, err := json.Marshal(value)
    if err != nil {
        return err
    }
    msg := kafka.Message{
        Topic: topic,
        Key:   []byte(key),
        Value: data,
        Time:  time.Now(),
    }
    return writer.WriteMessages(context.Background(), msg)
}