#!/bin/sh
set -e

echo "Ожидание запуска Keycloak..."
# Ждём Keycloak (проверяем endpoint master)
for i in $(seq 1 30); do
  if curl -s -f "${KEYCLOAK_AUTH_SERVER_URL}/realms/master" > /dev/null; then
    echo "✅ Keycloak доступен!"
    break
  fi
  echo "⏳ Keycloak недоступен, ждем 10 секунд..."
  sleep 10
done

echo "Получение токена администратора..."
TOKEN_RESPONSE=$(curl -s -X POST "${KEYCLOAK_AUTH_SERVER_URL}/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=${KEYCLOAK_ADMIN}" \
  -d "password=${KEYCLOAK_ADMIN_PASSWORD}" \
  -d "grant_type=password" \
  -d "client_id=admin-cli")

TOKEN=$(echo "$TOKEN_RESPONSE" | grep -o '"access_token":"[^"]*"' | awk -F':' '{print $2}' | tr -d '"')
if [ -z "$TOKEN" ]; then
  echo "❌ Не удалось получить токен администратора!"
  exit 1
fi
echo "✅ Токен администратора получен."

echo "Получение UUID клиента для ${KEYCLOAK_CLIENT_ID}..."
CLIENTS_RESPONSE=$(curl -s -X GET "${KEYCLOAK_AUTH_SERVER_URL}/admin/realms/${KEYCLOAK_REALM_NAME}/clients" \
  -H "Authorization: Bearer ${TOKEN}")

CLIENT_UUID=$(echo "$CLIENTS_RESPONSE" | grep -o '"id":"[^"]*","clientId":"'${KEYCLOAK_CLIENT_ID}'"' | awk -F'"' '{print $4}')
if [ -z "$CLIENT_UUID" ]; then
  echo "❌ Клиент ${KEYCLOAK_CLIENT_ID} не найден!"
  exit 1
fi
echo "✅ UUID клиента найден: ${CLIENT_UUID}"

echo "Получение client secret..."
SECRET_RESPONSE=$(curl -s -X GET "${KEYCLOAK_AUTH_SERVER_URL}/admin/realms/${KEYCLOAK_REALM_NAME}/clients/${CLIENT_UUID}/client-secret" \
  -H "Authorization: Bearer ${TOKEN}")

CLIENT_SECRET=$(echo "$SECRET_RESPONSE" | grep -o '"value":"[^"]*"' | awk -F':' '{print $2}' | tr -d '"')
if [ -z "$CLIENT_SECRET" ]; then
  echo "❌ Не удалось получить client secret!"
  exit 1
fi
echo "✅ Новый client secret: ${CLIENT_SECRET}"

# Устанавливаем переменную окружения и запускаем приложение
export KEYCLOAK_CLIENT_SECRET="${CLIENT_SECRET}"
echo "Запуск приложения..."
exec java -jar app.jar
