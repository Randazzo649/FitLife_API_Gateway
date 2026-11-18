# FitLife API Gateway

API Gateway com Spring Cloud Gateway WebFlux. Responsabilidades: roteamento de APIs, CORS, validação JWT via JWKS e tratamento transversal de requisições. Não contém controllers, serviços ou DTOs de BFF.

Rotas: `/api/usuarios/**` -> usuario-service; `/api/treinos/**` -> treino-service; `/api/personais/**` -> personal-service; `/api/comunidade/**` -> comunidade-service; `/oauth2/jwks` -> JWKS do usuario-service.

Variáveis: `USUARIO_SERVICE_URI`, `TREINO_SERVICE_URI`, `PERSONAL_SERVICE_URI`, `COMUNIDADE_SERVICE_URI`, `JWT_ISSUER_URI`, `JWT_JWK_SET_URI`, `FITLIFE_FRONTEND_ORIGIN` e as origens de cada MFE.

## Configuração de produção no Azure

As variáveis abaixo devem ser cadastradas no App Service do API Gateway. Para origens CORS, informe somente a origem (scheme + host), sem caminho e sem barra final.

| Variável | Valor esperado |
|---|---|
| `FITLIFE_FRONTEND_ORIGIN` | `https://fitligemfes.z41.web.core.windows.net` |
| `FITLIFE_CADASTRO_ORIGIN` | `https://fitlifemfecadastrousuario-h7e7g0e7emfef4b7.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_PERFIL_ORIGIN` | `https://fitlifemfeperfil-h0gfcmh9gecshfhf.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_PERSONAL_ORIGIN` | `https://fitlifemfepersonal-h5e6ggcmhhh2f3av.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_TREINOS_ORIGIN` | `https://fitlifemfertreino-eneneqd6apc8cda5.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_COMUNIDADE_ORIGIN` | `https://fitlifemfecomunidade-g8g2dtdhh5axdzhr.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_USUARIO_SERVICE_ORIGIN` | `https://fitlifeusuarioservice-gbc5bdb6fccxc2e2.mexicocentral-01.azurewebsites.net` |

Mantenha também as quatro URIs de destino dos microsserviços já cadastradas no Azure:

- `USUARIO_SERVICE_URI` = `https://fitlifeusuarioservice-gbc5bdb6fccxc2e2.mexicocentral-01.azurewebsites.net`
- `TREINO_SERVICE_URI` = `https://fitlifetreinoservice-a0dbhhc6fxg8c2b0.mexicocentral-01.azurewebsites.net` (microsserviço, não MFE)
- `PERSONAL_SERVICE_URI` = `https://fitlifepersonalservice-bdgma4bka7a6f6b7.mexicocentral-01.azurewebsites.net`
- `COMUNIDADE_SERVICE_URI` = `https://fitlifecomunidadeservice-bvezezamd9a4brfy.mexicocentral-01.azurewebsites.net`

### JWT

- `JWT_JWK_SET_URI` deve apontar para `https://fitlifeusuarioservice-gbc5bdb6fccxc2e2.mexicocentral-01.azurewebsites.net/oauth2/jwks`.
- `JWT_ISSUER_URI` deve ser exatamente igual ao claim `iss` dos tokens emitidos pelo Usuario Service. Não altere esse valor por suposição; confira o token emitido no login.

### Regras públicas

- `OPTIONS /**`: permitido para preflight CORS.
- `GET /oauth2/jwks`: permitido para descoberta da chave pública.
- `GET /uploads/**`: permitido para carregar imagens públicas.
- `POST /api/usuarios/login` e `POST /api/usuarios/cadastro`: permitidos sem JWT.
- Demais requisições: exigem JWT válido.

As variáveis de origem CORS só afetam chamadas feitas pelo navegador. Requisições BFF → Gateway são servidor-servidor e não dependem de CORS.

## Integrantes

- Amanda Milleo
- Camila Araújo
- Pedro Henrique Kleim Vieira
- Riccardo Randazzo
- Yuri Sobezak

##Vídeo
https://www.youtube.com/watch?v=Sv49b03aIbU&feature=youtu.be
