package outstagram.global.domain;

import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

//@Getter
//@MappedSuperclass
//@ToString
//@EntityListeners(AuditingEntityListener.class)
public class BaseAuditingEntity {
    @Setter
    @CreatedDate
    public LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime modifiedTime;


}
