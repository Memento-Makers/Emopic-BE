package mmm.emopic.app.domain.photo.dto.response;

import lombok.Data;
import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse {
    private List<PhotosInformationResponse> content;

    private long totalElements;
    private int totalPage;
    private boolean last;
    private int numberOfElements;
    private int currentPage;
    private boolean empty;

    public PageResponse(Page<PhotosInformationResponse> result) {
        this.currentPage = result.getNumber();
        this.totalPage = result.getTotalPages();
        this.last = result.isLast();
        this.content = result.getContent();
        this.numberOfElements = result.getNumberOfElements();
        this.totalElements = result.getTotalElements();
        this.empty = result.isEmpty();
    }
}
