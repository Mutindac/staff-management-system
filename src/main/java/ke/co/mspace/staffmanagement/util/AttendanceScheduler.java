package ke.co.mspace.staffmanagement.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ke.co.mspace.staffmanagement.dao.AttendanceDAO;
import ke.co.mspace.staffmanagement.dao.StaffDAO;
import ke.co.mspace.staffmanagement.model.Attendance;
import ke.co.mspace.staffmanagement.model.Staff;

import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AttendanceScheduler implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private static final ZoneId TIMEZONE = ZoneId.of("Africa/Nairobi");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newScheduledThreadPool(2);
        
        scheduleDailyTask(LocalTime.of(8, 30), AttendanceScheduler::checkMissingCheckIns);
        scheduleDailyTask(LocalTime.of(17, 30), AttendanceScheduler::checkMissingCheckOuts);
        
        System.out.println("Attendance Email Scheduler Initialized.");
    }

    private void scheduleDailyTask(LocalTime targetTime, Runnable task) {
        ZonedDateTime now = ZonedDateTime.now(TIMEZONE);
        ZonedDateTime nextRun = now.with(targetTime);

        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();
        long period = TimeUnit.DAYS.toSeconds(1);

        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
        System.out.println("Scheduled task for " + targetTime + " starting in " + initialDelay + " seconds.");
    }

    public static void checkMissingCheckIns() {
        System.out.println("Running 8:30 AM Missing Check-in Job...");
        try {
            Connection conn = DButil.getConnection();
            StaffDAO staffDAO = new StaffDAO(conn);
            AttendanceDAO attendanceDAO = new AttendanceDAO(conn);

            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            List<Staff> allStaff = staffDAO.getAllStaff();

            for (Staff staff : allStaff) {
                // Ignore inactive staff if necessary, assuming all in list should be checked
                if (!"ACTIVE".equalsIgnoreCase(staff.getStatus())) {
                    continue;
                }
                Attendance attendance = attendanceDAO.getTodayAttendance(staff.getStaffId(), today);
                if (attendance == null || attendance.getCheckInTime() == null) {
                    // Send reminder
                    String subject = "Reminder: Please Check In for Today";
                    String message = "Hello " + staff.getFirstName() + ",\n\n"
                            + "This is an automated reminder. It's past 8:30 AM and you haven't checked in yet.\n"
                            + "Please log into the Staff Management System and check in as soon as possible.\n\n"
                            + "Thank you.";
                    EmailUtil.sendReminderEmail(staff.getEmail(), subject, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkMissingCheckOuts() {
        System.out.println("Running 5:30 PM Missing Check-out Job...");
        try {
            Connection conn = DButil.getConnection();
            StaffDAO staffDAO = new StaffDAO(conn);
            AttendanceDAO attendanceDAO = new AttendanceDAO(conn);

            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            List<Staff> allStaff = staffDAO.getAllStaff();

            for (Staff staff : allStaff) {
                if (!"ACTIVE".equalsIgnoreCase(staff.getStatus())) {
                    continue;
                }
                Attendance attendance = attendanceDAO.getTodayAttendance(staff.getStaffId(), today);
                if (attendance != null && attendance.getCheckInTime() != null && attendance.getCheckOutTime() == null) {
                    // Sent reminder
                    String subject = "Reminder: Please Check Out for Today";
                    String message = "Hello " + staff.getFirstName() + ",\n\n"
                            + "This is an automated reminder. It's past 5:30 PM and you haven't checked out yet.\n"
                            + "Please log into the Staff Management System and check out before you leave.\n\n"
                            + "Thank you.";
                    EmailUtil.sendReminderEmail(staff.getEmail(), subject, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
            System.out.println("Attendance Email Scheduler Shutdown.");
        }
    }
}
