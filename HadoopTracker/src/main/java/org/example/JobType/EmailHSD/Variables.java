package org.example.JobType.EmailHSD;

/**
 The Variables class defines a set of constant values used throughout the application.
 These constants include the minimum paragraph length, prefixes for different types of text,
 a delimiter used to separate values, and a split sign used to split strings.
 The class is not meant to be instantiated, so all constants are declared as public static final.
 */
public class Variables {

    public static final int MIN_PARAGRAPH_LENGTH = 150;
    public static final String HEADER_PREFIX = "@Header ";
    public static final String SIGNATURE_PREFIX = "@Signature ";
    public static final String BODY_PREFIX = "@Body ";
    public static final String DISCLAIMER_PREFIX = "@Disclaimer ";
    public static final String DELIMITER = ",";
    public static final String RANGE_SEPARATOR = "-";
    public static final String SPLIT_SIGN = "<==>";
}
