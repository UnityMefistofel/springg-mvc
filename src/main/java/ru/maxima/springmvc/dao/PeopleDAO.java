package ru.maxima.springmvc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.maxima.springmvc.models.DB;
import ru.maxima.springmvc.models.ParamRec;
import ru.maxima.springmvc.models.People;
import ru.maxima.springmvc.models.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class PeopleDAO {
    private List<People> lstPeople = new ArrayList<>();
    private int iter = 0;

    private Connection conn;

    @Autowired
    public void PeopleDAO() {
//        lstPeople.add(new People(++iter,"Alex"));
//        lstPeople.add(new People(++iter,"Vlad"));
//        lstPeople.add(new People(++iter,"Dmitry"));
//        lstPeople.add(new People(++iter,"Maxim"));

        Map<String, String> paramSet = new HashMap<>();
        paramSet.put("host", "localhost");
        paramSet.put("db", "spring");
        paramSet.put("user", "postgres");
        paramSet.put("pass", "postgres");



        try {
            conn = DB.connToPG(paramSet);
            getLstPeople();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<People> getLstPeople() {
        lstPeople.clear();

        ResultSet rs;
        try {

            rs = DB.sqlQuery(conn, "select", "select * from personslist;");

            while (rs.next()) {
                People pers = new People();

                pers.setId(rs.getInt("id"));
                pers.setName(rs.getString("name"));
                pers.setAge(rs.getInt("age"));
                pers.setEmail(rs.getString("email"));

                lstPeople.add(pers);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lstPeople;
    }

    public People getPeople(int id) {
        return lstPeople.stream()
                .filter(p -> p.getId() == id).findFirst().get();
    }

    public void addPerson(People people) {
//        people.setId(++iter);
//        lstPeople.add(people);

        try {
            int iter = 0;
            HashMap<Integer,ParamRec> params = new HashMap<>();
            params.put(++iter, new ParamRec("str",people.getName()));
            params.put(++iter, new ParamRec("int",people.getAge()));
            params.put(++iter, new ParamRec("str", people.getEmail()));

            DB.sqlPrepQuery(conn, "insert", "insert into persons (name,age,email) " +
                    "values (?,?,?);", params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editPerson(int id, People people_n) {
        //people.setName(name);

        try {
            int iter = 0;
            HashMap<Integer,ParamRec> params = new HashMap<>();
            params.put(++iter, new ParamRec("str", people_n.getName()));
            params.put(++iter, new ParamRec("int", people_n.getAge()));
            params.put(++iter, new ParamRec("str", people_n.getEmail()));
            params.put(++iter, new ParamRec("int",id));

            DB.sqlPrepQuery(conn, "update", "update persons set "+
                                             "name = ?, age = ?, email = ? "+
                                             "where id = ?;",params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePerson(int id) {
        try {
            int iter = 0;
            HashMap<Integer,ParamRec> params = new HashMap<>();
            params.put(++iter, new ParamRec("int",id));

            DB.sqlPrepQuery(conn, "update", "update persons set "+
                    "is_del = true "+
                    "where id = ?;",params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
