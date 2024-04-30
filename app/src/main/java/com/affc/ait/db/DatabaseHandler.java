package com.affc.ait.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;

import com.affc.ait.models.Branch;
import com.affc.ait.models.Course;
import com.affc.ait.models.Student;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "JIT";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the tables

        final String sqlForStudent = "CREATE TABLE Student (" +
                "    Student_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    name TEXT,          " +
                "    email TEXT,         " +
                "    login_code TEXT,         " +
                "    address TEXT,        " +
                "    city TEXT,        " +
                "    phone TEXT,          " +
                "    gender TEXT,             " +
                "    DOB TEXT,               " +
                " profile_picture BLOB " +
                ");";
        final String sqlForAdmin = "CREATE TABLE Admin (" +
                "admin_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "admin_email TEXT," +
                "admin_password TEXT" +
                ");";
        final String sqlForBranch = "CREATE TABLE Branch (" +
                "branch_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "branch_name TEXT," +
                "location TEXT" +
                ");";

        final String sqlForCourse = "CREATE TABLE Course (" +
                "     course_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    course_name TEXT,          " +
                "    start_date TEXT,         " +
                "    end_date TEXT,        " +
                "    fee REAL,        " +
                "    description TEXT,          " +
                "    max_p INTEGER             " +
                ");";

        final String sqlForEnrollment = "CREATE TABLE Enrollment (" +
                "    enrollment_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    student_ID INTEGER, " +
                "    course_ID INTEGER, " +
                "    FOREIGN KEY(student_ID) REFERENCES Student(Student_ID), " +
                "    FOREIGN KEY(course_ID) REFERENCES Course(course_ID) " +
                ");";

        final String sqlForBranchCourse = "CREATE TABLE Branch_Course (" +
                "    branch_course_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    branch_ID INTEGER, " +
                "    course_ID INTEGER, " +
                "    FOREIGN KEY(branch_ID) REFERENCES Branch(branch_ID), " +
                "    FOREIGN KEY(course_ID) REFERENCES Course(course_ID) " +
                ");";

        sqLiteDatabase.execSQL(sqlForStudent);
        sqLiteDatabase.execSQL(sqlForBranch);
        sqLiteDatabase.execSQL(sqlForCourse);
        sqLiteDatabase.execSQL(sqlForAdmin);
        sqLiteDatabase.execSQL(sqlForEnrollment);
        sqLiteDatabase.execSQL(sqlForBranchCourse);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE Student");
        sqLiteDatabase.execSQL("DROP TABLE Admin");
        sqLiteDatabase.execSQL("DROP TABLE Branch");
        sqLiteDatabase.execSQL("DROP TABLE Course");
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    /*
    list of methods needed to work with the db:
    TODO:


    DONE:

    add profile picture
    query for courses by name
    query for users by name
    add course to branch
    enroll for a course
    fetch branches for given course
    verify admin
    add student
    verify user email
    update login code
    fetch all users
    fetch all courses
    fetch single course info
    add new branch
    add new course
     */

    public void addProfilePicture(int student_id, byte[] pictureBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_ID", student_id);
        values.put("profile_picture", pictureBytes);
        db.update("Student", values, "student_ID=?", new String[]{String.valueOf(student_id)});
        db.close();
    }

    public List<Course> queryCoursesByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Course> courses = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Course WHERE course_name LIKE ?", new String[]{"%" + name + "%"});
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getDouble(4), cursor.getString(5), cursor.getInt(6));
                courses.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courses;
    }

    public List<Student> queryUsersByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Student> students = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Student WHERE name LIKE ?", new String[]{"%" + name + "%"});
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getBlob(8));
                students.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return students;
    }

    public int enrollStudentForCourse(Student student, Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Enrollment WHERE student_ID = ? AND course_ID= ?", new String[]{String.valueOf(student.getId()), String.valueOf(course.getCourse_ID())});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return -1; //TODO: Make this throw an exception maybe?
        }

        ContentValues values = new ContentValues();
        values.put("student_ID", student.getId());
        values.put("course_ID", course.getCourse_ID());
        // Inserting Row
        db.insert("Enrollment", null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        return 1;
    }

    public List<Branch> getBranchesForACourse(Course course) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT branch_ID FROM Branch_Course where course_ID = ?", new String[]{String.valueOf(course.getCourse_ID())});
        List<Integer> branch_IDs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int contact = cursor.getInt(0);
                branch_IDs.add(contact);
            } while (cursor.moveToNext());
        }

        List<Branch> branches = new ArrayList<>();
        for (int id :
                branch_IDs) {
            branches.add(getBranchInfo(id));
        }

        cursor.close();
        return branches;
    }

    public int addCourseToBranch(Course course, Branch branch) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT branch_ID FROM Branch_Course WHERE course_ID=? AND branch_ID=?", new String[]{String.valueOf(course.getCourse_ID()), String.valueOf(branch.getBranch_id())});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return -1; //TODO: Make this throw an exception maybe?
        }

        ContentValues values = new ContentValues();
        values.put("course_ID", course.getCourse_ID());
        values.put("branch_ID", branch.getBranch_id());


        // Inserting Row
        db.insert("Student", null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        return 1;
    }


    public int addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Student WHERE email = ?", new String[]{student.getEmail()});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return -1; //TODO: Make this throw an exception maybe?
        }

        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("email", student.getEmail());
        values.put("login_code", student.getLogin_code());
        values.put("address", student.getAddress());
        values.put("city", student.getCity());
        values.put("phone", student.getPhone());
        values.put("gender", student.getGender());
        values.put("DOB", student.getDOB());

        // Inserting Row
        db.insert("Student", null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        return 1;
    }

    //NOTE: MIGHT HAVE TO CHANGE Picture datatype
    public int addProfilePicture(int student_id, PictureDrawable picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("student_ID", student_id);
        values.put("profile_picture", "STUB");

        // updating row
        return db.update("Student", values, "student_ID" + " = ?", new String[]{Integer.toString(student_id)});
    }

    public boolean verifyEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Student", new String[]{"id"}, "email=?",
                new String[]{email}, null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    public int updateLoginCode(Student student) {
        String code = student.generateLoginCode();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_ID", student.getId());
        values.put("login_code", code);
        return db.update("Student", values, "student_ID" + " = ?", new String[]{String.valueOf(student.getId())});


    }

    public boolean authStudent(String email, String login_code) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Student", new String[]{"id"}, "email=? AND login_code=?",
                new String[]{email, login_code}, null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    public List<Student> fetchStudents() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Student", null);
        List<Student> students = new ArrayList<Student>();

        if (cursor.moveToFirst()) {
            do {
                Student contact = new Student(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getBlob(8)
                );
                students.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return students;
    }

    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("course_name", course.getCourse_name());
        values.put("start_date", course.getStart_date());
        values.put("end_date", course.getEnd_date());
        values.put("fee", course.getFee());
        values.put("description", course.getDescription());
        values.put("max_p", course.getMax_p());
        // Inserting Row
        db.insert("Course", null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<Course> fetchCourses() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Course", null);
        List<Course> courses = new ArrayList<Course>();

        if (cursor.moveToFirst()) {
            do {
                Course contact = new Course(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getInt(6)
                );
                courses.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return courses;
    }

    public Course fetchSingleCourse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Course", new String[]{"course_ID", "course_name", "start_date", "end_date", "fee", "description", "max_p"}, "course_ID=?", new String[]{Integer.toString(id)}, null, null, null);
        if (cursor != null) {
            return new Course(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getDouble(4),
                    cursor.getString(5),
                    cursor.getInt(6));
        } else {
            return null;
        }
    }

    public void addBranch(Branch branch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("branch_name", branch.getBranch_name());
        values.put("branch_location", branch.getLocation());
        db.insert("Branch", null, values);
        db.close();
    }

    public Branch getBranchInfo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Branch WHERE branch_ID=?", new String[]{String.valueOf(id)});
        if (cursor != null) {
            return new Branch(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2));
        } else {
            return null;
        }
    }

    public boolean addAdmin(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the admin with the given email already exists
        Cursor cursor = db.rawQuery("SELECT * FROM Admin WHERE admin_email = ?", new String[]{email});
        if (cursor.getCount() > 0) {

            cursor.close();
            db.close();
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("admin_email", email);
        values.put("admin_password", password);

        // Insert the admin if it doesn't already exist
        long result = db.insert("Admin", null, values);
        db.close();


        return result != -1;
    }

    public boolean authAdmin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String password_hash = getPasswordHash(password);

        Cursor cursor = db.query("Student", new String[]{"id"}, "email=? AND password=?",
                new String[]{email, password_hash}, null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private String getPasswordHash(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Apply the hash function to the password bytes
            byte[] hashBytes = digest.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // Handle the case where the requested algorithm is not available
            e.printStackTrace();
            return null; // Or throw an exception
        }
    }


}
