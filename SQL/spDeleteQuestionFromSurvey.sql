Delimiter $$
DROP PROCEDURE IF EXISTS spDeleteQuestionFromSurvey$$

CREATE PROCEDURE spDeleteQuestionFromSurvey (IN survey_id int(11), IN question_id bigint(20))
BEGIN
    delete from SurveyQuestions where surveyid = survey_id and questionid = question_id;
END$$
DELIMITER ;