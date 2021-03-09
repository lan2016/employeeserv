package com.paypal.bfs.test.employeeserv.util;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtils {

    public List<String> validateJson(Employee employee) {
        try {
            InputStream is = ValidationUtils.class.getResourceAsStream("/v1/schema/employee.json");
            JSONTokener tokener = new JSONTokener(is);
            JSONObject jsonSchema = new JSONObject(tokener);
            ObjectMapper Obj = new ObjectMapper();
            String jsonStr = Obj.writeValueAsString(employee);
            JSONObject request = new JSONObject(jsonStr);
            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(request);
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (ValidationException e) {
            if (e.getCausingExceptions().size() == 0) {
                List<String> list = new ArrayList<>();
                list.add(e.getMessage());

                return list;

            } else {
                return e.getCausingExceptions().stream().map(e1 -> e1.getMessage()).collect(Collectors.toList());
            }

        }

    }
}