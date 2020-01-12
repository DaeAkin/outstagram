package outstagram.test;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import outstagram.config.TestProfile;

//@DataJpaTest
//DataJpaTest는 @Component 빈들이 ApplicationContext로 적재가 안됨. 그래서 Oauth bean들이 오류나는듯.
//대안으로 SpringBootTests를 써야할듯.
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(TestProfile.TEST)
//@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Ignore
public class RepositoryTest {

}
