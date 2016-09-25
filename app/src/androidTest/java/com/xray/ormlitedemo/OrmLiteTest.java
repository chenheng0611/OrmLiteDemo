package com.xray.ormlitedemo;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.query.In;
import com.xray.ormlitedemo.bean.School;
import com.xray.ormlitedemo.bean.Student;
import com.xray.ormlitedemo.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by apple on 2016/5/7.
 */
public class OrmLiteTest extends InstrumentationTestCase {

    public DatabaseHelper getHelper(){
        return DatabaseHelper.getInstance(this.getInstrumentation().getTargetContext());
    }

    public Dao<Student,Integer> getStudentDao() throws SQLException {
        return getHelper().getDao(Student.class);
    }

    public Dao<School,Integer> getSchoolDao() throws SQLException {
        return getHelper().getDao(School.class);
    }

    public void testInsert() throws SQLException {
        Dao<Student,Integer> stuDao = getStudentDao();
        Dao<School,Integer> schDao = getSchoolDao();
        School school = new School("北京大学","北京");
        Student stu1 = new Student("测试1",21,"88888888",school);
        Student stu2 = new Student("测试2",22,"88888888",school);
        Student stu3 = new Student("测试3",23,"88888888",school);
        schDao.create(school);
        stuDao.create(stu1);
        stuDao.create(stu2);
        stuDao.create(stu3);
    }

    public void testQuery() throws SQLException {
        Dao<Student,Integer> stuDao = getStudentDao();
        List<Student> students = stuDao.queryForAll();
        for(Student stu : students){
            Log.i("test","stu:"+stu.toString()+" school:"+stu.getSchool());
        }
        Dao<School,Integer> schDao = getSchoolDao();
        List<School> schools = schDao.queryForAll();
        for(School s : schools){
            Log.i("test","school:"+s.toString());
            for(Student stu : s.getStudents()){
                Log.i("test","school stu:"+stu);
            }
        }
//        Student stu1 = stuDao.queryForId(3);
//        Log.i("test","forID:"+stu1.toString());
//        List<Student> students1 = stuDao.queryForEq("name","测试2");
//        for(Student stu : students1){
//            Log.i("test","stu1："+stu.toString());
//        }
    }

    public void testUpdate() throws SQLException {
        Dao<Student,Integer> stuDao = getStudentDao();
//        UpdateBuilder update = stuDao.updateBuilder();
//        update.setWhere(update.where().eq("phone","88888888").and().gt("age",22));
//        update.updateColumnValue("name","测试更新～～");
//        update.updateColumnValue("phone","110");
//        update.update();
        stuDao.updateRaw("update tb_student set name = '测试更新～～2',phone = '119' where id = ?","1");
    }

    public void testDelete() throws SQLException {
        Dao<Student,Integer> stuDao = getStudentDao();
        stuDao.deleteById(1);
    }

    public void testTransaction() throws SQLException {
        final Dao<Student,Integer> stuDao = getStudentDao();
        final Student stu = new Student("测试事务",20,"110",new School("..",".."));
        TransactionManager.callInTransaction(
                getHelper().getConnectionSource(),
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for(int i = 0;i < 20;i++){
                            stuDao.create(stu);
//                            if(i == 10){
//                                throw new SQLException("test.......");
//                            }
                        }
                        return null;
                    }
                });
    }
}
