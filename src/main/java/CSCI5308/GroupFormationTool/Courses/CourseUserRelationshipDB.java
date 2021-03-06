package CSCI5308.GroupFormationTool.Courses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import CSCI5308.GroupFormationTool.AccessControl.IUser;
import CSCI5308.GroupFormationTool.AccessControl.User;
import CSCI5308.GroupFormationTool.Database.CallStoredProcedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import CSCI5308.GroupFormationTool.Database.DatabaseAbstractFactory;
import CSCI5308.GroupFormationTool.Database.ICallStoredProcedure;

public class CourseUserRelationshipDB implements ICourseUserRelationshipPersistence
{
	private Logger log = LoggerFactory.getLogger(CourseUserRelationshipDB.class);
	public List<IUser> findAllUsersWithoutCourseRole(Role role, long courseID)
	{
		log.trace("Loading all users without a role: {} for a course with ID: {} from database.", role.toString(), courseID);
		List<IUser> users = new ArrayList<>();
		ICallStoredProcedure proc = null;
		try
		{
			proc = DatabaseAbstractFactory.instance().makeCallStoredProcedure("spFindUsersWithoutCourseRole(?, ?)");
			proc.setParameter(1, role.toString());
			proc.setParameter(2,  courseID);
			ResultSet results = proc.executeWithResults();
			if (null != results)
			{
				while (results.next())
				{
					long userID = results.getLong(1);
					String bannerID = results.getString(2);
					String firstName = results.getString(3);
					String lastName = results.getString(4);
					User u = new User();
					u.setID(userID);
					u.setBannerID(bannerID);
					u.setFirstName(firstName);
					u.setLastName(lastName);
					users.add(u);
				}
			}
		}
		catch (SQLException e)
		{
			log.error("Error while loading all users without a role: {} for a course with ID: {} from database, error: {}", role.toString(), courseID, e.getMessage());
		}
		finally
		{
			if (null != proc)
			{
				proc.cleanup();
			}
		}
		return users;
	}

	public List<IUser> findAllUsersWithCourseRole(Role role, long courseID)
	{
		log.trace("Loading all users with a role: {} for course with ID: {} from database.", role.toString(), courseID);
		List<IUser> users = new ArrayList<>();
		ICallStoredProcedure proc = null;
		try
		{
			proc = DatabaseAbstractFactory.instance().makeCallStoredProcedure("spFindUsersWithCourseRole(?, ?)");
			proc.setParameter(1, role.toString());
			proc.setParameter(2,  courseID);
			ResultSet results = proc.executeWithResults();
			if (null != results)
			{
				while (results.next())
				{
					long userID = results.getLong(1);
					User u = new User();
					u.setID(userID);
					users.add(u);
				}
			}
		}
		catch (SQLException e)
		{
			log.error("Error while loading all users with a role: {} for a course with ID: {} from database, error: {}", role.toString(), courseID, e.getMessage());
		}
		finally
		{
			if (null != proc)
			{
				proc.cleanup();
			}
		}
		return users;
	}
	
	public boolean enrollUser(ICourse course, IUser user, Role role)
	{
		log.trace("Enrolling a user with bannerID: {} with a role: {} to a course with ID: {} in database.", user.getBannerID(), role.toString(), course.getId());
		ICallStoredProcedure proc = null;
		try
		{
			proc = DatabaseAbstractFactory.instance().makeCallStoredProcedure("spEnrollUser(?, ?, ?)");
			proc.setParameter(1, course.getId());
			proc.setParameter(2, user.getID());
			proc.setParameter(3, role.toString());
			proc.execute();
		}
		catch (SQLException e)
		{
			log.error("Error while enrolling a user with bannerID: {} with a role: {} to a course with ID: {} in database, error: {}", user.getBannerID(), role.toString(), course.getId(), e.getMessage());
			return false;
		}
		finally
		{
			if (null != proc)
			{
				proc.cleanup();
			}
		}
		return true;
	}

	public List<Role> loadUserRolesForCourse(ICourse course, IUser user)
	{
		log.trace("Loading all roles of a user with bannerID: {} for a course with ID: {} from database.", user.getBannerID(), course.getId());
		List<Role> roles = new ArrayList<Role>();
		ICallStoredProcedure proc = null;
		try
		{
			proc = DatabaseAbstractFactory.instance().makeCallStoredProcedure("spLoadUserRolesForCourse(?, ?)");
			proc.setParameter(1, course.getId());
			proc.setParameter(2, user.getID());
			ResultSet results = proc.executeWithResults();
			if (null != results)
			{
				while (results.next())
				{
					Role role = Role.valueOf(results.getString(1).toUpperCase());
					roles.add(role);
				}
			}
		}
		catch (SQLException e)
		{
			log.error("Error while loading all roles of a user with bannerID: {} for a course with ID: {} in database, error: {}", user.getBannerID(), course.getId(), e.getMessage());
		}
		finally
		{
			if (null != proc)
			{
				proc.cleanup();
			}
		}
		return roles;
	}
}
