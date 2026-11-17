# Configuração do API Gateway no Azure

Recurso: App Service `FitLifeAPIGateway`.

## 1. Origens autorizadas pelo CORS

Cadastre as seguintes configurações de aplicativo. Os valores não devem ter barra final.

| Nome | Valor |
|---|---|
| `FITLIFE_FRONTEND_ORIGIN` | `https://fitligemfes.z41.web.core.windows.net` |
| `FITLIFE_CADASTRO_ORIGIN` | `https://fitlifemfecadastrousuario-h7e7g0e7emfef4b7.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_PERFIL_ORIGIN` | `https://fitlifemfeperfil-h0gfcmh9gecshfhf.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_TREINOS_ORIGIN` | `https://fitlifemfertreino-eneneqd6apc8cda5.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_PERSONAL_ORIGIN` | `https://fitlifemfepersonal-h5e6ggcmhhh2f3av.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_COMUNIDADE_ORIGIN` | `https://fitlifemfecomunidade-g8g2dtdhh5axdzhr.mexicocentral-01.azurewebsites.net` |
| `FITLIFE_USUARIO_SERVICE_ORIGIN` | `https://fitlifeusuarioservice-gbc5bdb6fccxc2e2.mexicocentral-01.azurewebsites.net` |

`FITLIFE_USUARIO_SERVICE_ORIGIN` foi acrescentada porque o Swagger do Usuario Service faz chamadas ao Gateway a partir da origem do próprio Usuario Service.

## 2. Destinos das rotas

Mantenha estas variáveis já configuradas no App Service:

- `USUARIO_SERVICE_URI` = `https://fitlifeusuarioservice-gbc5bdb6fccxc2e2.mexicocentral-01.azurewebsites.net`
- `TREINO_SERVICE_URI` = `https://fitlifetreinoservice-a0dbhhc6fxg8c2b0.mexicocentral-01.azurewebsites.net` (microsserviço de Treino; não confundir com o MFE de Treinos).
- `PERSONAL_SERVICE_URI` = `https://fitlifepersonalservice-bdgma4bka7a6f6b7.mexicocentral-01.azurewebsites.net`
- `COMUNIDADE_SERVICE_URI` = `https://fitlifecomunidadeservice-bvezezamd9a4brfy.mexicocentral-01.azurewebsites.net`

## 3. JWT

- `JWT_JWK_SET_URI` = `https://fitlifeusuarioservice-gbc5bdb6fccxc2e2.mexicocentral-01.azurewebsites.net/oauth2/jwks`
- `JWT_ISSUER_URI` = o valor exato do claim `iss` retornado pelo Usuario Service nos JWTs. Não use uma URL presumida: compare com o payload de um token emitido no login.

Não compartilhe tokens, chaves privadas ou credenciais.

## 4. Alterações realizadas no código

1. Adicionada `FITLIFE_USUARIO_SERVICE_ORIGIN` à lista `allowedOrigins` do CORS.
2. Mantidas as rotas e os fallbacks locais para desenvolvimento.
3. A rota `GET /oauth2/jwks` permanece público.
4. Somente `GET /uploads/**` permanece público. Outros métodos em `/uploads/**` passam pela regra final e exigem JWT.
5. Login e cadastro continuam públicos por `POST`; as outras rotas continuam exigindo JWT.

## 5. Aplicação

1. Revise as variáveis no App Service `FitLifeAPIGateway`.
2. Publique esta versão pelo workflow do repositório do Gateway.
3. Teste `GET /oauth2/jwks`.
4. Teste `POST /api/usuarios/login` e depois uma rota protegida usando `Authorization: Bearer <access_token>`.
5. Teste o Swagger do Usuario Service após adicionar `FITLIFE_USUARIO_SERVICE_ORIGIN`.

O `JWT_ISSUER_URI` precisa corresponder exatamente ao claim `iss`; o ZIP do Gateway não contém como provar qual string o Usuario Service está emitindo atualmente.
