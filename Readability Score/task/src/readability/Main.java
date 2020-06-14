package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder text = new StringBuilder();

        try (Scanner reader = new Scanner(new File(args[0]))) {
            text.append(reader.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] words = text.toString().replaceAll("[!.?]", "").split("\\s+");
        int sentenceCount = text.toString().split("[!.?]").length;
        int wordCount = words.length;
        int characterCount = text.toString().replaceAll("\\s", "").length();
        int syllableCount = 0;
        int polysyllableCount = 0;

        String[] wordsString = text.toString().split("\\s+");

        for (int i = 0; i < wordCount; i++) {
            words[i] = words[i].replaceAll("\\W", "");
            String modifiedWord = words[i].replaceAll("[aeiouyAEIOUY]+[aeiouyAEIOUY]", "a");

            if (modifiedWord.charAt(modifiedWord.length() - 1) == 'e') {
                modifiedWord = modifiedWord.substring(0, modifiedWord.length() - 1);
            }

            int currentSyllables = modifiedWord.replaceAll("[^aeiouyAEIOUY]", "").length();

            if (currentSyllables > 2) {
                polysyllableCount++;
            }

            syllableCount += currentSyllables;
        }

        System.out.println(text.toString() + "\n");
        System.out.printf("Words: %d%n"
                + "Sentences: %d%n"
                + "Characters: %d%n"
                + "Syllables: %d%n"
                + "Polysyllables: %d%n",
                wordCount, sentenceCount, characterCount, syllableCount, polysyllableCount);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");

        String chosenRm = scanner.nextLine();
        System.out.println();
        ReadabilityMeasurement rm = new ReadabilityMeasurement(characterCount, syllableCount,
                polysyllableCount, wordCount, sentenceCount);
        rm.calculate(chosenRm);
    }
}

class ReadabilityMeasurement {

    private int characters;
    private int syllables;
    private int polysyllables;
    private int words;
    private int sentences;

    ReadabilityMeasurement(int characters, int syllables, int polysyllables, int words, int sentences) {
        this.characters = characters;
        this.syllables = syllables;
        this.polysyllables = polysyllables;
        this.words = words;
        this.sentences = sentences;
    }

    void calculate(String measurement) {
        switch (measurement) {
            case "ARI":
                automatedReadabilityIndex();
                break;
            case "FK":
                fleschKincaidReadabilityIndex();
                break;
            case "SMOG":
                smogIndex();
                break;
            case "CL":
                colemanLiauIndex();
                break;
            case "all":
                allIndex();
                break;
        }
    }
    int recommendAge(double index) {
        int indexCeil = (int) Math.ceil(index);
        if (indexCeil > 0 && indexCeil <= 2) {
            return indexCeil + 5;
        } else if (indexCeil >= 3 && indexCeil <= 12) {
            return indexCeil + 6;
        } else if (indexCeil == 13) {
            return 24;
        } else {
            return 25;
        }
    }

    int automatedReadabilityIndex() {
        double index = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
        int age = recommendAge(index);
        System.out.printf("Automated Readability Index: %.2f (about %d year olds).",
                index, age);
        return age;
    }

    int fleschKincaidReadabilityIndex() {
        double index = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
        int age = recommendAge(index);
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).",
                index, age);
        return age;
    }

    int smogIndex() {
        double index = 1.043 * Math.sqrt((double) polysyllables * 30 / sentences) + 3.1291;
        int age = recommendAge(index);
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).",
                index, age);
        return age;
    }

    int colemanLiauIndex() {
        double index = 0.0588 * (double) characters / words * 100 - 0.296 * sentences / words * 100 - 15.8;
        int age = recommendAge(index);
        System.out.printf("Coleman–Liau index: %.2f (about %d year olds).",
                index, age);
        return age;
    }

    void allIndex() {
        int ariAge = automatedReadabilityIndex();
        System.out.println();
        int fkAge = fleschKincaidReadabilityIndex();
        System.out.println();
        int smogAge = smogIndex();
        System.out.println();
        int clAge = colemanLiauIndex();
        System.out.println();
        System.out.printf("This text should be understood in average by %.2f year olds.",
                1.0 * (ariAge + fkAge + smogAge + clAge) / 4);
    }
}