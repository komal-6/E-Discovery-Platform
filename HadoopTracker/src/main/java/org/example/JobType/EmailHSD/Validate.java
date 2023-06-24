package org.example.JobType.EmailHSD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;
import org.example.JobType.EmailHSD.Variables;

public class Validate {

    private static final Logger log = Logger.getLogger(Validate.class);

    List<String> headerBlock = new ArrayList<>();
    List<String> signatureBlock = new ArrayList<>();
    List<String> DisclaimerBlock = new ArrayList<>();
    List<String> BodyBlock = new ArrayList<>();

    StringBuilder output = new StringBuilder();
    Text error = new Text("HSD not performed.");
    Text OutputString;

    /**
     * Parses an array of tagged HSD strings to validate and extract headers, signatures, body, and disclaimer
     *
     * @param taggedHSD the array of tagged HSD strings to parse
     * @return a Text object containing the parsed and validated output, or an error message if parsing fails
     */
    Text validHSD(String[] taggedHSD) {

            List<String> headers = new ArrayList<>();
            List<String> signatures = new ArrayList<>();
            List<String> body = new ArrayList<>();
            List<String> disclaimer = new ArrayList<>();
            headers.add("@");
            signatures.add("@");
            body.add("@");
            disclaimer.add("@");

            if (ValidateHeader(taggedHSD, headers) == 1) {
                return error;
            }
            if (addData(headers) == 1) {
                return error;
            }
            if (ValidateSignature(taggedHSD, signatures) == 1) {
                return error;
            }
            if (addData(signatures) == 1) {
                return error;
            }
            if (ValidateDisclaimer(taggedHSD, disclaimer) == 1) {
                return error;
            }
            if (addData(disclaimer) == 1) {
                return error;
            }
            if (ValidateBody(taggedHSD, body) == 1) {
                return error;
            }
            if (addData(body) == 1) {
                return error;
            }

            OutputString = new Text(String.valueOf(output));
            return OutputString;
        }

    /**
     * Validates the header of a tagged HSD array against a list of expected headers.
     *
     * @param taggedHSD An array of tagged HSD strings to be validated.
     * @param headers   A list of expected headers to be matched against the tagged HSD array.
     * @return An integer value indicating the success of the validation process. Returns 0 if successful, 1 if an
     * @throws ArrayIndexOutOfBoundsException occurs while processing the header.
     */
    private int ValidateHeader(String[] taggedHSD, List<String> headers) {
        AtomicInteger index = new AtomicInteger(0);
        try {
            Arrays.stream(taggedHSD).forEach(header -> {
                    int count = 0;
                    int count2 = 0;

                    if (index.get() == 0 && header.startsWith(Variables.HEADER_PREFIX)) {
                        count = processFirstHeader(taggedHSD);
                    }

                    if (index.get() != 0 && taggedHSD[index.get() - 1].startsWith(Variables.DISCLAIMER_PREFIX)) {
                        count2 = processHeader(taggedHSD, index.get() + 1);
                    }

                    if (count >= 1 || count2 >= 1) {
                        addBlock(headerBlock, headers);
                    }
                    index.incrementAndGet();
                });
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Error occurred while processing header: " + e.getMessage(), e);
            return 1;
        }
        return 0;
    }

    /**
     * Processes the first header block in the given taggedHSD array and adds the headers to the headerBlock list.
     *
     * @param taggedHSD the array of tagged headers, where each header is tagged with a prefix indicating its type
     * @return the number of headers in the first header block
     */
    private int processFirstHeader(String[] taggedHSD) {
        int count = 0;
        int j = 0, i = 0;
        while (j < taggedHSD.length - 1 && taggedHSD[j].startsWith(Variables.HEADER_PREFIX)) {
            j++;
        }
        if (taggedHSD[j].startsWith(Variables.BODY_PREFIX)) {
            count = (int) IntStream.range(i, j).takeWhile(index -> taggedHSD[index].startsWith(Variables.HEADER_PREFIX)).peek(index -> headerBlock.add(taggedHSD[index])).count();
        }
        return count;
    }

    /**
     * Processes the header block starting at the specified index in the given array of tagged HSD lines.
     * Adds the header lines to the headerBlock list and returns the number of header lines processed.
     *
     * @param taggedHSD the array of tagged HSD lines to process
     * @param i         the index at which to start processing the header block
     * @return the number of header lines processed
     */
    private int processHeader(String[] taggedHSD, int i) {
        int count = 0;
        if (taggedHSD[i - 1].startsWith(Variables.HEADER_PREFIX)) {
            int j = i - 1;
            while (j < taggedHSD.length - 1 && taggedHSD[j].startsWith(Variables.HEADER_PREFIX)) {
                j++;
            }
            if (taggedHSD[j].startsWith(Variables.BODY_PREFIX)) {
                count = (int) IntStream.range(i - 1, j).takeWhile(index -> taggedHSD[index].startsWith(Variables.HEADER_PREFIX)).peek(index -> headerBlock.add(taggedHSD[index])).count();
            }
        }
        return count;
    }

    /**
     * Validates the signatures in the given array of tagged HSDs and adds the validated signatures to the provided list.
     * A signature block is validated if it has at least two signatures.
     *
     * @param taggedHSD  the array of tagged HSDs, where each HSD is tagged with a prefix indicating its type
     * @param signatures the list of validated signatures
     * @return 0 if the validation is successful, otherwise 1
     */
    private int ValidateSignature(String[] taggedHSD, List<String> signatures) {
        AtomicInteger index = new AtomicInteger(0);
        try {
            Arrays.stream(taggedHSD).forEach(signature -> {
                int count = 0;
                int k = index.get() + 1;
                if (index.get() != 0 && taggedHSD[index.get() - 1].startsWith(Variables.BODY_PREFIX)) {
                    if (signature.startsWith(Variables.SIGNATURE_PREFIX)) {
                        int j = IntStream.range(k-1, taggedHSD.length).filter(i -> !taggedHSD[i].startsWith(Variables.SIGNATURE_PREFIX)).findFirst().orElse(taggedHSD.length);
                        if (taggedHSD[j].startsWith(Variables.DISCLAIMER_PREFIX)) {
                            count = processSignatureBlock(taggedHSD, index.get() + 1);
                        }
                    }
                }
                if (count >= 2) {
                    addBlock(signatureBlock, signatures);
                }
                index.incrementAndGet();
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Error occurred while processing signature at index " + index.get() + ": " + e.getMessage(), e);
            return 1;
        }
        return 0;
    }

    /**
     * Processes the signature block from the given index of the tagged HSD array and adds the signature
     * lines to the signatureBlock list.
     *
     * @param taggedHSD the tagged HSD array
     * @param k         the starting index of the signature block
     * @return the number of lines in the signature block
     */
    private int processSignatureBlock(String[] taggedHSD, int k) {
        AtomicInteger count= new AtomicInteger();
        IntStream.range(k - 1, taggedHSD.length).takeWhile(j -> taggedHSD[j].startsWith(Variables.SIGNATURE_PREFIX)).forEach(j -> {count.getAndIncrement();
                signatureBlock.add(taggedHSD[j]);
            });
        return count.get();
    }

    /**
     * Validates and processes the disclaimer blocks in the taggedHSD array and adds them to the disclaimer list.
     *
     * @param taggedHSD  an array of tagged text where each element represents a tagged word in the text
     * @param disclaimer a list to which the processed disclaimer blocks will be added
     * @return 0 if processing completed successfully, 1 if an error occurred
     */
    private int ValidateDisclaimer(String[] taggedHSD, List<String> disclaimer) {
        boolean errorOccurred = IntStream.range(1, taggedHSD.length + 1).mapToObj(i -> {
            try {
                validateDisclaimerBlock(taggedHSD, i);
                addBlock(DisclaimerBlock, disclaimer);
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error("Error occurred while processing disclaimer at index " + i + ": " + e.getMessage(), e);
                return 1;
            }
            return 0;
            })
        .anyMatch(i -> i == 1);
        return errorOccurred ? 1 : 0;
    }

    /**
     * Validates the disclaimer block in the given taggedHSD array at the specified index and adds it to the
     * DisclaimerBlock list if it is valid.
     *
     * @param taggedHSD the array of tagged headers and disclaimers
     * @param i         the index of the current header or disclaimer being processed
     */
    private void validateDisclaimerBlock(String[] taggedHSD, int i) {
        if (i != 1 && taggedHSD[i - 2].startsWith(Variables.SIGNATURE_PREFIX)) {
            int k = i;
            innerloop:
            if (taggedHSD[k - 1].startsWith(Variables.DISCLAIMER_PREFIX)) {
                int j = k - 1;
                while (j < taggedHSD.length - 1 && taggedHSD[j].startsWith(Variables.DISCLAIMER_PREFIX)) {
                    j++;
                }
                if (taggedHSD[j].startsWith(Variables.HEADER_PREFIX) || j == taggedHSD.length - 1) {
                    while (taggedHSD[k - 1].startsWith(Variables.DISCLAIMER_PREFIX)) {
                        DisclaimerBlock.add(taggedHSD[k - 1]);
                        if (k < taggedHSD.length) {
                            k++;
                            i++;
                        } else {
                            break innerloop;
                        }
                    }
                }
            }
        }
    }

    /**
     * Validates the body in the given taggedHSD array and adds it to the body list if it is valid.
     *
     * @param taggedHSD the array of tagged HSDs, where the body is tagged with a prefix indicating its type
     * @param body      the list of valid body lines to which valid body lines will be added
     * @return an integer indicating whether the body validation was successful or not (0 for success, 1 for failure)
     * @throws ArrayIndexOutOfBoundsException if the index of the current body line being processed is out of bounds
     */
    private int ValidateBody(String[] taggedHSD, List<String> body) {
        for (int i = 1; i <= taggedHSD.length; i++) {
            try {
                if (i != 1 && taggedHSD[i - 2].startsWith(Variables.HEADER_PREFIX)) {
                    if (taggedHSD[i - 1].startsWith(Variables.BODY_PREFIX)) {
                        ProcessBodyBlock(taggedHSD, i);
                    }
                }

                addBlock(BodyBlock, body);
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error("Error occurred while processing body at index " + i + ": " + e.getMessage(), e);
                return 1;
            }
        }
        return 0;
    }

    /**
     * Processes the body block in the given taggedHSD array starting at index k and adds the corresponding lines to the BodyBlock list.
     *
     * @param taggedHSD an array of Strings representing the tagged HSD
     * @param k         the index at which to start processing the body block
     */
    private void ProcessBodyBlock(String[] taggedHSD, int k) {
        if (taggedHSD[k - 1].startsWith(Variables.BODY_PREFIX)) {
            int j = k - 1;
            while (j < taggedHSD.length - 1 && taggedHSD[j].startsWith(Variables.BODY_PREFIX)) {
                j++;
            }
            if (taggedHSD[j].startsWith(Variables.SIGNATURE_PREFIX)) {
                BodyBlock.addAll(
                    IntStream.range(k - 1, j).filter(i -> taggedHSD[i].startsWith(Variables.BODY_PREFIX)).mapToObj(i -> taggedHSD[i]).collect(Collectors.toList())
                );
            }
        }
    }

    /**

     * Adds data to the output by iterating through the provided list of strings, which represents HSD data.
     * The method replaces carriage return characters and splits each string using the specified split sign.
     * If the resulting array has a length of 2, the second element is appended to the output. Otherwise, the first
     element is appended.
     * If an ArrayIndexOutOfBoundsException occurs during the operation, an error message is logged
     and the method returns 1. Otherwise, the method returns 0 upon successful completion.
     @param hsdList a list of strings representing HSD data
     @return 0 if the operation is successful; 1 if an ArrayIndexOutOfBoundsException occurs
     */
    private int addData(List<String> hsdList) {
        for (int i = 0; i < hsdList.size() - 1; i++) {
            try {
                String[] splits = hsdList.get(i).replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
//            append lines number with output
                output.append(splits.length == 2 ? splits[1] : splits[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error("Error occurred while storing lines in output at index " + i + ": " + e.getMessage(), e);
                return 1;
            }
        }
        return 0;
    }

    /**
     * Adds the contents of the provided block to the given list of HSDs.
     * If the block contains two or more lines, the first and last lines are added
     * to the list, along with appropriate separator and delimiter characters.
     * If the block contains only one line, it is added to the list with a delimiter
     * character.
     * The input block is cleared after it has been processed.
     * @param block the block of lines to add to the list of HSDs
     * @param listHSD the list of HSDs to add the block's contents to
     */
    private void addBlock(List<String> block, List<String> listHSD){
        if (block.size() >= 2) {
            listHSD.add(block.get(0));
            listHSD.add(Variables.RANGE_SEPARATOR);
            listHSD.add(block.get(block.size() - 1));
            listHSD.add(Variables.DELIMITER);
            block.clear();
        }

        if (block.size() == 1) {
            listHSD.add(block.get(0));
            listHSD.add(Variables.DELIMITER);
            block.clear();
        }
    }
}