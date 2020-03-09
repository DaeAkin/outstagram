package outstagram.domain.feedmedia.application;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feedmedia.dao.FeedMediaRepository;
import outstagram.test.MockTest;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
public class FeedTestsMediaServiceTests extends MockTest {
    @Mock
    Authentication authentication;

    @Autowired
    FeedMediaRepository feedMediaRepository;

    String resourcePath = System.getProperty("user.home") + File.separator + "outstagram-resource-test" + File.separator;

    @InjectMocks
    FeedMediaServiceImpl feedMediaService = new FeedMediaServiceImpl(resourcePath,feedMediaRepository);

    @Test
    public void 여러개의_이미지_넣기_테스트() {
        //given
        List<MultipartFile> givenFileList = Arrays.asList(
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes())
        );

        feedMediaService.saveFeedMedia(5L,givenFileList);
    }

    @Test
    public void 동영상이랑_이미지_넣기_테스트() {
        //given
        List<MultipartFile> givenFileList = Arrays.asList(
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
                new MockMultipartFile("file", "test.mov", "MOV", "testImage".getBytes())
        );

        feedMediaService.saveFeedMedia(5L,givenFileList);
    }
}
