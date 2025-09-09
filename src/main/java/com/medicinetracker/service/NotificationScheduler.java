package com.medicinetracker.service;

import com.medicinetracker.model.Schedule;
import com.medicinetracker.model.UserMedicine;
import com.medicinetracker.repository.ScheduleRepository;
import com.medicinetracker.repository.UserMedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final ScheduleRepository scheduleRepository;
    private final UserMedicineRepository userMedicineRepository;

    // Runs every minute
    @Scheduled(cron = "0 * * * * *") // Use "0" for the seconds part to run at the start of the minute
    @Transactional(readOnly = true)
    public void sendDosageReminders() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.info("Running dosage reminder job for time: {}", now);

        List<Schedule> schedules = scheduleRepository.findAllActiveSchedulesByTime(now);

        for (Schedule schedule : schedules) {
            if (schedule.getUser().getFcmToken() != null) {
                String title = String.format("Time for %s's Medicine", schedule.getProfile().getName());
                String body = String.format("It's time to take %s. Dosage: %s",
                        schedule.getMedicine().getName(),
                        schedule.getMedicine().getDosage() != null ? schedule.getMedicine().getDosage() : "As prescribed");

                // Simulate sending push notification
                log.info("[PUSH NOTIFICATION] Token: {}, Title: {}, Body: {}",
                        schedule.getUser().getFcmToken(), title, body);
            }
        }
    }

    // Runs once a day at 9 AM
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional(readOnly = true)
    public void sendDailyAlerts() {
        log.info("Running daily alerts job...");

        // Low Stock Alert
        List<UserMedicine> lowStockMedicines = userMedicineRepository.findLowStockMedicines(5);
        for (UserMedicine medicine : lowStockMedicines) {
            if (medicine.getUser().getFcmToken() != null) {
                String title = String.format("Low Stock Alert for %s", medicine.getProfile().getName());
                String body = String.format("Low stock for %s. Only %d left.",
                        medicine.getName(), medicine.getQuantity());
                log.info("[PUSH NOTIFICATION] Token: {}, Title: {}, Body: {}",
                        medicine.getUser().getFcmToken(), title, body);
            }
        }

        // Expiry Alert
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        List<UserMedicine> expiringMedicines = userMedicineRepository.findExpiringMedicines(LocalDate.now(), thirtyDaysFromNow);
        for (UserMedicine medicine : expiringMedicines) {
            if (medicine.getUser().getFcmToken() != null) {
                String title = String.format("Medicine Expiry Alert for %s", medicine.getProfile().getName());
                String body = String.format("Medicine %s expires on %s.",
                        medicine.getName(), medicine.getExpiryDate());
                log.info("[PUSH NOTIFICATION] Token: {}, Title: {}, Body: {}",
                        medicine.getUser().getFcmToken(), title, body);
            }
        }
    }
}
