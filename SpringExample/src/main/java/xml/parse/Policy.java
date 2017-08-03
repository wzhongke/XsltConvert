package xml.parse;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/27.
 */
public class Policy {
    private String baseVersion;
    private String newVersion;
    private String mode;
    private boolean logSearch;
    private boolean logMatch;
    private boolean logDigestWord;
    private boolean ipWhiteUrl;
    private boolean inputwordWhiteUrl;
    private boolean inputpicWhiteUrl;

    private List<URLRule> urlRules = new ArrayList<>();
    private Map<String, List<Rule>> ruleMap;

    public String getBaseVersion() {
        return baseVersion;
    }

    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isLogSearch() {
        return logSearch;
    }

    public void setLogSearch(boolean logSearch) {
        this.logSearch = logSearch;
    }

    public boolean isLogMatch() {
        return logMatch;
    }

    public void setLogMatch(boolean logMatch) {
        this.logMatch = logMatch;
    }

    public boolean isLogDigestWord() {
        return logDigestWord;
    }

    public void setLogDigestWord(boolean logDigestWord) {
        this.logDigestWord = logDigestWord;
    }

    public boolean isIpWhiteUrl() {
        return ipWhiteUrl;
    }

    public void setIpWhiteUrl(boolean ipWhiteUrl) {
        this.ipWhiteUrl = ipWhiteUrl;
    }

    public boolean isInputwordWhiteUrl() {
        return inputwordWhiteUrl;
    }

    public void setInputwordWhiteUrl(boolean inputwordWhiteUrl) {
        this.inputwordWhiteUrl = inputwordWhiteUrl;
    }

    public boolean isInputpicWhiteUrl() {
        return inputpicWhiteUrl;
    }

    public void setInputpicWhiteUrl(boolean inputpicWhiteUrl) {
        this.inputpicWhiteUrl = inputpicWhiteUrl;
    }

    public Map<String, List<Rule>> getRuleMap() {
        return ruleMap;
    }

    public void setRuleMap(Map<String, List<Rule>> ruleMap) {
        this.ruleMap = ruleMap;
    }

    public List<URLRule> getURLRules() {
        return urlRules;
    }

    public void setURLRules(List<URLRule> rules) {
        this.urlRules = rules;
    }

    public void addURLRule(URLRule rule) {
        this.urlRules.add(rule);
    }

    public void initAttr(Attributes atts) {
        this.baseVersion = atts.getValue("base_version");
        this.newVersion = atts.getValue("new_version");
        this.mode = atts.getValue("mode");
        this.logSearch = "yes".equalsIgnoreCase(atts.getValue("log_search"));
        this.logMatch = "yes".equalsIgnoreCase(atts.getValue("log_match"));
        this.logDigestWord = "yes".equalsIgnoreCase(atts.getValue("log_digest_word"));
        this.ipWhiteUrl = "yes".equalsIgnoreCase(atts.getValue("ip_whiteurl"));
        this.inputpicWhiteUrl = "yes".equalsIgnoreCase(atts.getValue("inputpic_whiteurl"));
        this.inputwordWhiteUrl = "yes".equalsIgnoreCase(atts.getValue("inputword_whiteurl"));
    }

    @Override
    public String toString() {
        return "Policy{" +
                "baseVersion='" + baseVersion + '\'' +
                ", newVersion='" + newVersion + '\'' +
                ", mode='" + mode + '\'' +
                ", logSearch=" + logSearch +
                ", logMatch=" + logMatch +
                ", logDigestWord=" + logDigestWord +
                ", ipWhiteUrl=" + ipWhiteUrl +
                ", inputwordWhiteUrl=" + inputwordWhiteUrl +
                ", inputpicWhiteUrl=" + inputpicWhiteUrl +
                ", urlRules=" + urlRules +
                ", ruleMap=" + ruleMap +
                '}';
    }
}
