package xml.parse;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    private String ruleId;
    private int matchMode;
    private int matchValue;
    private int secretLevel;
    private String portal;
    private String account;
    private int urlLevelSetId;
    private int importantLevel;
    private int sogouRank;
    private String imageUrl;
    private String md5;
    private String word;
    private List<String> words = new ArrayList<>();
    private int status;
    private int syncStatus = 4;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public int getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(int matchMode) {
        this.matchMode = matchMode;
    }

    public int getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(int secretLevel) {
        this.secretLevel = secretLevel;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public void addWord(String word) {
        this.words.add(word);
    }

    public int getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(int matchValue) {
        this.matchValue = matchValue;
    }

    public int getUrlLevelSetId() {
        return urlLevelSetId;
    }

    public void setUrlLevelSetId(int urlLevelSetId) {
        this.urlLevelSetId = urlLevelSetId;
    }

    public int getImportantLevel() {
        return importantLevel;
    }

    public void setImportantLevel(int importantLevel) {
        this.importantLevel = importantLevel;
    }

    public String getWord() {
        if (this.word == null || this.word.length() == 0) {
            if (this.words.size() == 0) this.word = "";
            else this.word = XMLParsUtil.flat(this.words, "\ue40a");
        }
        return this.word;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getSogouRank() {
        return sogouRank;
    }

    public void setSogouRank(int sogouRank) {
        this.sogouRank = sogouRank;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void initAtts(Attributes atts) {
        this.ruleId = atts.getValue("rule_id");
        this.matchMode = XMLParsUtil.parseInt(atts.getValue("match_mode"));
        this.matchValue = XMLParsUtil.parseInt(atts.getValue("match_value"));
        this.urlLevelSetId = XMLParsUtil.parseInt(atts.getValue("url_level_set_id"));
        this.secretLevel = "authorized".equals(atts.getValue("secret_level")) ? 0: 1;
        this.importantLevel = XMLParsUtil.parseInt(atts.getValue("important_level"));
        this.portal = atts.getValue("portal");
        this.account = atts.getValue("account");
    }

    @Override
    public String toString() {
        return "Rule{" +
                "ruleId='" + ruleId + '\'' +
                ", matchMode=" + matchMode +
                ", matchValue=" + matchValue +
                ", secretLevel='" + secretLevel + '\'' +
                ", portal='" + portal + '\'' +
                ", account='" + account + '\'' +
                ", urlLevelSetId=" + urlLevelSetId +
                ", importantLevel=" + importantLevel +
                ", words=" + words +
                '}';
    }
}
