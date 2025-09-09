require('dotenv').config();
const express = require('express');
const cors = require('cors');

// Import routers
const authRoutes = require('./routes/auth');
const medicineRoutes = require('./routes/medicines');
const profileRoutes = require('./routes/profiles');
const scheduleRoutes = require('./routes/schedules');
const userRoutes = require('./routes/users');
const globalMedicineRoutes = require('./routes/global_medicines');

// Import the scheduler module
const scheduler = require('./jobs/notificationScheduler');

// Initialize the Express app
const app = express();
const PORT = process.env.PORT || 300;

// Apply middleware
app.use(cors());
app.use(express.json());

// Health-check endpoint
app.get('/', (req, res) => {
  res.status(200).json({ message: 'Medicine Tracker Backend is running!' });
});

// Mount routers
app.use('/api/auth', authRoutes);
app.use('/api/medicines', medicineRoutes);
app.use('/api/users', userRoutes);
app.use('/api/profiles', profileRoutes);
app.use('/api/global-medicines', globalMedicineRoutes);
app.use('/api', scheduleRoutes); // handles nested routes like /medicines/:medicineId/schedules

// Start the server
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
  // Start the background jobs
  scheduler.start();
});