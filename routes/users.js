const express = require('express');
const pool = require('../db');
const { verifyToken } = require('../middleware/auth');

const router = express.Router();

// Apply the verifyToken middleware to all routes in this file
router.use(verifyToken);

// POST /api/users/fcm-token - Update FCM token for the authenticated user
router.post('/fcm-token', async (req, res) => {
  try {
    const { fcmToken } = req.body;

    // Validate required fields
    if (!fcmToken) {
      return res.status(400).json({ error: 'FCM token is required' });
    }

    // Execute an UPDATE query on the users table to set the fcm_token for the authenticated user
    await pool.query(
      'UPDATE users SET fcm_token = $1 WHERE id = $2',
      [fcmToken, req.userId]
    );

    // Return a success message
    res.status(200).json({ message: 'FCM token updated successfully' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

module.exports = router;