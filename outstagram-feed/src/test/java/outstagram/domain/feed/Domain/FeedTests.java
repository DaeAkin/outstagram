package outstagram.domain.feed.Domain;

import org.junit.Test;
import outstagram.domain.feed.domain.Feed;
import outstagram.test.MockTest;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
public class FeedTests extends MockTest {

    @Test
    public void 컨텐츠에서_해시태그만_추출_테스트() {
        //given
        List<String> expectList = Arrays.asList("인스타","인스타그램","맞팔","헤헤헤");
        String content = "#인스타 안녕하세요 인스타그램 입니다. 반가워요" +
                "#인스타그램 #맞팔 #헤헤헤 ";

        //when
        Feed feed = new Feed();
        List<String> hashTags = feed.getHashTags(content);
        //then
        assertThat(hashTags.containsAll(expectList));
    }
}
