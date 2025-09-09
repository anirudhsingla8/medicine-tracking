const cron = require('node-cron');
const pool = require('../db');

// Placeholder function to send push notifications
const sendPushNotification = (token, title, body) => {
  console.log(`[PUSH NOTIFICATION] Token: ${token}, Title: ${title}, Body: ${body}`);
  // In a real implementation, this would integrate with a push notification service like FCM
};

// Function to start all cron jobs
const start = () => {
  console.log('Starting notification scheduler...');
  
  // Dosage Reminder Job - runs every minute
  cron.schedule('* * * * *', async () => {
    try {
      console.log('Running dosage reminder job...');
      
      // Get the current time (formatted as HH:MM:00)
      const now = new Date();
      const currentTime = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}:00`;
      
      // Query the schedules table for all active schedules matching the current time
      // JOIN with users (for fcm_token), profiles (for name), and user_medicines (for name and dosage)
      const schedulesResult = await pool.query(
        `SELECT
          s.id,
          u.fcm_token,
          p.name as profile_name,
          m.name as medicine_name,
          m.dosage
        FROM schedules s
        JOIN users u ON s.user_id = u.id
        JOIN profiles p ON s.profile_id = p.id
        JOIN user_medicines m ON s.medicine_id = m.id
        WHERE s.is_active = true AND s.time_of_day = $1`,
        [currentTime]
      );
      
      // For each result, call the placeholder sendPushNotification function
      for (const schedule of schedulesResult.rows) {
        if (schedule.fcm_token) {
          const title = `Time for ${schedule.profile_name}'s Medicine`;
          const body = `It's time to take ${schedule.medicine_name}. Dosage: ${schedule.dosage || 'As prescribed'}`;
          sendPushNotification(schedule.fcm_token, title, body);
        }
      }
    } catch (err) {
      console.error('Error in dosage reminder job:', err);
    }
  });
  
  // Daily Alerts Job - runs once a day at 9 AM
  cron.schedule('0 9 * * *', async () => {
    try {
      console.log('Running daily alerts job...');
      
      // Low Stock Alert
      // Query the user_medicines table for entries where quantity is below a set threshold (e.g., 5)
      // JOIN with users and profiles
      const lowStockResult = await pool.query(
        `SELECT
          m.id,
          m.quantity,
          u.fcm_token,
          p.name as profile_name,
          m.name as medicine_name
        FROM user_medicines m
        JOIN users u ON m.user_id = u.id
        JOIN profiles p ON m.profile_id = p.id
        WHERE m.quantity < 5 AND m.quantity >= 0`
      );
      
      // For each result, trigger a notification
      for (const medicine of lowStockResult.rows) {
        if (medicine.fcm_token) {
          const title = `Low Stock Alert for ${medicine.profile_name}`;
          const body = `Low stock for ${medicine.medicine_name}. Only ${medicine.quantity} left.`;
          sendPushNotification(medicine.fcm_token, title, body);
        }
      }
      
      // Expiry Alert
      // Query user_medicines for entries where expiry_date is within a set window (e.g., 30 days)
      // JOIN with users and profiles
      const expiryDate = new Date();
      expiryDate.setDate(expiryDate.getDate() + 30);
      
      const expiryResult = await pool.query(
        `SELECT
          m.id,
          m.expiry_date,
          u.fcm_token,
          p.name as profile_name,
          m.name as medicine_name
        FROM user_medicines m
        JOIN users u ON m.user_id = u.id
        JOIN profiles p ON m.profile_id = p.id
        WHERE m.expiry_date <= $1 AND m.expiry_date >= NOW()`,
        [expiryDate.toISOString().split('T')[0]]
      );
      
      // For each result, trigger a notification
      for (const medicine of expiryResult.rows) {
        if (medicine.fcm_token) {
          const title = `Medicine Expiry Alert for ${medicine.profile_name}`;
          const body = `Medicine ${medicine.medicine_name} expires on ${medicine.expiry_date}.`;
          sendPushNotification(medicine.fcm_token, title, body);
        }
      }
    } catch (err) {
      console.error('Error in daily alerts job:', err);
    }
  });
  
  console.log('Notification scheduler started with 2 jobs.');
};

module.exports = { start };