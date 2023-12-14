package com.my.qfc.common;

import java.util.ArrayList;
import java.util.List;

import com.my.qfc.common.util.BulkInsertionService;
import com.my.qfc.common.vo.UserEntityDAO;
import com.my.qfc.common.vo.UserVO;

public class AppBulk {

    public static void main(String[] args) {
        // Create instances of UserEntityDAO and BulkInsertionService
        UserEntityDAO userEntityDAO = new UserEntityDAO();
        BulkInsertionService bulkInsertionService = new BulkInsertionService(userEntityDAO);

        // Create a list of UserVO for testing
        List<UserVO> userVOList = createSampleUserVOList();

        // Perform bulk insertion
        bulkInsertionService.bulkInsertFromExcel("");

        // Note: Make sure to have appropriate configurations for EntityManager and database connection.
    }

    private static List<UserVO> createSampleUserVOList() {
        List<UserVO> userVOList = new ArrayList<>();

        // Add sample UserVO objects to the list
        
        // Add more sample data as needed

        return userVOList;
    }
}
