package com.culture.code.assess;
import com.culture.code.assess.Culture.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.culture.code.assess.Culture.Type.RATINGQUESTION;
import static com.culture.code.assess.Culture.Type.SINGLESELECT;
import static org.junit.jupiter.api.Assertions.*;
public class CultureTest {

  @Test
  void parseSurvey() {
    List<Type> types = List.of(RATINGQUESTION, RATINGQUESTION);
    List<String> survey = List.of("theme,type,text",",ratingquestion", ",ratingquestion");
    List<Type> result = new Culture().parseSurvey(survey);
    assertArrayEquals(types.toArray(), result.toArray());
  }

  @Test
  void parseSurvey_empty() {
    List<Type> types = List.of();
    List<Type> result = new Culture().parseSurvey(List.of());
    assertArrayEquals(types.toArray(), result.toArray());
  }
  @Test
  void parseSurvey_invalidTypes() {
   assertThrows(IllegalArgumentException.class,()-> {
     List<String> survey = List.of("theme,type,text",",fquestion", ",fquestion");
     new Culture().parseSurvey(survey);
   });
  }
  @Test
  void parseSurvey_mixTypes() {
    List<Type> types = List.of(RATINGQUESTION, RATINGQUESTION, SINGLESELECT);
    List<String> survey = List.of("theme,type,text",",ratingquestion", ",ratingquestion", ",singleselect");
    List<Type> result = new Culture().parseSurvey(survey);
    assertArrayEquals(types.toArray(), result.toArray());
  }

  @Test
  void parseResponses() {
    List<String> survey = List.of("theme,type,text",",ratingquestion", ",ratingquestion", ",ratingquestion",",ratingquestion",",ratingquestion");
    List<String> responses = List.of("employee1@abc.xyz,1,2014-07-28T20:35:41+00:00,5,5,5,4,4", ",2,2014-07-29T07:05:41+00:00,4,5,5,3,3");
    Culture culture = new Culture();
    culture.calculateStatistics(survey, responses);
    assertEquals(culture.totalPartCount,2);
    assertEquals(culture.partPerc,100);
    assertArrayEquals(culture.avg,new float[]{4.5f,5, 5, 3.5f,3.5f});
  }

  @Test
  void processFiles_s1(){
    Culture.main(new String[]{"resources/survey-1.csv", "resources/survey-1-responses.csv"});
  }

  @Test
  void processFiles_s2(){
    Culture.main(new String[]{"resources/survey-2.csv", "resources/survey-2-responses.csv"});
  } @Test
  void processFiles_s3(){
    Culture.main(new String[]{"resources/survey-3.csv", "resources/survey-3-resonses.csv"});
  }
}
