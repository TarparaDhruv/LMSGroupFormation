package org.dal.cs5308.t20.Project.course;

import org.dal.cs5308.t20.Project.user.User;

import java.sql.SQLException;
import java.util.List;

public interface ICourseDAO {
	
	public int addCourse(Course course) throws SQLException;
	
	public int delCourse(Long course_id) throws SQLException;
	
	public List<Course> getAllCourses() throws SQLException;
	
	public int addInstructor(String email_id,Long course_id) throws SQLException;

	public List<Course> getUserCourses(String emailId) throws SQLException;

}
