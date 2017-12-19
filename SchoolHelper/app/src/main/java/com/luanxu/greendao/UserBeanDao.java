package com.luanxu.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.luanxu.bean.user.UserBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_BEAN".
*/
public class UserBeanDao extends AbstractDao<UserBean, String> {

    public static final String TABLENAME = "USER_BEAN";

    /**
     * Properties of entity UserBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property StudentNumber = new Property(1, String.class, "studentNumber", false, "STUDENT_NUMBER");
        public final static Property SchoolId = new Property(2, String.class, "schoolId", false, "SCHOOL_ID");
        public final static Property SchoolName = new Property(3, String.class, "schoolName", false, "SCHOOL_NAME");
        public final static Property College = new Property(4, String.class, "college", false, "COLLEGE");
        public final static Property Career = new Property(5, String.class, "career", false, "CAREER");
        public final static Property Grade = new Property(6, String.class, "grade", false, "GRADE");
        public final static Property Name = new Property(7, String.class, "name", false, "NAME");
        public final static Property Sex = new Property(8, String.class, "sex", false, "SEX");
        public final static Property Race = new Property(9, String.class, "race", false, "RACE");
        public final static Property ContaceNumber = new Property(10, String.class, "contaceNumber", false, "CONTACE_NUMBER");
        public final static Property Email = new Property(11, String.class, "email", false, "EMAIL");
        public final static Property PoloticeStatus = new Property(12, String.class, "poloticeStatus", false, "POLOTICE_STATUS");
        public final static Property ClassName = new Property(13, String.class, "className", false, "CLASS_NAME");
    }


    public UserBeanDao(DaoConfig config) {
        super(config);
    }
    
    public UserBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_BEAN\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"STUDENT_NUMBER\" TEXT," + // 1: studentNumber
                "\"SCHOOL_ID\" TEXT," + // 2: schoolId
                "\"SCHOOL_NAME\" TEXT," + // 3: schoolName
                "\"COLLEGE\" TEXT," + // 4: college
                "\"CAREER\" TEXT," + // 5: career
                "\"GRADE\" TEXT," + // 6: grade
                "\"NAME\" TEXT," + // 7: name
                "\"SEX\" TEXT," + // 8: sex
                "\"RACE\" TEXT," + // 9: race
                "\"CONTACE_NUMBER\" TEXT," + // 10: contaceNumber
                "\"EMAIL\" TEXT," + // 11: email
                "\"POLOTICE_STATUS\" TEXT," + // 12: poloticeStatus
                "\"CLASS_NAME\" TEXT);"); // 13: className
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserBean entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String studentNumber = entity.getStudentNumber();
        if (studentNumber != null) {
            stmt.bindString(2, studentNumber);
        }
 
        String schoolId = entity.getSchoolId();
        if (schoolId != null) {
            stmt.bindString(3, schoolId);
        }
 
        String schoolName = entity.getSchoolName();
        if (schoolName != null) {
            stmt.bindString(4, schoolName);
        }
 
        String college = entity.getCollege();
        if (college != null) {
            stmt.bindString(5, college);
        }
 
        String career = entity.getCareer();
        if (career != null) {
            stmt.bindString(6, career);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(7, grade);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(8, name);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(9, sex);
        }
 
        String race = entity.getRace();
        if (race != null) {
            stmt.bindString(10, race);
        }
 
        String contaceNumber = entity.getContaceNumber();
        if (contaceNumber != null) {
            stmt.bindString(11, contaceNumber);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(12, email);
        }
 
        String poloticeStatus = entity.getPoloticeStatus();
        if (poloticeStatus != null) {
            stmt.bindString(13, poloticeStatus);
        }
 
        String className = entity.getClassName();
        if (className != null) {
            stmt.bindString(14, className);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserBean entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String studentNumber = entity.getStudentNumber();
        if (studentNumber != null) {
            stmt.bindString(2, studentNumber);
        }
 
        String schoolId = entity.getSchoolId();
        if (schoolId != null) {
            stmt.bindString(3, schoolId);
        }
 
        String schoolName = entity.getSchoolName();
        if (schoolName != null) {
            stmt.bindString(4, schoolName);
        }
 
        String college = entity.getCollege();
        if (college != null) {
            stmt.bindString(5, college);
        }
 
        String career = entity.getCareer();
        if (career != null) {
            stmt.bindString(6, career);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(7, grade);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(8, name);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(9, sex);
        }
 
        String race = entity.getRace();
        if (race != null) {
            stmt.bindString(10, race);
        }
 
        String contaceNumber = entity.getContaceNumber();
        if (contaceNumber != null) {
            stmt.bindString(11, contaceNumber);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(12, email);
        }
 
        String poloticeStatus = entity.getPoloticeStatus();
        if (poloticeStatus != null) {
            stmt.bindString(13, poloticeStatus);
        }
 
        String className = entity.getClassName();
        if (className != null) {
            stmt.bindString(14, className);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public UserBean readEntity(Cursor cursor, int offset) {
        UserBean entity = new UserBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // studentNumber
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // schoolId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // schoolName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // college
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // career
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // grade
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // name
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // sex
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // race
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // contaceNumber
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // email
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // poloticeStatus
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // className
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setStudentNumber(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSchoolId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSchoolName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCollege(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCareer(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGrade(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSex(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRace(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setContaceNumber(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setEmail(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setPoloticeStatus(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setClassName(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    @Override
    protected final String updateKeyAfterInsert(UserBean entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(UserBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
