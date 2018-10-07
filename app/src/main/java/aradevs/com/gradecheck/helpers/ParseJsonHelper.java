package aradevs.com.gradecheck.helpers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aradevs.com.gradecheck.models.Courses;
import aradevs.com.gradecheck.models.Evaluations;
import aradevs.com.gradecheck.models.Teachers;
import aradevs.com.gradecheck.models.Users;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Ar4 on 26/08/2018.
 */
public class ParseJsonHelper {

    public ArrayList<Courses> parseJsonRegisteredCourses(JSONObject jsonObject) {
        //declaring useful variables
        ArrayList<Courses> courses = new ArrayList<>();
        JSONArray registeredArray;

        try {
            // retrieving registered courses
            registeredArray = jsonObject.getJSONArray("registeredCourse");

            //navigating through courses
            for (int i = 0; i < registeredArray.length(); i++) {
                try {
                    //useful local variables
                    ArrayList<String> tempDescription = new ArrayList<>();
                    ArrayList<String> tempEval = new ArrayList<>();
                    ArrayList<String> tempPercentages = new ArrayList<>();
                    ArrayList<String> tempPeriod = new ArrayList<>();

                    //fragmenting JSON in sections
                    JSONObject registeredCourse = registeredArray.getJSONObject(i);
                    JSONObject teacherObj = registeredCourse.getJSONObject("courseTeacher");
                    JSONObject courseObj = teacherObj.getJSONObject("course");
                    JSONArray evaArray = registeredCourse.getJSONArray("grades");

                    for (int j = 0; j < evaArray.length(); j++) {
                        //obtaining current evaluations
                        JSONObject evaObject = evaArray.getJSONObject(j);
                        JSONObject currentEva = evaObject.getJSONObject("evaluation");

                        //adding data to array list
                        tempDescription.add(currentEva.getString("description"));
                        tempEval.add(evaObject.getString("grade"));
                        tempPercentages.add(currentEva.getString("percentage"));
                        tempPeriod.add(currentEva.getString("period"));
                    }

                    //filling evaluations model
                    Evaluations e = new Evaluations(
                            tempDescription,
                            tempPeriod,
                            tempEval,
                            tempPercentages
                    );

                    //filling courses model
                    Courses c = new Courses(
                            courseObj.getString("id"),
                            courseObj.getString("name"),
                            e);

                    //filling courses array list
                    courses.add(c);

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing Error: " + e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public ArrayList<Courses> parseJsonCourses(JSONObject jsonObject) {
        //declaring useful variables
        ArrayList<Courses> courses = new ArrayList<>();
        JSONArray registeredArray;
        try {
            // retrieving registered courses
            registeredArray = jsonObject.getJSONArray("registeredCourse");

            //navigating through courses
            for (int i = 0; i < registeredArray.length(); i++) {
                try {
                    //fragmenting JSON in sections
                    JSONObject registeredCourse = registeredArray.getJSONObject(i);
                    JSONObject teacherObj = registeredCourse.getJSONObject("courseTeacher");
                    JSONObject courseObj = teacherObj.getJSONObject("course");

                    //filling evaluations model
                    Evaluations e = new Evaluations(
                            null,
                            null,
                            null,
                            null
                    );

                    //filling courses model
                    Courses c = new Courses(
                            courseObj.getString("id"),
                            courseObj.getString("name"),
                            e);

                    //filling courses array list
                    courses.add(c);

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing Error: " + e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return courses;

    }

    public Users parseJsonUsers(JSONObject jsonObject) {
        //declaring useful variables
        Users users = new Users();

        try {
            JSONObject userJson = jsonObject.getJSONObject("user");
            users.setId(jsonObject.getString("id"));
            users.setEmail(userJson.getJSONObject("person").getString("email"));
            users.setName(userJson.getJSONObject("person").getString("name"));
            users.setPhone(userJson.getJSONObject("person").getString("phone"));
            users.setSurname(userJson.getJSONObject("person").getString("surname"));
            users.setUsername(userJson.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<Teachers> parseJsonTeachers(JSONObject jsonObject) {

        ArrayList<Teachers> teachers = new ArrayList<>();

        JSONArray registeredArray;
        try {
            // retrieving registered courses
            registeredArray = jsonObject.getJSONArray("employee");

            //navigating through courses
            for (int i = 0; i < registeredArray.length(); i++) {
                try {
                    //fragmenting JSON in sections
                    JSONObject teacherObj = registeredArray.getJSONObject(i);
                    JSONObject userObj = teacherObj.getJSONObject("user");
                    JSONObject personObj = userObj.getJSONObject("person");

                    Users u = new Users(userObj.getString("id"),
                            personObj.getString("name"),
                            personObj.getString("surname"),
                            userObj.getString("username"),
                            personObj.getString("phone"),
                            personObj.getString("email"));

                    Teachers t = new Teachers(teacherObj.getString("id"), u);
                    //filling teachers array list
                    teachers.add(t);

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing Error: " + e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return teachers;
    }

    public ArrayList<Courses> parseJsonTeacherCourses(JSONObject jsonObject) {
        //declaring useful variables
        ArrayList<Courses> courses = new ArrayList<>();
        JSONArray registeredArray;

        //try {
        // retrieving registered courses
        Log.e("Tamano: ", String.valueOf(jsonObject.length()));
        try {
            registeredArray = jsonObject.getJSONArray("courseTeachers");
            //navigating through courses
            for (int i = 0; i < registeredArray.length(); i++) {
                try {
                    //fragmenting JSON in sections
                    JSONObject registeredCourse = registeredArray.getJSONObject(i);
                    JSONObject courseObj = registeredCourse.getJSONObject("course");

                    //filling evaluations model
                    Evaluations e = new Evaluations(
                            null,
                            null,
                            null,
                            null
                    );

                    //filling courses model
                    Courses c = new Courses(
                            courseObj.getString("id"),
                            courseObj.getString("name"),
                            e);

                    //filling courses array list
                    courses.add(c);

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing Error: " + e.getMessage());
                }
            }/*
            }catch (Exception e){
                JSONObject courseObj = jsonObject.getJSONObject("courseTeachers").getJSONObject("course");

                //filling evaluations model
                Evaluations evaluations = new Evaluations(
                        null,
                        null,
                        null
                );

                //filling courses model
                Courses c = new Courses(
                        courseObj.getString("id"),
                        courseObj.getString("name"),
                        evaluations);

                //filling courses array list
                courses.add(c);
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public Evaluations parseJsonCourseEvaluations(JSONObject jsonObject) {
        //filling evaluations model
        Evaluations evaluations = new Evaluations();
        JSONArray registeredArray;
        //useful local variables
        ArrayList<String> tempDescription = new ArrayList<>();
        ArrayList<String> tempEval = new ArrayList<>();
        ArrayList<String> tempPercentages = new ArrayList<>();
        ArrayList<String> tempPeriod = new ArrayList<>();
        try {
            // retrieving registered courses
            registeredArray = jsonObject.getJSONArray("evaluation");

            //navigating through courses
            for (int i = 0; i < registeredArray.length(); i++) {
                try {
                    //obtaining current evaluations
                    JSONObject evaObject = registeredArray.getJSONObject(i);
                    JSONObject grade = evaObject.getJSONObject("grades");

                    //adding data to array list
                    tempDescription.add(evaObject.getString("description"));
                    tempEval.add(grade.getString("grade"));
                    tempPercentages.add(evaObject.getString("percentage"));
                    tempPeriod.add(evaObject.getString("period"));

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing Error: " + e.getMessage());
                }
            }
            //filling evaluations model
            evaluations = new Evaluations(
                    tempDescription,
                    tempPeriod,
                    tempEval,
                    tempPercentages
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return evaluations;
    }
}

