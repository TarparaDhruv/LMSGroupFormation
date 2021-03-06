package CSCI5308.GroupFormationTool.SurveyManagementTest;

import CSCI5308.GroupFormationTool.Question.Question;
import CSCI5308.GroupFormationTool.Question.QuestionOption;
import CSCI5308.GroupFormationTool.SurveyManagement.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SurveyServiceTest {

    ISurveyPersistence surveyPersistence;
    ISurveyService surveyService;

    @BeforeEach
    void initiateObjects() {
        surveyPersistence = Mockito.mock(SurveyPersistence.class);
        surveyService = SurveyFactory.instance().createService();
    }


    @Test
    public void isSurveyPublishedTest() {
        long surveyId = 1L;
        Mockito.when(surveyPersistence.getSurveyStatus(surveyId)).thenReturn(1);
        assertTrue(surveyService.isSurveyPublished(surveyId, surveyPersistence));
        assertFalse(surveyService.isSurveyPublished(surveyId + 1, surveyPersistence));
    }

    @Test
    public void getAllSurveyQuestionsTest() throws SQLException {
        long courseId = 1L;
        long surveyId = 1L;
        Mockito.when(surveyPersistence.getSurveyIdUsingCourseId(courseId)).thenReturn(surveyId);
        Mockito.when(surveyPersistence.getSurveyStatus(surveyId)).thenReturn(0);
        Mockito.when(surveyPersistence.getAllSurveyQuestions(1L)).thenReturn(new ArrayList<Question>() {
        });
        Map<String, Object> response = new HashMap<>();
        response.put("surveyId", surveyId);
        response.put("status", false);
        response.put("questions", new ArrayList<Question>() {
        });
        assertEquals(surveyService.getAllSurveyQuestions(courseId, surveyPersistence), response);
    }

    @Test
    public void addQuestionPageTest() throws SQLException {
        long courseId = 1;
        long surveyId = 1;
        Mockito.when(surveyPersistence.getAllInstructorQuestionsUsingCourseId(courseId, surveyId)).thenReturn(new ArrayList<>());
        Mockito.when(surveyPersistence.getAllSurveyQuestions(surveyId)).thenReturn(new ArrayList<Question>() {
        });
        Map<String, Object> response = new HashMap<>();
        response.put("addedQuestion", new ArrayList<Question>());
        response.put("availableQuestions", new ArrayList<Question>());
        assertEquals(surveyService.addQuestionPage(courseId, surveyId, surveyPersistence), response);
    }

    @Test
    public void addQuestionToSurveyTest() {
        long questionId = 1;
        long surveyId = 1;
        Mockito.when(surveyPersistence.addQuestionToSurvey(surveyId, questionId)).thenReturn(true);
        Mockito.when(surveyPersistence.getSurveyStatus(surveyId)).thenReturn(1);
        surveyService.addQuestionToSurvey(surveyId, questionId, surveyPersistence);
    }

    @Test
    public void deleteQuestionFromSurveyTest() {
        long questionId = 1;
        long surveyId = 1;
        Mockito.when(surveyPersistence.deleteQuestionFromSurvey(surveyId, questionId)).thenReturn(true);
        Mockito.when(surveyPersistence.getSurveyStatus(surveyId)).thenReturn(1);
        surveyService.deleteQuestionFromSurvey(surveyId, questionId, surveyPersistence);
    }

    @Test
    public void publishSurveyTest() {
        long surveyId = 1;
        Mockito.when(surveyPersistence.publishSurvey(surveyId)).thenReturn(true);
        assertTrue(surveyService.publishSurvey(surveyId, surveyPersistence));
    }

    @Test
    public void unpublishSurveyTest() {
        long surveyId = 1;
        Mockito.when(surveyPersistence.unpublishSurvey(surveyId)).thenReturn(true);
        assertTrue(surveyService.unpublishSurvey(surveyId, surveyPersistence));
    }

    @Test
    public void displaySurveyQuestionsToStudentsTest() throws SQLException {
        long courseId = 1;
        long surveyId = 1;
        Mockito.when(surveyPersistence.getSurveyIdUsingCourseId(courseId)).thenReturn(surveyId);
        Mockito.when(surveyPersistence.getAllSurveyQuestions(1L)).thenReturn(this.getSampleQuestions());
        Mockito.when(surveyPersistence.getSurveyQuestionOption(2L)).thenReturn(this.getSampleQuestionOptions());

        Map<String, Object> response = surveyService.displaySurveyQuestionsToStudents(1L, surveyPersistence);

        assertNotNull(response);
        assertFalse((boolean) response.get("isSurveyPublished"));
        assertEquals(surveyId, (long) response.get("surveyId"));
        List<Question> surveyQuestionToBeTested = ((Survey) response.get("survey")).getQuestions();
        assertNotNull(surveyQuestionToBeTested);

        List<Question> answers = this.getSampleQuestions();
        assertEquals(answers.get(0).getTitle(), surveyQuestionToBeTested.get(0).getTitle());
        assertEquals(answers.get(1).getQuestionOptions().get(0).getOption(), surveyQuestionToBeTested.get(1).getQuestionOptions().get(0).getOption());
    }

    @Test
    public void submitAnswersTest() {
        String bannerId = "B00841234";
        Long surveyId = 1L;
        Survey survey = new Survey();
        survey.setQuestions(new ArrayList<>());
        Mockito.when(surveyPersistence.submitAnswers(bannerId, surveyId, survey)).thenReturn(true);
        assertTrue(surveyService.submitAnswers(bannerId, surveyId, survey, surveyPersistence));
    }

    private List<Question> getSampleQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question(1L, "Test Numeric Question", "What is Test Question?", 1L,
                Question.NUMERIC, new Date(System.currentTimeMillis()), new ArrayList<>()));
        questions.add(new Question(2L, "Test MCQ One Question", "What is Test Question?", 1L,
                Question.MULTIPLE_CHOICE_CHOOSE_ONE, new Date(System.currentTimeMillis()), this.getSampleQuestionOptions()));
        return questions;
    }

    private List<QuestionOption> getSampleQuestionOptions() {
        List<QuestionOption> options = new ArrayList<>();
        options.add(new QuestionOption(1L, "Test Option 1", 1));
        options.add(new QuestionOption(2L, "Test Option 2", 2));
        options.add(new QuestionOption(3L, "Test Option 3", 3));
        return options;
    }

    @Test
    public void validateQuestionCriteriaListTest() throws Exception {
    	List<Question> sampleQuestions = getSampleQuestions();
    	QuestionCriteriaList list = new QuestionCriteriaList(sampleQuestions);
    	list.setMembersPerGroup(5);
    	assertTrue(SurveyFactory.instance().createService().validateQuestionCriteriaList(list));
    }
    
}
