# Ultra Player

App IPTV Android nativo (Kotlin + Jetpack Compose), consumindo o painel
Maximus (`check_mac.php`) e a API Xtream Codes.

## ⚠️ Status atual

Esse é o **esqueleto inicial** do projeto — a estrutura Gradle completa, o
tema visual, e a tela de login funcionando (verifica o MAC do aparelho no
painel). A Home ainda é só uma tela de confirmação — o próximo passo é
montar as fileiras de conteúdo, o player de vídeo, etc.

**Importante:** esse código nunca foi compilado nem testado ainda (foi
escrito sem acesso a um ambiente com Android SDK). É bem provável que a
primeira tentativa de build encontre 1-2 erros pequenos de sintaxe ou de
versão de dependência — é normal, é só me colar o erro que eu corrijo.

## Como compilar no GitHub Codespaces

1. No repositório, clica em **Code → Codespaces → Create codespace on main**
2. Espera o Codespace abrir (pode levar 1-2 minutos)
3. No terminal do Codespace, gera o wrapper do Gradle (ele baixa a versão
   certa automaticamente):
   ```
   gradle wrapper --gradle-version 8.9
   ```
   Se o comando `gradle` não existir no Codespace, primeiro instala:
   ```
   sdk install gradle 8.9
   ```
   (usando o [SDKMAN](https://sdkman.io/), ou baixando manualmente)
4. Compila:
   ```
   ./gradlew assembleDebug
   ```
5. O APK gerado fica em `app/build/outputs/apk/debug/app-debug.apk`
6. Baixa esse arquivo do Codespaces (botão direito → Download) e instala
   no celular

## Confirmar antes de compilar

- `PANEL_BASE_URL` em `app/src/main/java/com/ultraplayer/app/network/PanelApi.kt`
  — confirma se é a URL certa de onde `check_mac.php` está hospedado
