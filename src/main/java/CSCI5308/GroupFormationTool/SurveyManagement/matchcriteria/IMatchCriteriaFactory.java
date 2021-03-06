package CSCI5308.GroupFormationTool.SurveyManagement.matchcriteria;

public interface IMatchCriteriaFactory {
	public IMatchCriteria getSimilarityCriteria();
	
	public IMatchCriteria getDissimilarityCriteria();
	
	public IMatchCriteria getXGreaterThanYCriteria(int x, int y);
	
	public IMatchCriteria getXLesserThanYCriteria(int x, int y);
	
}
