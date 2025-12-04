curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pepito Grillo",
    "username": "pepitogrillo",
    "password": "123123123",
    "passwordRepeat": "123123123"
  }' \
  -v