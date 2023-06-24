package org.example.JobType.EmailHSD;

import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;
import org.example.JobType.EmailHSD.TagData;
import org.example.JobType.EmailHSD.Validate;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.JobType.EmailHSD.Variables.MIN_PARAGRAPH_LENGTH;

public class EmailHSD {

    private static final List<String> DISCLAIMER_KEYWORDS_LIST = Arrays.asList("This", "email","attachments", "Confidential", "contain", "privileged", "Privileged", "Copyright", "Legal", "Disclaimer",
                 "Notice", "Privacy", "Disclosure", "Recipient", "Sender", "Electronic", "Information", "Transmission", "Virus", "Malware", "Attachments",
                "Access", "Unauthorized", "Intended", "Consent", "Sensitive", "Attorney", "Client", "Advisor", "Financial", "Investment", "Recommendation",
                "Opinion", "Expression", "Personal", "Business", "Official", "Government", "Regulated", "Regulatory", "Professional", "Ethical", "Standards",
                "Conduct", "Practice", "Indemnity", "Negligence", "Misrepresentation", "Errors", "Omissions", "Warranties", "Liability", "Force", "Maure",
                "Termination", "Severability", "Governing", "Law", "Jurisdiction", "Arbitration", "Confidentiality", "Non-Disclosure", "Proprietary", "Trade",
                "Secrets", "Intellectual", "Property", "Patents", "Trademarks", "Copyrights", "License", "Permitted", "Prohibited ", "Misuse", "Fraud", "Laundering",
                "Sanctions", "Compliance", "Laws", "Anti-Bribery", "Anti-Corruption", "Anti-Money", "Data", "Policy", "Cookie", "Website");

    private static final Logger log = Logger.getLogger(EmailHSD.class);

    /**
     Extracts the HSD (Header, Signature, and Disclaimer) from the input file at the given path, tags each line with the appropriate tag (@header, @signature, or @disclaimer), and validates the HSD to produce an output line.
     @param id the id of the input file
     @param path the path of the input file
     @return a Text object containing the validated HSD lines, or "0" if the input file is empty or not found
     @throws NullPointerException if the input file is empty
     */
    public Text extractHSD(String id, String path) {
        Text outputLine = null;
        try {
            log.info("Reading input file as a paragraph, file id : " + id);

            String input = readFile(path);

            // Split the input by blank lines
            String[] paragraphs;
            if (input.equals("empty")) {
                Text output = new Text("0");
                log.info("File is empty id-"+ id);
                return output;
            }else if(input.equals("Not found")){
                Text output = new Text("0");
                log.error("File not found! id-" + id);
                return output;
            }else {
                paragraphs = input.split("(?m)^[ \t]*\r?\n");
            }

            List<String> disclaimerLines;
            log.info("Filtering paragraphs with a length of at least " + MIN_PARAGRAPH_LENGTH + " characters...");
            // Check paragraph for disclaimer
            disclaimerLines = Arrays.stream(paragraphs)
                    .filter(paragraph -> paragraph.length() >= MIN_PARAGRAPH_LENGTH)
                    .filter(this::containsKeywords)
                    .flatMap(paragraph -> Arrays.stream(paragraph.split("\\n")))
                    .collect(Collectors.toList());

            // Read file as lines
            log.info("Reading input file as lines, file id : " + id);
            String[] lines = new String[0];
            if (input != null) {
                lines = input.split("\\n");
            }

            // Tag lines with header, signature and disclaimer tags
            log.info("Tagging lines with header, signature and disclaimer tags...");
            String[] copyTagged = TagData.tagData(lines, disclaimerLines);

            // Validate HSD
            Validate validate = new Validate();
            if(copyTagged!= null) {
//                outputLine = validate.validHSD(copyTagged);
                outputLine = DemoValidate.demoValidate(copyTagged);
            }
        } catch (NullPointerException e) {
            log.error("File is empty", e);
            return new Text("0");
        }

        return outputLine;
    }

    /**
     Reads the contents of a file located at the specified path and returns the contents as a String.
     @param path the path of the file to be read
     @return a String containing the contents of the file at the specified path, or an error message if the file is not found or empty
     */
    private String readFile(String path) {
        String input;
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\Z"); // Read the entire file as a single string
            input = scanner.next();
            scanner.close();
        } catch (NullPointerException | NoSuchElementException e) {
            log.error("File is empty");
            return "empty";
        } catch (Exception e) {
            log.error("File not found!");
            return "Not found";
        }
        return input;
    }

    /**
     Checks if the given paragraph contains at least 5 keywords from the DISCLAIMER_KEYWORDS array.
     @param paragraph the paragraph to check for keywords
     @return true if the paragraph contains at least 5 keywords from the DISCLAIMER_KEYWORDS array, false otherwise
     */
    private boolean containsKeywords(String paragraph) {
        int count = 0;
        try{
            for (String keyword : DISCLAIMER_KEYWORDS_LIST) {
                if (paragraph.toUpperCase().contains(keyword.toUpperCase())) {
                    count++;
                }
            }
        }catch(NullPointerException e){
            log.error("No keywords in Disclaimer array");
            return false;
        }catch(ArrayIndexOutOfBoundsException e){
            log.error("Array index out of bound of the array \"DISCLAIMER_KEYWORDS\"");
            return false;
        }
        return count >= 5;
    }
}
