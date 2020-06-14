package com.example.attendance;

public class workersList {

    String emp_name, emp_code;
    int emp_id;
    boolean box;


    workersList(int _emp_id, String _emp_code, String _emp_name, boolean _box) {
        emp_id = _emp_id;
        emp_code = _emp_code;
        emp_name = _emp_name;
        box = _box;
    }

}
