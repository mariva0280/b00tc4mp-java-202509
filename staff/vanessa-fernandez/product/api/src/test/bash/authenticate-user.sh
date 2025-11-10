curl -X POST http://localhost:8080/api/users/auth \
  -H "Content-Type: application/json" \
  -d '{
    "username": "pepitogrillo",
    "password": "123123123"
  }' \
  -v