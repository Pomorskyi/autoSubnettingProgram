package com.jetbrains;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {

    public static String convertStringToBinaryString(String input) {
        StringBuilder result = new StringBuilder();
        Arrays.stream(input.split("\\."))
                .map(num -> new BigInteger(num, 10).toString(2))
                .forEach(numBin -> {
                    int leng = numBin.length();
                    while (leng != 8) {
                        result.append("0");
                        leng++;
                    }
                    result.append(numBin);
                    result.append(".");
                });

        return result.substring(0, result.length() - 1);
    }

    public static String convertBinaryStringToString(String input) {
        StringBuilder res = new StringBuilder();

        Arrays.stream(input.split("\\.")).forEach(numBin -> {
            res.append(new BigInteger(numBin.trim(), 2).toString(10)).append(".");
        });

        return res.toString();
    }

    public static String binaryAnd(String s1, String s2) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) == '.') {
                res.append('.');
            } else {
                res.append((char) (((int) s1.charAt(i)) & ((int) s2.charAt(i))));
            }
        }

        return res.toString();
    }

    public static String maskDecToBin(int mask) {
        StringBuilder res = new StringBuilder();

        int[] maskAr = new int[4];
        for (int i = 0; i < maskAr.length; i++) {
            if (mask >= 8) {
                maskAr[i] = 8;
                mask -= 8;
            } else {
                maskAr[i] = mask;
                mask -= mask;
            }
        }

        Arrays.stream(maskAr).forEach(el -> {
            if (el == 8) {
                res.append("11111111").append(".");
            } else {
                StringBuilder numInBin = new StringBuilder();
                for (int i = 0; i < el; i++) {
                    numInBin.append("1");
                }
                int leng = numInBin.length();
                while (leng != 8) {
                    numInBin.append("0");
                    leng++;
                }
                res.append(numInBin).append(".");
            }
        });

        return res.substring(0, res.length() - 1);
    }

    public static String incrementNetworkAddress(String address, int pos) {
        address = address.replaceAll("\\.", "");
        pos = 32 - pos;
        pos--;
        StringBuilder res = new StringBuilder(address);
        boolean isIncremented = false;
        while (!isIncremented) {
            if (res.charAt(pos) == '0') {
                res.replace(pos, pos + 1, "1");

                for (int i = pos + 1; i < res.length(); i++) {
                    res.replace(i, i + 1, "0");
                }

                isIncremented = true;
            } else {
                pos--;
            }
        }
        res.insert(8, ".");
        res.insert(17, ".");
        res.insert(26, ".");
        res.insert(35, ".");
        return res.toString();
    }

    public static String getAdressRozgloszeniowy(String address, int pos) {
        address = address.replaceAll("\\.", "");
        StringBuilder res = new StringBuilder();
        pos = 32 - pos;
        int index = 0;
        for (int i = 0; i < address.length(); i++) {
            if (index < pos) {
                res.append(address.charAt(i));
            } else {
                break;
            }
            index++;
        }

        int length = res.length();
        while (length != 32) {
            length++;
            res.append("1");
        }

        res.insert(8, ".");
        res.insert(17, ".");
        res.insert(26, ".");
        res.insert(35, ".");
        return res.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input net: ");
        String inputNetDec = scanner.nextLine();
        String inputNetBin = convertStringToBinaryString(inputNetDec);

        System.out.print("Input mask (0 - 32): ");
        int maskDec = Integer.parseInt(scanner.nextLine());
        String maskBin = maskDecToBin(maskDec);
        final String[] networkAddress = new String[]{convertBinaryStringToString(binaryAnd(inputNetBin, maskBin))};
        System.out.println("Your network address: " + networkAddress[0]);

        System.out.print("Input first letter for ordering(eg. A->Z: A): ");
        Character ordering = scanner.nextLine().charAt(0);

        Map<Character, Integer> subing = new HashMap<>();
        System.out.print("Input your subs(e.g (Z,118), (L,5), (B,1)): ");
//        scanner.next();
        String subs = scanner.nextLine().trim();
        Arrays.stream(subs.split("\\(")).forEach(el -> {
            if (el.length() > 1) {
                StringBuilder sb = new StringBuilder(el);
                subing.put(el.charAt(0), Integer.parseInt(sb.substring(2, sb.indexOf(")"))));
            }
        });

        System.out.println("NAME\tPLACE\tMASKdec\tMASKbin\t\t\t\tNetworkAddress\t\tBroadcast");
        AtomicInteger sumOfAll = new AtomicInteger();
        networkAddress[0] = convertStringToBinaryString(networkAddress[0]);
        subing.entrySet().stream()
                .sorted((el1, el2) -> {
                    if (el2.getValue() > el1.getValue())
                        return 1;
                    else if (el2.getValue() < el1.getValue())
                        return -1;
                    else {
                        if (ordering == 'A')
                            return el1.getKey().compareTo(el2.getKey());
                        else
                            return el2.getKey().compareTo(el1.getKey());
                    }
                })
                .forEach(entr -> {
                    StringBuilder res = new StringBuilder();
                    int levelOfTwo = 0;
                    while (entr.getValue() + 2 > Math.pow(2, levelOfTwo)) {
                        levelOfTwo++;
                    }
                    sumOfAll.set(sumOfAll.get() + (int) Math.pow(2, levelOfTwo));

                    res.append(entr.getKey()).append("\t\t");
                    int rozmiarPodsieci = (int) Math.pow(2, levelOfTwo);
                    res.append(rozmiarPodsieci).append("\t\t");
                    int mask = 32 - levelOfTwo;
                    res.append("/").append(mask).append("\t\t");
                    String smallMaskInBin = maskDecToBin(mask);
                    String adresPodsieciBin = binaryAnd(networkAddress[0], smallMaskInBin);
                    res.append(convertBinaryStringToString(smallMaskInBin)).append("\t\t");
                    res.append(convertBinaryStringToString(adresPodsieciBin)).append("\t\t");
                    String adresRozgloszeniowyPodsieciBin = getAdressRozgloszeniowy(networkAddress[0], levelOfTwo);
                    res.append(convertBinaryStringToString(adresRozgloszeniowyPodsieciBin)).append("\t\t");

                    networkAddress[0] = incrementNetworkAddress(networkAddress[0], levelOfTwo);

                    System.out.println(res.toString());
                });

        System.out.println("\nSUM: " + sumOfAll.get());
    }
}
