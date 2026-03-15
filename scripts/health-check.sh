#!/bin/bash

# Health Check Script para SRM Credit Engine

HOST="http://localhost:8081"
HEALTH_ENDPOINT="/api/actuator/health"
MAX_RETRIES=30
RETRY_INTERVAL=5

echo "🔍 Verificando saúde da aplicação..."

for i in $(seq 1 $MAX_RETRIES); do
    RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$HOST$HEALTH_ENDPOINT")

    if [ "$RESPONSE" -eq 200 ]; then
        echo "✅ Aplicação está saudável!"
        exit 0
    fi

    echo "⏳ Tentativa $i/$MAX_RETRIES - Aguardando... (HTTP $RESPONSE)"
    sleep $RETRY_INTERVAL
done

echo "❌ Aplicação não respondeu após $MAX_RETRIES tentativas"
exit 1