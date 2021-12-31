.PHONY: up
up:
	docker-compose up -d && docker logs -f titlebot_titlebot_1 --tail=500

.PHONY: down
down:
	docker-compose down

