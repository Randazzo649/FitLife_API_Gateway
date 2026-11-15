# FitLife API Gateway

API Gateway com Spring Cloud Gateway WebFlux. Responsabilidades: roteamento de APIs, CORS, validação JWT via JWKS e tratamento transversal de requisições. Não contém controllers, serviços ou DTOs de BFF.

Rotas: `/api/usuarios/**` -> usuario-service; `/api/treinos/**` -> treino-service; `/api/personais/**` -> personal-service; `/api/comunidade/**` -> comunidade-service; `/oauth2/jwks` -> JWKS do usuario-service.

Variáveis: `USUARIO_SERVICE_URI`, `TREINO_SERVICE_URI`, `PERSONAL_SERVICE_URI`, `COMUNIDADE_SERVICE_URI`, `JWT_ISSUER_URI`, `JWT_JWK_SET_URI`, `FITLIFE_FRONTEND_ORIGIN` e as origens de cada MFE.
