package cn.fzk.mySpringBoot.param;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */
public class EmailParam {
    private String from;
    private String to;
    private String subject;
    private String text;
    private String pic;
    private List<String> files;
    /**
     * 是否使用模板,0:否，1是
     * */
    private Integer isUseTem = 0;
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Integer getIsUseTem() {
        return isUseTem;
    }

    public void setIsUseTem(Integer isUseTem) {
        this.isUseTem = isUseTem;
    }
}
