import java.util.*;
import java.io.*;

// GAGGCTCCATGTCTAGTGGCCTAGAATTTGTATCCCACAATCCCCCAAGTGAAGCAGCTGCTCCTCCAGTGGCAAGGACCAGTCCTGCAGGGGGAACGTGGTCCTCAGTGGTCAGTGGGG
public class DNAStrandReader{
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.print("Enter your DNA sequence: ");
        String choice = console.nextLine();
        System.out.print("How many sequences would you like? ");
        int sequence = console.nextInt();
        System.out.print("How many bases would you like in each sequence? ");
        int bases = console.nextInt();
        System.out.println();
        System.out.println("This is your new DNA sequence: " + revComp(choice));
        System.out.println();
        normalSeq(choice, sequence, bases);
        reverseSeq(choice, sequence, bases);
        System.out.println();
        System.out.print("What barcode would you like to add to normal? ");
        String barcode = console.next();
        System.out.print("What barcode would you like to add to reverse? ");
        String reverseBarcode = console.next();
        System.out.println();
        normalBar(choice, sequence, bases, barcode);
        reverseBar(choice, sequence, bases, reverseBarcode);

        System.out.println();
        System.out.print("Do you want to save this to a file? ");
        if (console.next().equalsIgnoreCase("Yes")) {
            saveFile(console, choice, sequence, bases, barcode, reverseBarcode);
        } else {
            System.exit(0);
        }
    }

    public static void normalSeq(String choice, int sequence, int bases) {
        List<List<String>> list = allGuides(choice, sequence, bases);
        System.out.println("These are your " + sequence + " sequences:");
        int i = 0;
        for (List<String> normal : list) {
            for (String normalSeq : normal) {
                System.out.println("Sequence " + (i + 1) + ": " + normalSeq);
            }
            i++;
        }
        System.out.println();
    }

    public static void reverseSeq(String choice, int sequence, int bases) {
        List<List<String>> reverseList = reverseAllGuides(choice, sequence, bases);
        System.out.println("These are your " + sequence + " reversed sequences:");
        int j = 0;
        for (List<String> reverse : reverseList) {
            for (String reverseSeq : reverse) {
                System.out.println("Reverse Sequence " + (j + 1) + ": " + reverseSeq);
            }
            j++;
        }
    }
    public static void normalBar(String choice, int sequence, int bases, String barcode) {
        List<List<String>> list = addBarcode(choice, sequence, bases, barcode);
        System.out.println("These are your " + sequence + " sequences with barcodes:");
        int i = 0;
        for (List<String> normal : list) {
            for (String normalSeq : normal) {
                System.out.println("Sequence " + (i + 1) + ": " + normalSeq);
            }
            i++;
        }
        System.out.println();
    }
    public static void reverseBar(String choice, int sequence, int bases, String reverseBarcode) {
        List<List<String>> reverseList = addRevBarcode(choice, sequence, bases, reverseBarcode);
        System.out.println("These are your reversed " + sequence + " sequences with barcodes:");
        int j = 0;
        for (List<String> reverse : reverseList) {
            for (String reverseSeq : reverse) {
                System.out.println("Reverse Sequence " + (j + 1) + ": " + reverseSeq);
            }
            j++;
        }
    }

    public static String revComp(String choice) {
        List<Character> dnaList = new ArrayList<>();

        for (int i = 0; i < choice.length(); i++) {
            char c = choice.charAt(i);
            if (c == 'A' || c == 'a') {
                dnaList.add('T');
            } else if (c == 'T' || c == 't') {
                dnaList.add('A');
            } else if (c == 'G' || c == 'g') {
                dnaList.add('C');
            } else if (c == 'C' || c == 'c') {
                dnaList.add('G');
            } else {
                throw new IllegalArgumentException("Your sequence contains unknown sequence");
            }
        }

        // Reverse the list
        for (int i = 0; i < dnaList.size() / 2; i++) {
            char temp = dnaList.get(i);
            dnaList.set(i, dnaList.get(dnaList.size() - i - 1));
            dnaList.set(dnaList.size() - i - 1, temp);
        }

        // Convert list to string
        StringBuilder finalDNA = new StringBuilder();
        for (char c : dnaList) {
            finalDNA.append(c);
        }

        return finalDNA.toString();
    }

    public static List<List<String>> allGuides(String choice, int sequence, int bases) {
        String reversed = revComp(choice);
        List<List<String>> dnaList = new ArrayList<>();

        for (int i = 0; i < sequence; i++) {
            List<String> sublist = new ArrayList<>();
            sublist.add(reversed.substring(i, bases + i));
            dnaList.add(sublist);
        }
        return dnaList;
    }

    public static List<List<String>> reverseAllGuides(String choice, int sequence, int bases) {
        List<List<String>> dnaList = allGuides(choice, sequence, bases);
        List<List<String>> finalList = new ArrayList<>();

        for (List<String> sublist : dnaList) {
            List<String> transformedSublist = new ArrayList<>();
            for (String str : sublist) {
                StringBuilder transformedString = new StringBuilder();
                for (char c : str.toCharArray()) {
                    if (c == 'A') {
                        transformedString.append('T');
                    } else if (c == 'T') {
                        transformedString.append('A');
                    } else if (c == 'C') {
                        transformedString.append('G');
                    } else if (c == 'G') {
                        transformedString.append('C');
                    } else {
                        throw new IllegalArgumentException("Your sequence contains unknown."); // For any other characters, keep them as they are
                    }
                }
                transformedSublist.add(transformedString.reverse().toString()); // Reverse the string
            }
            finalList.add(transformedSublist);
        }
        return finalList;
    }

    public static List<List<String>> addBarcode(String choice, int sequence, int bases, String barcode) {
        List<List<String>> outerList = allGuides(choice, sequence, bases);
        for (List<String> innerList : outerList) {
            for (int i = 0; i < innerList.size(); i++) {
                String originalString = innerList.get(i);
                String modifiedString = barcode + originalString;
                innerList.set(i, modifiedString);
            }
        }
        return outerList;
    }

    public static List<List<String>> addRevBarcode(String choice, int sequence, int bases, String reverseBarcode) {
        List<List<String>> reverseSeq = reverseAllGuides(choice, sequence, bases);
        for (List<String> innerList : reverseSeq) {
            for (int i = 0; i < innerList.size(); i++) {
                String originalString = innerList.get(i);
                String modifiedString = reverseBarcode + originalString;
                innerList.set(i, modifiedString);
            }
        }
        return reverseSeq;
    }

    public static void saveFile(Scanner console, String choice, int sequence, int bases, String barcode, String reverseBarcode) throws FileNotFoundException {
        System.out.print("What is the name of the file? ");
        String userFile = console.next();
        File outputFile = new File(userFile);
        PrintStream output = new PrintStream(outputFile);

        int i = 0;
        String reverseDNA = revComp(choice);
        List<List<String>> normalSeq = allGuides(choice, sequence, bases);
        List<List<String>> reverseSeq = reverseAllGuides(choice,sequence, bases);
        List<List<String>> normalBar = addBarcode(choice, sequence, bases, barcode);
        List<List<String>> reverseBar = addRevBarcode(choice, sequence, bases, reverseBarcode);


        output.println("This is your reversed DNA: " + reverseDNA);

        for (List<String> normal : normalSeq) {
            for (String seq : normal) {
                output.println("Sequence " + (i + 1) + ": " + seq);
            }
            i++;
        }
        i = 0;

        output.println();

        for (List<String> reverse : reverseSeq) {
            for (String revSeq : reverse) {
                output.println("Reverse Sequence " + (i + 1) + ": " + revSeq);
            }
            i++;
        }
        i = 0;

        output.println();

        for (List<String> normalCode : normalBar) {
            for (String code : normalCode) {
                output.println("Sequence with added barcodes: " + (i + 1) + ": " + code);
            }
            i++;
        }
        i = 0;

        output.println();

        for (List<String> reverseCode : reverseBar) {
            for (String revCode : reverseCode) {
                output.println("Reverse sequence with added barcodes: " + (i + 1) + ": " + revCode);
            }
            i++;
        }
    }
}