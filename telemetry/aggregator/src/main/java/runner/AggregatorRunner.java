package runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import service.AggregationStarter;

@Component
@RequiredArgsConstructor
public class AggregatorRunner implements CommandLineRunner {
    private final AggregationStarter aggregationStarter;

    @Override
    public void run(String... args) throws Exception {
        aggregationStarter.start();
    }
}
