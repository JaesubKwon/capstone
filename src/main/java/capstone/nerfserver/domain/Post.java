package capstone.nerfserver.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Post {

    private long id;  //post id(given by server)
    private long userId; //판매자 id
    private String title;
    private String content;
    private long price;
    private LocalDateTime date;
    private Long numberOfImages;
    private String state;

    public Post() {
    }
    public Post(long userId, String title, String content, long price, long numberOfImages) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.price = price;
        this.numberOfImages = numberOfImages;
        this.date = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.state = "waiting";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void updateDate(String date) {
        this.date = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public Long getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(Long numberOfImages) {
        this.numberOfImages = numberOfImages;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
