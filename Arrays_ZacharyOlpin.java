
package arrays_zachary.olpin;

import java.io.File;
import java.util.*;

public class Arrays_ZacharyOlpin
{

   public static void main(String[] args) throws Exception
    {
        Scanner reader;
        int numRows;
        double[] gpas;
        String[] ids, splitLine;

        reader = new Scanner(new File("studentdata.txt"));
        numRows = 0;
        while (reader.hasNextLine())
        {
            numRows++;
            reader.nextLine();
        }
        reader.close();

        ids = new String[numRows];
        gpas = new double[numRows];
        reader = new Scanner(new File("studentdata.txt"));
        for (int i = 0; reader.hasNextLine(); i++)
        {
            splitLine = reader.nextLine().split("\\s\\s");
            ids[i] = splitLine[0];
            gpas[i] = Double.parseDouble(splitLine[1]);
        }
        reader.close();

        printStudents(ids, gpas);
        printHistogram(gpas);
    }

    private static void printHistogram(double[] gpas)
    {
        int[] gpaBracketTotals;
        double totalStudents;

        gpaBracketTotals = new int[8];
        for (Double g : gpas) {
            if (g < 0.5) gpaBracketTotals[0] += 1;
            else if (g >= 0.5 && g < 1.0) gpaBracketTotals[1] += 1;
            else if (g >= 1.0 && g < 1.5) gpaBracketTotals[2] += 1;
            else if (g >= 1.5 && g < 2.0) gpaBracketTotals[3] += 1;
            else if (g >= 2.0 && g < 2.5) gpaBracketTotals[4] += 1;
            else if (g >= 2.5 && g < 3.0) gpaBracketTotals[5] += 1;
            else if (g >= 3.0 && g < 3.5) gpaBracketTotals[6] += 1;
            else if (g >= 3.5) gpaBracketTotals[7] += 1;
        }

        totalStudents = Arrays.stream(gpaBracketTotals).sum();

        System.out.println( "\nStudent GPA distribution by octile\n"
                          + "----------------------------------------");
        for (int i = 0; i < gpaBracketTotals.length; i++)
        {
            System.out.printf( "%.2f - %.2f: %s [%d]\n"
                             , i * 0.5, i * 0.5 + 0.5
                             , "||".repeat((int) Math.floor((gpaBracketTotals[i] / totalStudents) * 100))
                             , gpaBracketTotals[i]);
        }
    }

    private static void printStudents(String[] ids, double[] gpas)
    {
        HashMap<String, Double> students;
        double[] sortedGpas;
        int[] ranks;
        double lowest;
        int currentRank, ties;
        HashMap<Double, Integer> gpaCounts;

        students = new HashMap<>(ids.length);
        sortedGpas = gpas.clone();
        Arrays.sort(sortedGpas);
        ranks = new int[ids.length];
        lowest = 4.0;
        currentRank = ids.length;
        for (int i = ids.length - 1; i >= 0; i--)
        {
            if (sortedGpas[i] < lowest)
            {
                lowest = sortedGpas[i];
                currentRank = i;
            }
            ranks[i] = ids.length - 1 - currentRank;
            students.put(ids[i], gpas[i]);
        }

        gpaCounts = new HashMap<>(ranks.length);
        for (Double gpa: sortedGpas)
        {
                gpaCounts.put(gpa, gpaCounts.getOrDefault(gpa, 0) + 1);
        }

        for (int i = 0; i < ids.length; i++)
        {
            ties = gpaCounts.get(students.get(ids[i])) - 1;

            if (ties > 0)
            {
                System.out.printf( "\nStudent: %s\nRank: %s%d (with %d others)\nGPA: %.2f\n"
                                 , ids[i]
                                 , "T"
                                 , ranks[Arrays.binarySearch(sortedGpas, students.get(ids[i]))]
                                 , ties
                                 , students.get(ids[i]));
            }
            else
            {
                System.out.printf( "\nStudent: %s\nRank: %d\nGPA: %.2f\n"
                                 , ids[i]
                                 , ranks[Arrays.binarySearch(sortedGpas, students.get(ids[i]))]
                                 , students.get(ids[i]));
            }
        }
    }
    
}
