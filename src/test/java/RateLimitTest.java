import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitTest {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitTest.class);

    private static final String URL = "https://ced.discloud.app/api/magias";

    private static final int TOTAL_REQUESTS = 101;

    private static final int THREAD_COUNT = 50;

    public static void main(String[] args) throws InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger rateLimitedCount = new AtomicInteger(0);
        AtomicInteger otherFailures = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(TOTAL_REQUESTS);

        logger.info("Iniciando teste de rate limiting: Enviando {} requisições para {}", TOTAL_REQUESTS, URL);

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            executor.submit(() -> {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(URL))
                            .timeout(Duration.ofSeconds(10))
                            .build();

                    HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

                    int statusCode = response.statusCode();

                    if (statusCode == 200) {
                        successCount.incrementAndGet();
                        logger.debug("Requisição SUCESSO (200)");
                    } else if (statusCode == 429) {
                        rateLimitedCount.incrementAndGet();
                        logger.debug("Requisição BLOQUEADA (429)");
                    } else {
                        otherFailures.incrementAndGet();
                        logger.warn("Requisição com código inesperado: {}", statusCode);
                    }
                } catch (Exception e) {
                    otherFailures.incrementAndGet();
                    logger.error("Erro ao enviar requisição: {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        executor.shutdown();

        logger.info("Teste concluído.");
        logger.info("Total de requisições enviadas: {}", TOTAL_REQUESTS);
        logger.info("Sucesso (200): {}", successCount.get());
        logger.info("Bloqueadas (429): {}", rateLimitedCount.get());
        logger.info("Falhas (outros códigos ou erros): {}", otherFailures.get());
    }
}
