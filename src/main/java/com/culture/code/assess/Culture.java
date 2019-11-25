package com.culture.code.assess;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Culture {
  public static final String DELIM = ",";
  float partPerc = 0.0f, totalPartCount = 0;
  float[] avg;
  private int[] ansCount;

  public static void main(String[] args) {
    System.out.println(args[0]);
    System.out.println(args[1]);
    if (args.length < 2) {
      System.out.println("Please provide survey and response file paths");
      System.exit(1);
    }
    Culture culture = new Culture();
    culture.calculateStatistics(getContent(args[0]), getContent(args[1]));
    System.out.println(culture);
  }


  private static List<String> getContent(String path) {
    try {
      return Files.readAllLines(Path.of(path));
    } catch (IOException e) {
      System.out.println("Error reading file");
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }


  List<Type> parseSurvey(List<String> survey) {
    List<Type> qTypes = new ArrayList<>();
    int i=0;
    if (!survey.isEmpty()){
      for (String s : survey.get(0).split(DELIM,-1)) {
        if (s.equalsIgnoreCase("type")){
          break;
        }
        i++;
      }
      //header skip
      boolean first = true;
      for (String l : survey)
      {
        if (first)
        {
          first = false;
          continue;
        }
        String[] split = l.split(DELIM, -1);
        qTypes.add(Type.valueOf(split[i].toUpperCase()));
      }
    }
    this.avg = new float[qTypes.size()];
    this.ansCount = new int[qTypes.size()];
    return qTypes;
  }


  private void parseResponses(List<String> responses, List<Type> questions) {
    for (String l : responses) {
      String[] response = l.split(DELIM,-1);
      if (response[2] != null && !response[2].isEmpty()) {
        int i = 0;
        totalPartCount++;
        while (i < questions.size()) { // && (3 + i) < response.length just incase response data is invalid and doesnt contain all answers
          if (questions.get(i).equals(Type.RATINGQUESTION) && !response[3 + i].isEmpty()) {
            avg[i] += Integer.parseInt(response[3 + i]);
            ansCount[i] += 1;
          }
          i++;
        }
      }
    }
  }


  public void calculateStatistics(List<String> survey, List<String> responses) {
    List<Type> questions = parseSurvey(survey);
    parseResponses(responses,questions);
    partPerc = (totalPartCount / responses.size()) * 100;
    for (int i = 0; i < avg.length; i++) {
      avg[i] /= ansCount[i];
    }
  }


  @Override
  public String toString() {
    return "Culture{"
        + "partPerc="
        + partPerc
        + ", totalPartCount="
        + totalPartCount
        + ", avg="
        + Arrays.toString(avg)
        + '}';
  }

  enum Type {
    RATINGQUESTION,
    SINGLESELECT;
  }
}
