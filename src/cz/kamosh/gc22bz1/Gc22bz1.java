package cz.kamosh.gc22bz1;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Gc22bz1 {

    private static final String EXPECTED_RESULT = "6c758f3ebffd3e6d46f1f964ca8fd02e1135d2f33597aaa1ed940201d095d0a9";
    private static final char DEGREE = '\u00b0';
    private static final String COORDINATES = "N 50° 0A.BCD E 014° 2E.FGH";
    private final StringBuilder sb = new StringBuilder(COORDINATES);

    private int allPosibilities;
    private int c = 0;

    private HashFunction hashFunction;
    private MessageDigest messageDigest;

    public static void main(String[] args) {
        new Gc22bz1().generateAndCheck();
    }

    private Gc22bz1() {
        init();
    }

    private void init() {
        hashFunction = Hashing.sha256();
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Gc22bz1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private PositionAndNumbers[] createPositionAndNumbers() {
        PositionAndNumbers A = new PositionAndNumbers(COORDINATES.indexOf('A'), createArrayOfNumbers(0, 2));
        PositionAndNumbers B = new PositionAndNumbers(COORDINATES.indexOf('B'), createArrayOfNumbers(0, 9));
        PositionAndNumbers C = new PositionAndNumbers(COORDINATES.indexOf('C'), createArrayOfNumbers(0, 9));
        PositionAndNumbers D = new PositionAndNumbers(COORDINATES.indexOf('D'), createArrayOfNumbers(0, 9));
        PositionAndNumbers E = new PositionAndNumbers(COORDINATES.lastIndexOf('E'), createArrayOfNumbers(8, 9));
        PositionAndNumbers F = new PositionAndNumbers(COORDINATES.indexOf('F'), createArrayOfNumbers(0, 9));
        PositionAndNumbers G = new PositionAndNumbers(COORDINATES.indexOf('G'), createArrayOfNumbers(0, 9));
        PositionAndNumbers H = new PositionAndNumbers(COORDINATES.indexOf('H'), createArrayOfNumbers(0, 9));
        PositionAndNumbers[] positionsAndNumbers = new PositionAndNumbers[]{A, B, C, D, E, F, G, H};
        return positionsAndNumbers;
    }

    private void generateAndCheck() {
        PositionAndNumbers[] positionsAndNumbers = createPositionAndNumbers();

        allPosibilities = calculateNumberOfPossibilities(positionsAndNumbers) * getCharsets().length;
        System.out.println(allPosibilities);
        generateAndCheck(sb, positionsAndNumbers, 0);
    }

    private void generateAndCheck(StringBuilder sb, PositionAndNumbers[] positionAndNumberses, int pos) {
        if (pos < positionAndNumberses.length) {
            PositionAndNumbers pan = positionAndNumberses[pos];
            for (String replacingNumber : pan.numbers) {
                sb.replace(pan.position, pan.position + 1, replacingNumber);
                generateAndCheck(sb, positionAndNumberses, pos + 1);
            }
        } else {
            String toCheckCoordinates = sb.toString();

//            System.out.println(toCheckCoordinates);
            for (Charset charset : getCharsets()) { //
                if (++c % 10000 == 0) {
                    System.out.printf("%.2f%% \n", ((double) c / allPosibilities * 100));
                }

                if (check(toCheckCoordinates, charset)) {
                    System.out.println(toCheckCoordinates);
                    System.out.println(charset.name());
                    System.exit(0);
                }
            }
        }
    }

    private boolean check(String text, Charset charset) {
        HashCode hashCode = hashFunction.hashString(text, charset);
        String hashFunctionActual = hashCode.toString();
//        messageDigest.reset();
//        messageDigest.update(getBytes(text, charset));
//        byte[] messageDigestBytes = messageDigest.digest();
//        String messageDigestActual = getHex(messageDigestBytes);
//      
//        if(!hashFunctionActual.equals(messageDigestActual)) {
//            throw new RuntimeException(hashFunctionActual + ":" + messageDigestActual);
//        }
//        System.out.println(hashFunctionActual);
//        System.out.println(messageDigestActual);
//        System.out.println("-------");

//        System.out.println(charset.name());
//        System.out.println(hashFunctionActual);
//        System.out.println(EXPECTED_RESULT);
//        System.out.println("---");
        return EXPECTED_RESULT.equals(hashFunctionActual);
    }    
    
    private Charset[] charsets = null;
    private Charset[] getCharsets() {
        if (charsets == null) {
            charsets = new Charset[]{
                Charsets.ISO_8859_1,
                Charsets.US_ASCII,
                Charsets.UTF_16,
                Charsets.UTF_16BE,
                Charsets.UTF_16LE,
                Charsets.UTF_8,    
            };
        }
        return charsets;
    }    

    private static int calculateNumberOfPossibilities(PositionAndNumbers[] positionAndNumberses) {
        int res = 1;
        for (PositionAndNumbers positionAndNumbers : positionAndNumberses) {
            res *= positionAndNumbers.numbers.length;
        }
        return res;
    }

    private static class PositionAndNumbers {
        int position;
        String[] numbers;

        public PositionAndNumbers(int position, String[] numbers) {
            this.position = position;
            this.numbers = numbers;
        }

        @Override
        public String toString() {
            return "{position=" + position + ", numbers=" + Arrays.toString(numbers) + '}';
        }
    }

    private static String[] createArrayOfNumbers(int start, int end) {
        if (start > end) {
            throw new UnsupportedOperationException("Start cannot be bigger that end");
        }

        String[] res = new String[end - start + 1];
        int counter = 0;
        for (int i = start; i <= end; i++) {
            res[counter++] = "" + i;
        }
        return res;
    }

    private static final String HEXES = "0123456789abcdef";
    private static String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

        private byte[] getBytes(String text, Charset charset) {
        return text.getBytes(charset);
    }

}