# armeria-transaction-sample

## reproduce

**Transaction on override method**

```bash
curl --request POST \
  --url http://localhost:8080/hello.v1.HelloService/HelloWithTransaction \
  --header 'Content-Type: application/json' \
  --data '{
	"name": "me"
}'
```

**No Transaction on method**

```bash
curl --request POST \
  --url http://localhost:8080/hello.v1.HelloService/HelloWithoutTransaction \
  --header 'Content-Type: application/json' \
  --data '{
	"name": "me"
}'
```

**Transaction on Dependency Injected Service method**

```bash
curl --request POST \
  --url http://localhost:8080/hello.v1.HelloService/HelloWithInnerTransaction \
  --header 'Content-Type: application/json' \
  --data '{
	"name": "me"
}'
```
