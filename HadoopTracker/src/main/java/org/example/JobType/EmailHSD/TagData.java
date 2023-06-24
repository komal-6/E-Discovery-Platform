//package org.example;
//
//import org.apache.log4j.Logger;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.regex.Pattern;
//
//import static org.example.Variables.*;
//
//public class TagData {
//
//    private static final Logger log = Logger.getLogger(TagData.class);
//
//    /**
//     Tags the given data lines based on various criteria such as headers, signatures, blank lines, disclaimers, and body text.
//     @param lines the array of data lines to be tagged
//     @param disclaimerLines the list of lines that constitute the disclaimer
//     @return a string array containing the tagged lines
//     */
//    public static String[] tagData(String[] lines, List<String> disclaimerLines) {
//        log.info("Tagging data...");
//        String[] tagLines = new String[lines.length];
//
//        tagHeaders(lines, tagLines);
//        tagSignatures(lines, tagLines);
//        tagEmptyLines(lines, tagLines);
//        tagDisclaimer(lines, tagLines, disclaimerLines);
//        tagBody(lines, tagLines);
//
//        log.info("Tagging data completed.");
//
//        return filterBlankLines(tagLines);
//    }
//
//    /**
//     Tags headers in the given lines array with a header prefix and line number, and stores the tagged lines in the
//     tagLines array.
//     @param lines the array of lines to be tagged
//     @param tagLines the array in which the tagged lines will be stored
//     @throws NullPointerException if either the lines array or the tagLines array is null
//     @throws ArrayIndexOutOfBoundsException if the index of the current line being processed is out of bounds
//     */
//    private static void tagHeaders(String[] lines, String[] tagLines) {
//        log.info("Tagging headers...");
//        try {
//            for (int i = 0; i < lines.length; i++) {
//                String line = lines[i];
//                Optional<Pattern> foundPattern = Regex.headerRegex().stream().filter(pattern -> pattern.matcher(line).find()).findFirst();
//                if (foundPattern.isPresent()) {
//                    String taggedLine = HEADER_PREFIX + line + SPLIT_SIGN + (i + 1);
//                    tagLines[i] = taggedLine;
//                }
//            }
//        } catch (NullPointerException e) {
//            log.error("list or tagLines array is null");
//        } catch (ArrayIndexOutOfBoundsException e) {
//            log.error("Array index is out of bound");
//        }
//    }
//
//    /**
//     Tags the signatures in the given lines array with a signature prefix and line number and adds them to the tagLines array.
//     @param lines the array of lines to search for signatures
//     @param tagLines the array of tagged lines, where signatures will be tagged with a prefix indicating their type and line number
//     @throws ArrayIndexOutOfBoundsException if the index of the current line being processed is out of bounds
//     */
//    private static void tagSignatures(String[] lines, String[] tagLines) {
//        log.info("Tagging signatures...");
//        try {
//
//            for (int i = 0; i < lines.length; i++) {
//                String line = lines[i];
//                Optional<Pattern> matchingPattern = Regex.signatureRegex().stream().filter(pattern -> pattern.matcher(line).find()).findFirst();
//                if (matchingPattern.isPresent()) {
//                    String taggedLine = SIGNATURE_PREFIX + line + SPLIT_SIGN + (i + 1);
//                    tagLines[i] = taggedLine;
//                }
//            }
//        }catch(ArrayIndexOutOfBoundsException e){
//            log.error("Array is out of bound");
//        }
//    }
//
//    /**
//     Tags blank lines in the given array of lines and populates the tagLines array with the corresponding tags.
//     @param lines the array of lines to be tagged
//     @param tagLines the array to which the tags will be added
//     */
//    private static void tagEmptyLines(String[] lines, String[] tagLines) {
//        log.debug("Tagging blank lines...");
//        try {
//            for (int j = 0; j < lines.length; j++) {
//                Optional<String> line = Optional.ofNullable(lines[j]);
//                if (line.map(String::trim).orElse("").isEmpty()) {
//                    tagLines[j] = "BlankLine";
//                }
//            }
//        } catch (NullPointerException e) {
//            log.error("tagLines array is null");
//        } catch (ArrayIndexOutOfBoundsException e) {
//            log.error("Array index is out of bound");
//        }
//    }
//
//    /**
//     Tags the lines in the given array that contain disclaimer text by adding a prefix and line number to them and
//     storing them in the corresponding index of the tagLines array.
//     @param lines the array of text lines to be searched for disclaimer text
//     @param tagLines the array of tagged lines, where each tagged line is marked with a prefix indicating its type
//     @param disclaimerLines the list of lines that contain disclaimer text
//     @throws NullPointerException if the tagLines array is null
//     @throws ArrayIndexOutOfBoundsException if the index of the current line being processed is out of bounds
//     */
//    private static void tagDisclaimer(String[] lines, String[] tagLines, List<String> disclaimerLines) {
//        log.debug("Tagging disclaimer lines...");
//        try {
//            for (int j = 0; j < lines.length; j++) {
//                if (tagLines[j] == null && disclaimerLines.contains(lines[j])) {
//                    tagLines[j] = DISCLAIMER_PREFIX + lines[j] + SPLIT_SIGN + (j + 1);
//                }
//            }
//        } catch (NullPointerException e) {
//            log.error("tagLines array is null");
//        } catch (ArrayIndexOutOfBoundsException e) {
//            log.error("Array index is out of bound");
//        }
//    }
//
//    /**
//     Tags the lines of the body with a prefix indicating that they are part of the body and a split sign followed by their index in the original array.
//     @param lines the array of lines in the body
//     @param tagLines the array of tagged lines to which the tagged body lines will be added
//     @throws NullPointerException if the tagLines array is null
//     @throws ArrayIndexOutOfBoundsException if the index of the current line being processed is out of bounds
//     */
//    private static void tagBody(String[] lines, String[] tagLines) {
//        log.debug("Tagging body lines...");
//        try {
//            for (int j = 0; j < lines.length; j++) {
//                if (tagLines[j] == null) {
//                    tagLines[j] = BODY_PREFIX + lines[j] + SPLIT_SIGN + (j + 1);
//                }
//            }
//        } catch (NullPointerException e) {
//            log.error("tagLines array is null");
//        } catch (ArrayIndexOutOfBoundsException e) {
//            log.error("Array index is out of bound");
//        }
//    }
//
//    /**
//     Filters out the blank lines from the given array of tagged lines.
//     @param tagLines the array of tagged lines to be filtered
//     @return the filtered array of tagged lines with blank lines removed
//     @throws NullPointerException if the input array is null
//     */
//    private static String[] filterBlankLines(String[] tagLines) {
//        log.info("Filtering blank lines...");
//        try {
//            List<String> tagList = new ArrayList<>();
//            for (String value : tagLines) {
//                if (!value.startsWith("BlankLine")) {
//                    tagList.add(value);
//                }
//            }
//
//            log.info("Blank lines filtered successfully.");
//
//            return tagList.toArray(new String[0]);
//        }catch (NullPointerException e) {
//            log.error("tagLines array is null");
//            return null;
//        }
//    }
//}




package org.example.JobType.EmailHSD;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.example.JobType.EmailHSD.Variables.*;

public class TagData {

    private static final Logger log = Logger.getLogger(TagData.class);

    /**
     Tags the given data lines based on various criteria such as headers, signatures, blank lines, disclaimers, and body text.
     @param lines the array of data lines to be tagged
     @param disclaimerLines the list of lines that constitute the disclaimer
     @return a string array containing the tagged lines
     */
    public static String[] tagData(String[] lines, List<String> disclaimerLines) {
        log.info("Tagging data...");
        String[] tagLines = new String[lines.length];

        tagHeaders(lines, tagLines, disclaimerLines);
//        tagSignatures(lines, tagLines);
//        tagEmptyLines(lines, tagLines);
//        tagDisclaimer(lines, tagLines, disclaimerLines);
//        tagBody(lines, tagLines);

        log.info("Tagging data completed.");

        return filterBlankLines(tagLines);
    }

    /**
     Tags headers in the given lines array with a header prefix and line number, and stores the tagged lines in the
     tagLines array.
     @param lines the array of lines to be tagged
     @param tagLines the array in which the tagged lines will be stored
     @throws NullPointerException if either the lines array or the tagLines array is null
     @throws ArrayIndexOutOfBoundsException if the index of the current line being processed is out of bounds
     */
    private static void tagHeaders(String[] lines, String[] tagLines, List<String> disclaimerLines) {
        log.info("Tagging headers...");
        try {
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                Optional<Pattern> foundHeader = Regex.headerRegex().stream().filter(pattern -> pattern.matcher(line).find()).findFirst();
                if (foundHeader.isPresent()) {
                    String taggedLine = HEADER_PREFIX + line + SPLIT_SIGN + (i + 1);
                    tagLines[i] = taggedLine;
                    continue;
                }
                Optional<Pattern> foundSignature = Regex.signatureRegex().stream().filter(pattern -> pattern.matcher(line).find()).findFirst();
                if (foundSignature.isPresent()) {
                    String taggedLine = SIGNATURE_PREFIX + line + SPLIT_SIGN + (i + 1);
                    tagLines[i] = taggedLine;
                    continue;
                }
                if (tagLines[i] == null && disclaimerLines.contains(lines[i])) {
                    tagLines[i] = DISCLAIMER_PREFIX + lines[i] + SPLIT_SIGN + (i + 1);
                    continue;
                }
                Optional<String> blankLine = Optional.ofNullable(lines[i]);
                if (blankLine.map(String::trim).orElse("").isEmpty()) {
                    tagLines[i] = "BlankLine";
                    continue;
                }
                if (tagLines[i] == null) {
                    tagLines[i] = BODY_PREFIX + lines[i] + SPLIT_SIGN + (i + 1);
                }

            }
        } catch (NullPointerException e) {
            log.error("list or tagLines array is null");
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Array index is out of bound");
        }
    }


/*

    */
/**
     Tags the signatures in the given lines array with a signature prefix and line number and adds them to the tagLines array.
     @param lines the array of lines to search for signatures
     @param tagLines the array of tagged lines, where signatures will be tagged with a prefix indicating their type and line number
     @throws ArrayIndexOutOfBoundsException if the index of the current line being processed is out of bounds
     *//*

    private static void tagSignatures(String[] lines, String[] tagLines) {
        log.info("Tagging signatures...");
        try {

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                Optional<Pattern> matchingPattern = Regex.signatureRegex().stream().filter(pattern -> pattern.matcher(line).find()).findFirst();
                if (matchingPattern.isPresent()) {
                    String taggedLine = SIGNATURE_PREFIX + line + SPLIT_SIGN + (i + 1);
                    tagLines[i] = taggedLine;
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){
            log.error("Array is out of bound");
        }
    }

    */
/**
     Tags blank lines in the given array of lines and populates the tagLines array with the corresponding tags.
     @param lines the array of lines to be tagged
     @param tagLines the array to which the tags will be added
     *//*

    private static void tagEmptyLines(String[] lines, String[] tagLines) {
        log.debug("Tagging blank lines...");
        try {
            for (int j = 0; j < lines.length; j++) {
                Optional<String> line = Optional.ofNullable(lines[j]);
                if (line.map(String::trim).orElse("").isEmpty()) {
                    tagLines[j] = "BlankLine";
                }
            }
        } catch (NullPointerException e) {
            log.error("tagLines array is null");
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Array index is out of bound");
        }
    }

    */
/**
     Tags the lines in the given array that contain disclaimer text by adding a prefix and line number to them and
     storing them in the corresponding index of the tagLines array.
     @param lines the array of text lines to be searched for disclaimer text
     @param tagLines the array of tagged lines, where each tagged line is marked with a prefix indicating its type
     @param disclaimerLines the list of lines that contain disclaimer text
     @throws NullPointerException if the tagLines array is null
     @throws ArrayIndexOutOfBoundsException if the index of the current line being processed is out of bounds
     *//*

    private static void tagDisclaimer(String[] lines, String[] tagLines, List<String> disclaimerLines) {
        log.debug("Tagging disclaimer lines...");
        try {
            for (int j = 0; j < lines.length; j++) {
                if (tagLines[j] == null && disclaimerLines.contains(lines[j])) {
                    tagLines[j] = DISCLAIMER_PREFIX + lines[j] + SPLIT_SIGN + (j + 1);
                }
            }
        } catch (NullPointerException e) {
            log.error("tagLines array is null");
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Array index is out of bound");
        }
    }

    */
/**
     Tags the lines of the body with a prefix indicating that they are part of the body and a split sign followed by their index in the original array.
     @param lines the array of lines in the body
     @param tagLines the array of tagged lines to which the tagged body lines will be added
     @throws NullPointerException if the tagLines array is null
     @throws ArrayIndexOutOfBoundsException if the index of the current line being processed is out of bounds
     *//*

    private static void tagBody(String[] lines, String[] tagLines) {
        log.debug("Tagging body lines...");
        try {
            for (int j = 0; j < lines.length; j++) {
                if (tagLines[j] == null) {
                    tagLines[j] = BODY_PREFIX + lines[j] + SPLIT_SIGN + (j + 1);
                }
            }
        } catch (NullPointerException e) {
            log.error("tagLines array is null");
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Array index is out of bound");
        }
    }

    */
/**
     Filters out the blank lines from the given array of tagged lines.
     @param tagLines the array of tagged lines to be filtered
     @return the filtered array of tagged lines with blank lines removed
     @throws NullPointerException if the input array is null
     *//*

*/
    private static String[] filterBlankLines(String[] tagLines) {
        log.info("Filtering blank lines...");
        try {
            List<String> tagList = new ArrayList<>();
            for (String value : tagLines) {
                if (!value.startsWith("BlankLine")) {
                    tagList.add(value);
                }
            }

            log.info("Blank lines filtered successfully.");

            return tagList.toArray(new String[0]);
        }catch (NullPointerException e) {
            log.error("tagLines array is null");
            return null;
        }
    }
}
