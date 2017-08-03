package xml.parse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SAX 方式解析xml
 */
public class SAXParser {

    // 创建xml解析器。
    private static SAXParser saxParser = new SAXParser();
    private static XMLReader parser;
    private static BookHandler bookHandler;

    private SAXParser() {
        try {
            // 初始化xml解析器
            parser = XMLReaderFactory.createXMLReader();
            bookHandler = new BookHandler();
            parser.setContentHandler(bookHandler);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static SAXParser getInstance() {
        return saxParser;
    }

    private static class BookHandler extends DefaultHandler {
        private Policy policy;
        private Charset charset;
        private String ruleType;
        private Rule rule;
        private URLRule urlRule;
        private XmlTag tagName;
        private boolean isPic;
        private Map<String ,List<Rule>> ruleMap;

        /**
         * 在xml解析开始的时候调用
          */
        @Override
        public void startDocument() throws SAXException {
            policy = new Policy();
            ruleMap = new HashMap<>();
        }

        /**
         *  在xml解析结束的时候调用
         */
        @Override
        public void endDocument() throws SAXException {
            policy.setRuleMap(ruleMap);
        }

        /**
         * 开始解析一个元素
         * @param uri : 命名空间URI，如果没有的话，为空字符串
         * @param localName: 本地名，如果命名空间没有被处理的话，为空字符串
         * @param qName: 元素名称（带前缀）或 如果限定名不可用，则为空字符串
         * @param attr: 元素的属性
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
            // 使用 qualified name, 在此没有使用xmlns prefixes
            if (qName != null && qName.contains("word")) {
                tagName = XmlTag.WORD;
            } else {
                tagName = XmlTag.value(qName);
            }

            switch (tagName) {
                case POLICY:
                    policy.initAttr(attr); break;
                case RULES:
                    ruleType = attr.getValue("class");
                    if ("no".equals(attr.getValue("valid")) || ruleType == null) {
                        ruleType = null;
                    }
                    break;
                case RULE:
                    if (ruleType != null) {
                        rule = new Rule();
                        rule.initAtts(attr);

                        if (! ruleMap.containsKey(ruleType)) {
                            ruleMap.put(ruleType, new ArrayList<>());
                        }
                        ruleMap.get(ruleType).add(rule);
                    }
                    break;
                case WORD:
                    charset = Charset.forName(attr.getValue("encoding"));
                    break;
                case URL_SET:
                    urlRule = new URLRule();
                    urlRule.initAttr(attr);
                    policy.addURLRule(urlRule);

            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
           tagName = XmlTag.NullResult;
        }

        /**
         * @param ch The characters.
         * @param start The start position in the character array.
         * @param length The number of characters to use from the character array.
         */

        @Override
        public void characters(char[] ch, int start, int length) {
            // Processing character data inside an element
            if (tagName == XmlTag.WORD) {
                 rule.addWord(XMLParsUtil.getFromBase64(new String(ch, start, length)));
            } else if (tagName == XmlTag.GREEN_URL) {
                urlRule.addUrl(XMLParsUtil.getFromBase64(new String(ch, start, length)));
            }
        }
    }

    public Policy getPolicy(String xmlPath) throws SAXException, IOException {
        parser.parse(xmlPath);
        return bookHandler.policy;
    }


    private enum XmlTag {
        POLICY ("policy_of_filter"),
        RULES ("rule_list"),
        RULE("rule"),
        WORD("word"),
        URL_SET("url_level_set"),
        GREEN_URL("greenurl"),
        NullResult("");
        private String xmlTag;


        XmlTag(String xmlTag) {
            this.xmlTag = xmlTag;
        }

        private static Map<String, XmlTag> tagMap = new HashMap<>(4);
        static {
            for (XmlTag xml : XmlTag.values()) {
                tagMap.put(xml.xmlTag, xml);
            }
        }

        public static XmlTag value(String value) {
            if (tagMap.containsKey(value)) {
                return tagMap.get(value);
            }
            return NullResult;
        }
    }
}
