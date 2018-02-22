package blogCrawler.entity;

import java.util.Date;
import java.util.List;

public class Article {

    private String title;

    private String orUrl;

    private String content;

    private List<String> imageList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getOrUrl() {
        return orUrl;
    }

    public void setOrUrl(String orUrl) {
        this.orUrl = orUrl == null ? null : orUrl.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
    
}