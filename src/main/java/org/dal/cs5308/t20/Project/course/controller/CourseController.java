package org.dal.cs5308.t20.Project.course.controller;

import org.dal.cs5308.t20.Project.Factory;
import org.dal.cs5308.t20.Project.admin.IAdminService;
import org.dal.cs5308.t20.Project.course.bo.Student;
import org.dal.cs5308.t20.Project.course.exception.CourseException;
import org.dal.cs5308.t20.Project.course.service.ICourseService;
import org.dal.cs5308.t20.Project.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IAdminService adminService;

    @GetMapping("/course/{id}/students/register/upload-csv")
    public String getIndex(Model model, @PathVariable("id") Long courseId) {
        try {
            this.courseService.isValidCourse(courseId);
        } catch (CourseException e) {
            model.addAttribute("invalidCourse", e.getMessage());
        }
        model.addAttribute("fileName", new String());
        model.addAttribute("courseId", courseId);
        return "studentRegistrationUploadCsv";
    }

    @PostMapping(value = "/course/{id}/students/register/upload-csv")
    public String registerUploadedStudent(@ModelAttribute String fileName, @PathVariable("id") Long courseId,
                                          @RequestParam("file") MultipartFile file,
                                          Model model) {
        if (fileName == null || fileName.isEmpty() || file == null){
            model.addAttribute("invalidFile","Please provide valid file.");
        }
        try {
            this.courseService.registerUploadedStudent(courseId, file, fileName);
        }catch (CourseException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("fileName", fileName);
            model.addAttribute("courseId", courseId);
            return "studentRegistrationUploadCsv";
        }
        return "redirect:/course/" + courseId + "/students";
    }

    @GetMapping("/course/{id}/students")
    public String getRegisteredStudentForCourse(Model model, @PathVariable("id") Long courseId) {
        List<User> students = null;
        try {
            students = this.courseService.getRegisteredStudentForCourse(courseId);
        } catch (CourseException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("students", students);
        return "studentRegistered";
    }

    @GetMapping("/course/{id}/user/search")
    public String searchUser(Model model, @PathVariable("id") Long courseId) {
        try{
            this.courseService.isValidCourse(courseId);
        } catch (CourseException e) {
            model.addAttribute("invalidCourse", e.getMessage());
        }
        model.addAttribute("courseId", courseId);
        return "userSearch";
    }

    @GetMapping("/course/{id}/ta")
    public String getCourseTAs(Model model, @PathVariable("id") Long courseId) {
        List<User> students = null;
        try {
            students = this.courseService.getCourseTAs(courseId);
        } catch (CourseException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("users", students);
        return "courseTAs";
    }

    @PostMapping("/course/{id}/ta/{userId}/assign")
    public String assignCourseTA(@PathVariable("id") Long courseId, @PathVariable("userId") Long userId,
                                 Model model) {
        try {
            this.courseService.assignCourseTA(courseId, userId);
        } catch (CourseException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "courseTAs";
        }
        return "redirect:/course/" + courseId + "/ta";
    }

    @PostMapping("/user/search")
    public String searchUser(Model model, @RequestParam(value = "bannerId", required = false) String bannerId,
                             @RequestParam(value = "emailId", required = false) String emailId,
                             @RequestParam(value = "courseId", required = false) Long courseId){
    	try {
    		model.addAttribute("users", Factory.getUserService().searchUsers(bannerId, emailId));
    	} catch(Exception e) {
    		model.addAttribute("errorMsg", e.getMessage());
    	}
        model.addAttribute("courseId",courseId);
        model.addAttribute("emailId",emailId);
        model.addAttribute("bannerId", bannerId);
        return "userSearch";
    }

    @RequestMapping(value="/courses/{id}",method= RequestMethod.GET)
    public String getCoursePage(Model model, @PathVariable("id") int course_id, @RequestParam(value="cname") String course_name)
    {
        model.addAttribute("course_id",course_id);
        model.addAttribute("course_name",course_name);
        return "coursepage";
    }

    @RequestMapping("/home")
    public String viewAllCourses(Model model){
        model.addAttribute("courses", adminService.getAllCourse());
        return "userHome";
    }

}
