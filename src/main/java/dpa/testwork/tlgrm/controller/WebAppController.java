package dpa.testwork.tlgrm.controller;

import dpa.testwork.tlgrm.process_user.WebAppDataService;
import dpa.testwork.tlgrm.process_user.dto.TelegramUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class WebAppController {

    private final WebAppDataService authService;

    @GetMapping
    public ResponseEntity<?> handleWebApp(
            HttpServletRequest request,
            @RequestParam(name = "initData", required = false) String initData,
            @RequestParam(name = "redirected", required = false) String redirected) throws Exception {

        if (initData == null) {
            if ("true".equals(redirected)) {
                return showAuthError();
            }
            return getInitPage();
        }

        if (!authService.validateInitData(initData)) {
            System.err.println("Подпись неверна");
            return showAuthError();
        }

        TelegramUser user = authService.parseUserData(initData);
        if (user == null) {
            System.err.println("Не удалось распарсить пользователя");
            return getInitPage();
        }

        System.out.println("Подпись верна");
        System.out.println("Пользователь: " + user.getFirstName() + " " + user.getLastName());

        return ResponseEntity.ok(generateUserPage(user));
    }

    private ResponseEntity<String> showAuthError() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("""
                <h1>Authentication Error</h1>
                <p>Failed to verify Telegram data authenticity.</p>
                <button onclick="window.Telegram.WebApp.close()">Close</button>
            """);
    }

    private ResponseEntity<String> getInitPage() {
        String html = """
        <!DOCTYPE html>
        <html>
        <head>
            <script src="https://telegram.org/js/telegram-web-app.js "></script>
            <script>
                window.onload = () => {
                    if (Telegram?.WebApp) {
                        Telegram.WebApp.ready();
                        const params = new URLSearchParams({
                            initData: Telegram.WebApp.initData || '',
                            redirected: 'true'
                        });
                        window.location.href = '/?' + params.toString();
                    }
                };
            </script>
        </head>
        <body>
            <h1>Redirecting...</h1>
        </body>
        </html>
    """;
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

    private String generateUserPage(TelegramUser user) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <title>User Info</title>
            <meta charset="UTF-8">
            <style>
                body { font-family: Arial, sans-serif; padding: 20px; }
                .user-card { border: 1px solid #ddd; padding: 20px; max-width: 400px; margin: 0 auto; }
            </style>
        </head>
        <body>
            <div class="user-card">
                <h2>User Information</h2>
                <p><strong>ID:</strong> %d</p>
                <p><strong>First Name:</strong> %s</p>
                %s
                %s
                <p><strong>Auth Date:</strong> %s</p>
            </div>
        </body>
        </html>
        """.formatted(
                user.getId(),
                user.getFirstName(),
                user.getLastName() != null ? "<p><strong>Last Name:</strong> " + user.getLastName() + "</p>" : "",
                user.getUsername() != null ? "<p><strong>Username:</strong> @" + user.getUsername() + "</p>" : "",
                user.getLastAuth()
        );
    }
}