package outstagram.domain.feedmedia.application;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import outstagram.global.utils.MediaUtil;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class FeedMediaServiceImpl implements FeedMediaService {

    @Value("${resource-path}media")
    private String resourcePath;


    @Override
    public boolean saveFeedMedia(List<MultipartFile> feedMediaList) {
        feedMediaList
                .forEach(fm -> MediaUtil.saveImageFile(fm,resourcePath,""));
        return true;
    }
}
