package outstagram.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import outstagram.config.TestProfile;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(TestProfile.TEST)
//@Ignore
public class MockTest {

    @Test
    public void test() {

    }
}