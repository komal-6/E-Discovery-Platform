package org.example.JobType.EmailHSD;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Regex {

    /**
     * Returns a list of regular expression patterns for matching email headers.
     * The returned list includes patterns for matching the from, To, Subject,
     * Reply-To, X-Mailer, and Date headers. Each pattern is a string that can be
     * used to construct a {@link Pattern} object for matching
     * against email header strings.
     * @return a list of
     * egular expression patterns for matching email headers
     */
    static List<Pattern> headerRegex() {

        List<String> headerRegexes= Arrays.asList(
                "^From:[\\s\\w+]+[\\.x|\\w+]+\\@\\w+\\.\\w+",
                "^From:\\s*(([\\w\\s,]+)?\\s*(<[\\w\\s@.-]+>)?\\s*(\\[([\\w.@-]+)\\])?|([\\w\\s,]+)?\\s*(<[\\w\\s@.-]+>)?\\s*\\[mailto:([\\w.-]+)@\\w+\\.\\w+\\]?|([\\w\\s,]+)|([\\w\\s]+)\\s+<([\\w.-]+@[\\w.-]+)>(\\s*\\[mailto:([\\w.-]+)@([\\w.-]+)\\])?|\\[mailto:([\\w.-]+)@[\\w.-]+\\]|.*<.*@.*\\..*>|(\\S+)\\s+\\[mailto:(\\S+)\\]|\\s*([\\w\\s]+)\\s+\\[mailto:([\\w.-]+)@([\\w.-]+)\\])$",
                "^To:(([\\s\\w+]+[\\.|\\w+]+\\@\\w+\\.\\w+(\\,[\\s\\w+]+[\\.|\\w+]+\\@\\w+\\.\\w+)?)|(\\s[\\w\\s]+<\\w+@\\w+\\.\\w+>(,)?))",
//                "^(\\s*((?:['\\w\\s]+,\\s)?['\\w\\s]+(?:\\s<[\\w.-]+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}>)?|[\\w\\s]+<[\\w.-]+(@)?[a-zA-Z_]+(-)?[a-zA-Z_]+\\.[a-zA-Z]{2,3}>|([\\w\\s]+),?\\s*)*(?:(?:;\\s*)?(?:(?:['\\w\\s]+,\\s)?['\\w\\s]+(?:\\s<[\\w.-]+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}>)?|[\\w\\s]+<[\\w.-]+(@)?[a-zA-Z_]+(-)?[a-zA-Z_]+\\.[a-zA-Z]{2,3}>|([\\w\\s]+),?\\s*)+)?(,)\\s*)",
                "^To:\\s*((?:['\\w\\s]+,\\s)?['\\w\\s]+(?:\\s<[\\w.-]+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}>)?|[\\w\\s]+<[\\w.-]+(@)?[a-zA-Z_]+(-)?[a-zA-Z_]+\\.[a-zA-Z]{2,3}>|([\\w\\s]+),?\\s*)*(?:(?:;\\s*)?(?:(?:['\\w\\s]+,\\s)?['\\w\\s]+(?:\\s<[\\w.-]+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}>)?|[\\w\\s]+<[\\w.-]+(@)?[a-zA-Z_]+(-)?[a-zA-Z_]+\\.[a-zA-Z]{2,3}>|([\\w\\s]+),?\\s*)+)?\\s*$",
                "^Subject:.*",
                "^Reply-To:[\\s\\w+]+[\\.|\\w+]+\\@\\w+\\.\\w+",
                "^X-Mailer:.*$",
                "^Date:\\s\\w{3},\\s\\d{2}\\s\\w{3}\\s\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\s[-+]\\d{4}",
                "^Date:\\s(\\b(?:\\d{1,2}(?:st|nd|rd|th)?\\s+)?(?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?)\\D{0,3}(?:\\s|,\\s)(?:\\d{1,2}(?:\\D{1,3}|\\s+))?(?:\\d{2}|\\d{4})(?:\\D|T|\\s+)\\d{1,2}(?::\\d{2}){1,2}(?:\\s+[AP]M)?(?:\\s+\\w+)?(?:\\s+\\+\\d{4})?|\\d{1,2}([-/.])\\d{1,2}\\1(?:\\d{2}|\\d{4})[ T]\\d{1,2}(?::\\d{2}){1,2}\\s(?:AM|PM)(?=\\b|\\s|\\d|')|\\d{1,2}([-/.])\\d{1,2}\\2(?:\\d{2}|\\d{4})[ T]\\d{1,2}(?::\\d{2}){1,2}(?:\\s(?:AM|PM|\\bCET\\b))?|(?:\\d{1,2}\\s+)?(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\\s+\\d{1,2}(?:st|nd|rd|th)?(?:\\D{1,3}|\\s+)\\d{2,4}(?:\\s+|,\\s+)\\d{1,2}(?::\\d{2}){1,2}(?:\\s+[AP]M)?(?:\\s+\\w+)?(?:\\s+\\+\\d{4})?|\\d{1,2}(?:[-/]\\d{1,2}){2,}(?:\\s+|,\\s+)\\d{1,2}(?::\\d{2}){1,2}(?:\\s+[AP]M)?(?:\\s+\\w+)?(?:\\s+\\+\\d{4})?|(\\d{1,2}[-/]\\w{3,4}[-/](?:\\d{2}|\\d{4})|\\w{3,4}[.-]\\s?\\d{1,2}[,-]\\s?\\d{4},?|\\d{4}[/-]\\d{2}[/-]\\d{2}[T\\s]\\d{2}:\\d{2}:\\d{2}(?:Z|[+-]\\d{2}:?\\d{2})?|\\w{3,9}[.-]\\s?\\d{1,2}[,-]?\\s?\\d{4}[,-]?|\\d{1,2}[-/]\\w{3,4}[-/](?:\\d{2}|\\d{4})\\s\\d{2}:\\d{2}:\\d{2}\\s(?:AM|PM)?|[A-Z][a-z]{2}\\.\\s\\d{1,2},?\\s\\d{4},?\\s\\d{2}:\\d{2}:\\d{2}\\s(?:AM|PM)?))",
                "^Date:\\s*(\\d{1,2}(?:st|nd|rd|th)?(?:\\s*(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)|\\/|-|\\.)\\s*(?:\\d{1,2}(?:st|nd|rd|th)?|\\d{4}))(?:(?:\\s|T)\\s*(\\d{1,2}(?::\\d{2}){1,2})(?:\\s*(?:AM|PM))?)?(?:\\s*(?:GMT|[+\\-]\\d{2}(?::?\\d{2})?|CET|CEST|IST|PDT|PST|EDT|EST|CDT|CST|BST|BRT|ART|EEST|EET|MSK|UTC|Z|A|B|C|D|E|F|G|H|I|K|M|N|O|P|R|S|T|U|V|W|X|Y))?.*",
                "^Date:(\\s(\\d{1,2}[-/]\\w{3,4}[-/](?:\\d{2}|\\d{4})|\\w{3,4}[.-]\\s?\\d{1,2}[,-]\\s?\\d{4},?|\\d{4}[/-]\\d{2}[/-]\\d{2}[T\\s]\\d{2}))",
                "^\\s*((((\\W[a-zA-Z]+(.)?)+)+((\\W[a-zA-Z]+)+)?\\s*)?)<[^\\s@]+@[^\\s@]+\\.[^\\s@]+>(,)?",
                "^Reply-to:\\s\\S+@\\S+\\.\\S+",
                "^(CC|Cc|cc):((\\s+([\\w\\s]+)\\s+<([\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,3})>(,))|(\\s*(([\\w\\s]+);?\\s*)+$)|(\\s*(([\\w\\s]+);?\\s*)+(<(\\w+)@(\\w+).com>(,)?))|([\\s]*\\w+((\\-)?\\w+)?@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}))",
                "^(Sent:\\s+(Mon|Tue(s)?|Wednes|Thu(rs)?|Fri|Sat(ur)?|Sun)day,\\s+(January|February|March|April|May|June|July|August|September|October|November|December)\\s+\\d{1,2}(,)?\\s+\\d{4}\\s+\\d{1,2}:\\d{2}(:\\d{2})?\\s+(AM|PM))|(Sent:\\s+(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun),\\s\\d{2}\\s(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\s[+-]\\d{4})"
        );

        List<Pattern> HeaderRegexes = headerRegexes.stream().map(Pattern::compile).collect(Collectors.toList());

        return HeaderRegexes;
    }

    /**
     Returns a list of regular expression patterns for detecting signatures in an email message.
     The patterns include common signature patterns such as "Best regards", "Sincerely", "Regards", and "Phone:", among others.
     @return a list of regular expression patterns for detecting signatures in an email message
     */
    static List<Pattern> signatureRegex(){
        List<String> regexes = Arrays.asList(
            "^(?:Best regards|Sincerely|Warmly|Many thanks|Regards|Kind regards|Yours truly|Thanks|Best|Warm regards|All the best|Cordially|Respectfully|Cheers|With gratitude|Yours sincerely|Faithfully|Take care|Have a great day|Until next time|Wishing you well|In appreciation),.*",
            "^(Name: ).\\S+(\\s+\\S+){0,2}$",
            "^(?!Dear\\s)[a-zA-Z0-9/\\''(),\\s]{2,80}$",
            "Phone:\\s*(\\+?\\d{0,3}[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}(?:\\s*(?:ext|x)\\.? \\d{1,5})?)",
            "^Website:.*",
            "^Email:(?:\\w+: )?[\\s\\w+]+[\\.|\\w+]+\\@\\w+\\.\\w+"
        );

        List<Pattern> SignaturePatterns = regexes.stream().map(Pattern::compile).collect(Collectors.toList());

        return SignaturePatterns;
    }
}

