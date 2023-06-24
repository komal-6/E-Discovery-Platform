package org.example.JobType.EmailHSD;

import org.apache.hadoop.io.Text;
import org.example.JobType.EmailHSD.Variables;

public class DemoValidate {
    static StringBuilder headerout = new StringBuilder();
    static StringBuilder signaureout = new StringBuilder();
    static StringBuilder disclaimerout = new StringBuilder();
    static StringBuilder bodyout = new StringBuilder();
    static StringBuilder HSDout = new StringBuilder();

    static Text demoValidate(String[] taggedHSD) {

        Text OutputString = null;
        try {
            headerout.setLength(0);
            signaureout.setLength(0);
            disclaimerout.setLength(0);
            bodyout.setLength(0);
            HSDout.setLength(0);
            int j = 0;
            headerout.append("@");
            signaureout.append("@");
            disclaimerout.append("@");
            bodyout.append("@");

            for (int i = 0; i < taggedHSD.length; ) {
                j = i;
                if (taggedHSD[i].startsWith(Variables.HEADER_PREFIX)) {
                    while (taggedHSD[j].startsWith(Variables.HEADER_PREFIX)) {
                        j++;
                    }
                    if (i == 0 && taggedHSD[j].startsWith(Variables.BODY_PREFIX)) {
                        String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                        headerout.append(splits.length == 2 ? splits[1] : splits[0]);
                        headerout.append("-");
                        splits = taggedHSD[j - 1].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                        headerout.append(splits.length == 2 ? splits[1] : splits[0]);
                        headerout.append(",");
                    }
                    if (i != 0 && taggedHSD[j].startsWith(Variables.BODY_PREFIX) && taggedHSD[i - 1].startsWith(Variables.DISCLAIMER_PREFIX)) {
                        String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                        headerout.append(splits.length == 2 ? splits[1] : splits[0]);
                        headerout.append("-");
                        splits = taggedHSD[j - 1].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                        headerout.append(splits.length == 2 ? splits[1] : splits[0]);
                        headerout.append(",");
                    }
                }

                if (taggedHSD[i].startsWith(Variables.SIGNATURE_PREFIX)) {
                    while (taggedHSD[j].startsWith(Variables.SIGNATURE_PREFIX)) {
                        j++;
                    }
                    if (i != 0 && taggedHSD[j].startsWith(Variables.DISCLAIMER_PREFIX) && taggedHSD[i - 1].startsWith(Variables.BODY_PREFIX)) {
                        String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                        signaureout.append(splits[1]);
                        signaureout.append("-");
                        splits = taggedHSD[j - 1].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                        signaureout.append(splits[1]);
                        signaureout.append(",");
                    }
                }

                if (taggedHSD[i].startsWith(Variables.DISCLAIMER_PREFIX)) {
                    while (taggedHSD[j].startsWith(Variables.DISCLAIMER_PREFIX)) {
                        if (j < taggedHSD.length - 1) {
                            j++;
                        } else {
                            break;
                        }
                    }
                    if (i != 0 && (taggedHSD[j].startsWith(Variables.HEADER_PREFIX) || j == (taggedHSD.length - 1)) && taggedHSD[i - 1].startsWith(Variables.SIGNATURE_PREFIX)) {

                        if (j == (taggedHSD.length - 1)) {
                            if (i == j) {
                                String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                                disclaimerout.append(splits.length == 2 ? splits[1] : splits[0]);
                                disclaimerout.append(",");
                            } else {
                                String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                                disclaimerout.append(splits.length == 2 ? splits[1] : splits[0]);
                                disclaimerout.append("-");
                                splits = taggedHSD[j].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                                disclaimerout.append(splits.length == 2 ? splits[1] : splits[0]);
                                disclaimerout.append(",");
                            }
                        } else {
                            if (i == (j - 1)) {
                                String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                                disclaimerout.append(splits.length == 2 ? splits[1] : splits[0]);
                                disclaimerout.append(",");
                            } else {
                                String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                                disclaimerout.append(splits.length == 2 ? splits[1] : splits[0]);
                                disclaimerout.append("-");
                                splits = taggedHSD[j - 1].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                                disclaimerout.append(splits.length == 2 ? splits[1] : splits[0]);
                                disclaimerout.append(",");
                            }
                        }
                    }
                }

                if (taggedHSD[i].startsWith(Variables.BODY_PREFIX)) {
                    while (taggedHSD[j].startsWith(Variables.BODY_PREFIX)) {
                        j++;
                    }
                    if (i != 0 && taggedHSD[j].startsWith(Variables.SIGNATURE_PREFIX) && taggedHSD[i - 1].startsWith(Variables.HEADER_PREFIX)) {
                        if (i == (j - 1)) {
                            String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                            bodyout.append(splits.length == 2 ? splits[1] : splits[0]);
                            bodyout.append(",");
                        } else {
                            String[] splits = taggedHSD[i].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                            bodyout.append(splits.length == 2 ? splits[1] : splits[0]);
                            bodyout.append("-");
                            splits = taggedHSD[j - 1].replaceAll("\\r", "").split(Variables.SPLIT_SIGN);
                            bodyout.append(splits.length == 2 ? splits[1] : splits[0]);
                            bodyout.append(",");
                        }
                    }
                }

                if ((j - i) == 0) {
                    i++;
                } else {
                    i += (j - i);
                }
            }

            HSDout.append(headerout.deleteCharAt(headerout.length() - 1)).append(signaureout.deleteCharAt(signaureout.length() - 1)).append(disclaimerout.deleteCharAt(disclaimerout.length() - 1)).append(bodyout.deleteCharAt(bodyout.length() - 1));
            System.out.println(HSDout);
            OutputString = new Text(String.valueOf(HSDout));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return OutputString;
    }
}
