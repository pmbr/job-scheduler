package org.scheduler;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.cloud.config.enabled = false"})
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public class IntegrationTest {
}
