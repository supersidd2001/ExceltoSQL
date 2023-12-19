package com.my.qfc.service.code.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Scheduled(fixedRate = 5000) // Run every 5 seconds, adjust as needed
    public void performUseCase3() {
        // Add your Use Case 3 logic here
        System.out.println("Use Case 3 logic executed");
    }
}
